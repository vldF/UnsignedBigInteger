import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static java.lang.Integer.max;

public class UnsignedBigInteger {
    private static final int base = 1_000_000_000; // max degree of 10, that int can describe
    private static final int digitsCount = 9; // max degree of 10, that int can describe
    List<Integer> value;

    UnsignedBigInteger(String str) {
        StringBuilder numberStringBuilder = new StringBuilder(str);
        value = new ArrayList<>();

        // 9 is count of decimal digits in one array element
        for (int i = 0; i < str.length(); i += digitsCount) {
            int lastCharIndex = numberStringBuilder.length();
            String nextDigits = numberStringBuilder.substring(max(0, lastCharIndex - digitsCount), lastCharIndex);
            numberStringBuilder.delete(max(0, lastCharIndex - 9), lastCharIndex);
            value.add(Integer.parseInt(nextDigits));
        }
    }

    private UnsignedBigInteger() {
        value = new ArrayList<>();
    }

    private UnsignedBigInteger(List<Integer> value) {
        this.value = value;
    }

    //todo
    UnsignedBigInteger(int value) {
        this(Integer.toString(value));
    }

    enum equality {
        LESS,
        MORE,
        EQUALS
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        int k = 0;
        for (int i : value) {
            k++;
            String stringify = k == value.size() ? Integer.toString(i) : String.format("%09d", i); // 9 - base
            res.insert(0, stringify);
        }
        return res.toString();
    }

    private Iterator<Integer> getIterable() {
        return value.iterator();
    }

    private ListIterator<Integer> getBackwardIterable() {
        return value.listIterator(value.size());
    }

    private void addDigit(int digit) {
        value.add(digit);
    }

    private void shift(int n) {
        if (n > 0) {
            for (int i = 0; i < n; i++)
                value.add(0, 0);
        }else {
            for (int i = 0; i < -n; i++)
                value.remove(0);
        }
    }

    private int getDigitCount() {
        return value.size(); // even if value.size() more than Integer.MAX_VALUE, return Integer.MAX_VALUE. Methods below will be right
    }

    private void popZeros() {
        int firstNonZero = 0;
        for (int i = this.value.size() - 1; i >= 0; i--) {
            if (this.value.get(i) != 0) {
                firstNonZero = i;
                break;
            }
        }
        this.value = this.value.subList(0, firstNonZero + 1);
    }

    //todo что, если n больше, чем надо?
    private UnsignedBigInteger getLast(int n) {
        if (n > value.size()) return this;
        return new UnsignedBigInteger(value.subList(max(0, value.size() - n), value.size()));
    }

    private UnsignedBigInteger copy() {
        UnsignedBigInteger res = new UnsignedBigInteger();
        res.value.addAll(this.value);
        return res;
    }

    private static UnsignedBigInteger leftSubtract(UnsignedBigInteger a, UnsignedBigInteger b) {
        UnsignedBigInteger bShifted = b.copy();
        bShifted.shift(a.getDigitCount() - b.getDigitCount());
        UnsignedBigInteger res = a.subtract(bShifted);
        res.popZeros();
        return res;
    }

    /**
     * A.add(B) ~ A + B
     * adds a with b and returns result
     * @param other UnsignedBigInteger
     * @return UnsignedBigInteger
     */
    public UnsignedBigInteger add(UnsignedBigInteger other) {
        UnsignedBigInteger res = new UnsignedBigInteger();

        Iterator<Integer> aIter = this.getIterable();
        Iterator<Integer> bIter = other.getIterable();

        int transfer = 0;
        while (aIter.hasNext() || bIter.hasNext()) {
            int newValue = 0;
            if (aIter.hasNext()) newValue += aIter.next();
            if (bIter.hasNext()) newValue += bIter.next();
            newValue += transfer;
            transfer = newValue / base;
            newValue %= base;
            res.addDigit(newValue);
        }

        if (transfer != 0) {
            res.addDigit(transfer);
        }
        return res;
    }

    /**
     * A.add(B) ~ A + B
     * adds a with b and returns result
     * @param other int
     * @return UnsignedBigInteger
     */
    public UnsignedBigInteger add(int other) {
        UnsignedBigInteger res = new UnsignedBigInteger();
        Iterator<Integer> aIter = this.getIterable();

        int transfer = other;
        while (aIter.hasNext()) {
            int val = aIter.next();
            val += transfer;
            transfer = val < 0 ? (int) Math.floor(val * 1.0 / base) : val / base;
            val = Math.floorMod(val, base);
            res.addDigit(val);
        }
        if (transfer != 0) {
            res.addDigit(transfer);
        }

        return res;
    }

