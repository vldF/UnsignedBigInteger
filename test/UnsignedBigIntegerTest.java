import org.junit.jupiter.api.Test;
import org.junit.Assert;

import java.util.Random;

class UnsignedBigIntegerTest {
    private final Random rand = new Random();

    private String numberA = "182739812739817239812734582904989219318239182390";
    private String numberB = "283127318273875823814818238284284828";

    UnsignedBigInteger a = new UnsignedBigInteger(numberA);
    UnsignedBigInteger b = new UnsignedBigInteger(numberB);

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
        String sum = "182739812740100367131008458728804037556523467218";

        UnsignedBigInteger a = new UnsignedBigInteger(numberA);
        UnsignedBigInteger b = new UnsignedBigInteger(numberB);
        UnsignedBigInteger bigRes = UnsignedBigInteger.add(a, b);
        Assert.assertEquals(sum, bigRes.toString());
    }

    @Test
    void compareTest() {
        Assert.assertTrue(UnsignedBigInteger.isAMoreThanB(a, b));
        Assert.assertTrue(UnsignedBigInteger.isALessThanB(b, a));
        Assert.assertTrue(UnsignedBigInteger.isAEqualsB(a, a));
        Assert.assertTrue(UnsignedBigInteger.isALessOrEqualsB(a, a));
        Assert.assertTrue(UnsignedBigInteger.isAMoreOrEqualsB(b, b));

        Assert.assertFalse(UnsignedBigInteger.isAMoreThanB(b, a));
    }

    @Test
    void subtractTest() {
        String subtract = "182739812739534112494460707081174401079954897562";
        Assert.assertEquals(subtract, UnsignedBigInteger.subtract(a, b).toString());
    }

}