package ru.vldf.unsignedbiginteger;

import java.util.*;

import static java.lang.Integer.max;

public class UnsignedBigInteger implements Comparable<UnsignedBigInteger>{
    private static final int base = 1_000_000_000; // max degree of 10, that int can describe
    private static final int digitsCount = 9; // max degree of 10, that int can describe
    private static final UnsignedBigInteger zeroNumber = new UnsignedBigInteger("0");
    private List<Integer> value;

    /**
     * Creates a UnsignedBigInteger from string
     * @param str String
     */
    public UnsignedBigInteger(String str) {
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

    /**
     * Creates a UnsignedBigInteger from long
     * @param num long
     */
    public UnsignedBigInteger(long num) {
        value = new ArrayList<>();
        while (num > 0) {
            int digit = (int) (num % base);
            num /= base;
            value.add(digit);
        }

        if (value.size() == 0) value.add(0);
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
        for (int i = 0; i < n; i++)
            value.add(0, 0);
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
        addZerosIfNeeded();
    }

    private UnsignedBigInteger getLast(int n) {
        if (n > value.size()) return this;
        return new UnsignedBigInteger(value.subList(max(0, value.size() - n), value.size()));
    }

    public UnsignedBigInteger copy() {
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

    private void addZerosIfNeeded() {
        if (this.value.size() == 0) this.value.add(0);
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
        if (other < 0) throw new ArithmeticException("other number should be unless than 0");
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
        if (this.isLessThan(other)) throw new ArithmeticException("decreasing number should be less than subtracted number");

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
    public UnsignedBigInteger times(long other) {
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
    public UnsignedBigInteger times(UnsignedBigInteger other) {
        UnsignedBigInteger res = new UnsignedBigInteger();
        Iterator<Integer> aIter = this.getIterable();

        int shift = 0;
        while (aIter.hasNext()) {
            UnsignedBigInteger newNum = other.times(aIter.next());
            newNum.shift(shift);
            res = res.add(newNum);
            shift++;
        }
        res.popZeros();
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
        res.addZerosIfNeeded();
        return res;
    }

    /**
     * A.divMod(B) ~ [A / B, A % B]
     * adds a with b and returns result
     * @param other UnsignedBigInt
     * @return UnsignedBigInteger[]; UnsignedBigInteger[0] is a result of division,
     * UnsignedBigInteger[1] is a modulo result
     */
    public DivModResult divMod(UnsignedBigInteger other) {
        if (other.equals(zeroNumber)) throw new ArithmeticException("Zero division");
        UnsignedBigInteger res = new UnsignedBigInteger();
        UnsignedBigInteger dividend = this.copy();
        while (dividend.isMoreOrEquals(other)) {
            UnsignedBigInteger a1 = dividend.getLast(other.getDigitCount());
            UnsignedBigInteger divisor = other.copy();
            if (a1.isLessThan(other)) {
                a1 = dividend.getLast(other.getDigitCount() + 1);
            }

            int r = 1;
            while (divisor.times(r).isLessOrEquals(a1)) {
                r *= 2;
            }
            int lower = r / 2;
            int upper = r;

            while (true) {
                r = lower / 2 + upper / 2;
                if (divisor.times(r).isLessThan(a1)) {
                    lower = r;
                } else if (divisor.times(r).isMoreThan(a1)) {
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
            dividend = leftSubtract(dividend, other.times(r));
        }
        res.addZerosIfNeeded();
        return new DivModResult(res, dividend);
    }

    public UnsignedBigInteger div(UnsignedBigInteger other) {
        return this.divMod(other).getDiv();
    }

    public UnsignedBigInteger mod(UnsignedBigInteger other) {
        return this.divMod(other).getMod();
    }

    /**
     * Comparable method.
     * @param other UnsignedBigInteger
     * @return 0 if equals other, -1 if less than other, 1 if more than other
     */
    @Override
    public int compareTo(UnsignedBigInteger other) {
        if (this.getDigitCount() > other.getDigitCount()) return 1;
        else if (this.getDigitCount() < other.getDigitCount()) return -1;

        ListIterator<Integer> aIter = this.getBackwardIterable();
        ListIterator<Integer> bIter = other.getBackwardIterable();

        while (aIter.hasPrevious()) {
            int digA = aIter.previous();
            int digB = bIter.previous();
            if (digA > digB) return 1;
            else if (digA < digB) return -1;
        }
        return 0;
    }

    /**
     * @param other UnsignedBigInteger
     * @return true if this more than other, else false
     */
    public boolean isMoreThan(UnsignedBigInteger other) {
        return this.compareTo(other) > 0;
    }

    /**
     * @param other UnsignedBigInteger
     * @return true if this less than other, else false
     */
    public boolean isLessThan(UnsignedBigInteger other) {
        return this.compareTo(other) < 0;
    }

    /**
     * @param other UnsignedBigInteger
     * @return true if this more or equals than other, else false
     */
    public boolean isMoreOrEquals(UnsignedBigInteger other) {
        return this.compareTo(other) >= 0;
    }

    /**
     * @param other UnsignedBigInteger
     * @return true if this less or equals than other, else false
     */
    public boolean isLessOrEquals(UnsignedBigInteger other) {
        return this.compareTo(other) <= 0;
    }

    /**
     * @param other UnsignedBigInteger
     * @return true if this equals other, else false
     */
    public boolean isEquals(UnsignedBigInteger other) {
        return this.compareTo(other) == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UnsignedBigInteger)) return false;
        return this.isEquals((UnsignedBigInteger) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
