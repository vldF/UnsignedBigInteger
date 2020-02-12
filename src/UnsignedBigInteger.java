import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static java.lang.Integer.max;

public class UnsignedBigInteger {
    private static final int base = 1_000_000_000; // max degree of 10, that int can describe
    private static final int digitsCount = 9; // max degree of 10, that int can describe
    private int digitCount = -1;
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
        return digitCount = value.size(); // even if value.size() more than Integer.MAX_VALUE, return Integer.MAX_VALUE. Methods below will be right
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

    private int getLastInt() {
        return value.get(0);
    }

    private UnsignedBigInteger copy() {
        UnsignedBigInteger res = new UnsignedBigInteger();
        res.value.addAll(this.value);
        return res;
    }

    public static UnsignedBigInteger leftSubtract(UnsignedBigInteger a, UnsignedBigInteger b) {
        UnsignedBigInteger bShifted = b.copy();
        bShifted.shift(a.getDigitCount() - b.getDigitCount());
        UnsignedBigInteger res = UnsignedBigInteger.subtract(a, bShifted);
        res.popZeros();
        return res;
    }

    public static UnsignedBigInteger add(UnsignedBigInteger a, UnsignedBigInteger b) {
        UnsignedBigInteger res = new UnsignedBigInteger();

        Iterator<Integer> aIter = a.getIterable();
        Iterator<Integer> bIter = b.getIterable();

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

    public static UnsignedBigInteger addInt(UnsignedBigInteger a, int b) {
        UnsignedBigInteger res = new UnsignedBigInteger();
        Iterator<Integer> aIter = a.getIterable();

        int transfer = b;
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

    public static UnsignedBigInteger subtract(UnsignedBigInteger a, UnsignedBigInteger b) {
        UnsignedBigInteger res = new UnsignedBigInteger();

        Iterator<Integer> aIter = a.getIterable();
        Iterator<Integer> bIter = b.getIterable();

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

    public static UnsignedBigInteger mulByInt(UnsignedBigInteger a, long b) {
        UnsignedBigInteger res = new UnsignedBigInteger();
        Iterator<Integer> aIter = a.getIterable();

        int transfer = 0;
        while (aIter.hasNext()) {
            long newVal = aIter.next() * b + transfer;
            transfer = (int) (newVal / base);
            res.addDigit((int) (newVal % base));
        }

        if (transfer != 0) {
            res.addDigit(transfer);
        }

        return res;
    }

    public static UnsignedBigInteger mul(UnsignedBigInteger a, UnsignedBigInteger b) {
        UnsignedBigInteger res = new UnsignedBigInteger();
        Iterator<Integer> aIter = a.getIterable();

        int shift = 0;
        while (aIter.hasNext()) {
            UnsignedBigInteger newNum = mulByInt(b, aIter.next());
            newNum.shift(shift);
            res = UnsignedBigInteger.add(res, newNum);
            shift++;
        }
        return res;
    }

    public static UnsignedBigInteger divByInt(UnsignedBigInteger a, int b) {
        UnsignedBigInteger res = new UnsignedBigInteger();
        res.value = a.value;
        int carry = 0;
        for (int i = a.value.size() - 1; i >= 0; i--) {
            long cur = a.value.get(i) + (long) carry * base;
            a.value.set(i, (int) (cur / b));
            carry = (int) (cur % b);
        }
        res.popZeros();
        return res;
    }

    public static UnsignedBigInteger div(UnsignedBigInteger a, UnsignedBigInteger b) {
        UnsignedBigInteger res = new UnsignedBigInteger();
        while (isAMoreOrEqualsB(a, b)) {
            UnsignedBigInteger a1 = a.getLast(b.getDigitCount());
            UnsignedBigInteger divisor = b.copy();
            if (isALessThanB(a1, b)) {
                a1 = a.getLast(b.getDigitCount() + 1);
            }

            int r = 1;
            while (isALessOrEqualsB(mulByInt(divisor, r), a1)) {
                r *= 2;
            }
            int lower = r / 2;
            int upper = r;

            while (true) {
                r = lower / 2 + upper / 2;
                if (isALessThanB(mulByInt(divisor, r), a1)) {
                    lower = r;
                } else if (isAMoreThanB(mulByInt(divisor, r), a1)) {
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
            a = leftSubtract(a, mulByInt(b, r));
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

    public static boolean isAMoreThanB(UnsignedBigInteger a, UnsignedBigInteger b) {
        equality res = CompareAAndB(a, b);
        return res == equality.MORE;
    }

    public static boolean isALessThanB(UnsignedBigInteger a, UnsignedBigInteger b) {
        equality res = CompareAAndB(a, b);
        return res == equality.LESS;
    }

    public static boolean isAMoreOrEqualsB(UnsignedBigInteger a, UnsignedBigInteger b) {
        equality res = CompareAAndB(a, b);
        return res == equality.MORE || res == equality.EQUALS;
    }

    public static boolean isALessOrEqualsB(UnsignedBigInteger a, UnsignedBigInteger b) {
        equality res = CompareAAndB(a, b);
        return res == equality.LESS || res == equality.EQUALS;
    }

    public static boolean isAEqualsB(UnsignedBigInteger a, UnsignedBigInteger b) {
        equality res = CompareAAndB(a, b);
        return res == equality.EQUALS;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != UnsignedBigInteger.class) return false;
        return isAEqualsB(this, (UnsignedBigInteger) o);
    }
}
