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

    private int getDigitCount() {
        if (digitCount == -1) digitCount = value.size();
        return digitCount; // even if value.size() more than Integer.MAX_VALUE, return Integer.MAX_VALUE. Methods below will be right
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