    /**
     * A.subtract(B) ~ A - B
     * adds a with b and returns result
     * @param other UnsignedBigInteger
     * @return UnsignedBigInteger
     */
    public UnsignedBigInteger subtract(UnsignedBigInteger other) {
        UnsignedBigInteger res = new UnsignedBigInteger();

        Iterator<Integer> aIter = this.getIterable();
        Iterator<Integer> bIter = other.getIterable();

        int transfer = 0;
        while (aIter.hasNext() || bIter.hasNext()) {
            int newValue = 0;
            if (aIter.hasNext()) newValue += aIter.next();
            if (bIter.hasNext()) newValue -= bIter.next();
            newValue += transfer;
            transfer = newValue < 0 ? (int) Math.floor(newValue * 1.0 / base) : 0;
            newValue = Math.floorMod(newValue, base);
            res.addDigit(newValue);
        }
        return res;
    }

    /**
     * A.mul(B) ~ A * B
     * adds a with b and returns result
     * @param other long
     * @return UnsignedBigInteger
     */
    public UnsignedBigInteger mul(long other) {
        UnsignedBigInteger res = new UnsignedBigInteger();
        Iterator<Integer> aIter = this.getIterable();

        int transfer = 0;
        while (aIter.hasNext()) {
            long newVal = aIter.next() * other + transfer;
            transfer = (int) (newVal / base);
            res.addDigit((int) (newVal % base));
        }

        if (transfer != 0) {
            res.addDigit(transfer);
        }

        return res;
    }

    /**
     * A.mul(B) ~ A * B
     * adds a with b and returns result
     * @param other UnsignedBigInteger
     * @return UnsignedBigInteger
     */
    public UnsignedBigInteger mul(UnsignedBigInteger other) {
        UnsignedBigInteger res = new UnsignedBigInteger();
        Iterator<Integer> aIter = this.getIterable();

        int shift = 0;
        while (aIter.hasNext()) {
            UnsignedBigInteger newNum = other.mul(aIter.next());
            newNum.shift(shift);
            res = res.add(newNum);
            shift++;
        }
        return res;
    }

    /**
     * A.div(B) ~ A / B
     * adds a with b and returns result
     * @param other int
     * @return UnsignedBigInteger
     */
    public UnsignedBigInteger div(int other) {
        UnsignedBigInteger res = new UnsignedBigInteger();
        res.value = this.value;
        int carry = 0;
        for (int i = this.value.size() - 1; i >= 0; i--) {
            long cur = this.value.get(i) + (long) carry * base;
            this.value.set(i, (int) (cur / other));
            carry = (int) (cur % other);
        }
        res.popZeros();
        return res;
    }

    /**
     * A.div(B) ~ A / B
     * adds a with b and returns result
     * @param other UnsignedBigInt
     * @return UnsignedBigInteger
     */
    public UnsignedBigInteger div(UnsignedBigInteger other) {
        UnsignedBigInteger res = new UnsignedBigInteger();
        UnsignedBigInteger dividend = this.copy();
        while (dividend.isMoreOrEquals(other)) {
            UnsignedBigInteger a1 = dividend.getLast(other.getDigitCount());
            UnsignedBigInteger divisor = other.copy();
            if (a1.isLessThan(other)) {
                a1 = dividend.getLast(other.getDigitCount() + 1);
            }

            int r = 1;
            while (divisor.mul(r).isLessOrEquals(a1)) {
                r *= 2;
            }
            int lower = r / 2;
            int upper = r;

            while (true) {
                r = lower / 2 + upper / 2;
                if (divisor.mul(r).isLessThan(a1)) {
                    lower = r;
                } else if (divisor.mul(r).isMoreThan(a1)) {
                    upper = r;
                } else {
                    break;
                }

                if (upper - lower == 1) {
                    r = lower;
                    break;
                }
            }

            res.value.add(0, r);
            dividend = leftSubtract(dividend, other.mul(r));
        }
        return res;
    }

    private static equality CompareAAndB(UnsignedBigInteger a, UnsignedBigInteger b) {
        if (a.getDigitCount() > b.getDigitCount()) return equality.MORE;
        else if (a.getDigitCount() < b.getDigitCount()) return equality.LESS;

        ListIterator<Integer> aIter = a.getBackwardIterable();
        ListIterator<Integer> bIter = b.getBackwardIterable();

        while (aIter.hasPrevious()) {
            int digA = aIter.previous();
            int digB = bIter.previous();
            if (digA > digB) return equality.MORE;
            else if (digA < digB) return equality.LESS;
        }
        return equality.EQUALS;
    }

    public boolean isMoreThan(UnsignedBigInteger other) {
        equality res = CompareAAndB(this, other);
        return res == equality.MORE;
    }

    public boolean isLessThan(UnsignedBigInteger other) {
        equality res = CompareAAndB(this, other);
        return res == equality.LESS;
    }

    public boolean isMoreOrEquals(UnsignedBigInteger other) {
        equality res = CompareAAndB(this, other);
        return res == equality.MORE || res == equality.EQUALS;
    }

    public boolean isLessOrEquals(UnsignedBigInteger other) {
        equality res = CompareAAndB(this, other);
        return res == equality.LESS || res == equality.EQUALS;
    }

    public boolean isEquals(UnsignedBigInteger other) {
        equality res = CompareAAndB(this, other);
        return res == equality.EQUALS;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != UnsignedBigInteger.class) return false;
        return this.isEquals((UnsignedBigInteger) o);
    }
}
