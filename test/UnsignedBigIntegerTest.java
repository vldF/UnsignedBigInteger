import org.junit.jupiter.api.Test;
import org.junit.Assert;

import java.util.Random;

class UnsignedBigIntegerTest {
    private final Random rand = new Random();
    private String generateBigIntString(int countDigits){
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < countDigits; i++) {
            int digit = rand.nextInt(9);
            if (number.length() == 0 && digit == 0) continue;
            number.append(digit);
        }
        return number.toString();
    }

    @Test
    void testNumberInit() {
        for (int n = 0; n < 10; n++) {
            String numStr = generateBigIntString(n * 100);
            UnsignedBigInteger num = new UnsignedBigInteger(numStr);
            Assert.assertEquals(numStr, num.toString());
        }
    }

    @Test
    void addTest() {
        String numberA = "182739812739817239812734582904989219318239182390";
        String numberB = "283127318273875823814818238284284828";
        String res = "182739812740100367131008458728804037556523467218";

        UnsignedBigInteger a = new UnsignedBigInteger(numberA);
        UnsignedBigInteger b = new UnsignedBigInteger(numberB);
        UnsignedBigInteger bigRes = UnsignedBigInteger.add(a, b);
        Assert.assertEquals(res, bigRes.toString());
    }

}