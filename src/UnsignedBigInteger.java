import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Integer.max;

public class UnsignedBigInteger {
    private static final int base = 1_000_000_000; // max degree of 10, that int can describe
    private static final int digitsCount = 9; // max degree of 10, that int can describe
    int digitLen = 0;  // todo: убрать digitLen
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
        digitLen = value.size();
    }

    private UnsignedBigInteger(int digitsCount) {
        value = new ArrayList<>();
        digitLen = digitsCount;
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

    private void addDigit(int digit) {
        value.add(digit);
    }

    public static UnsignedBigInteger add(UnsignedBigInteger a, UnsignedBigInteger b) {
        UnsignedBigInteger res = new UnsignedBigInteger(max(a.digitLen, b.digitLen));

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
}
