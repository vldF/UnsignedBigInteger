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

    @Test
    void addAndSubtractTest() {
        for (int i = 0; i < 10000; i++) {
            UnsignedBigInteger first = new UnsignedBigInteger(generateBigIntString(1000));
            UnsignedBigInteger second = new UnsignedBigInteger(generateBigIntString(995));

            UnsignedBigInteger diff = UnsignedBigInteger.subtract(first, second);
            Assert.assertEquals(UnsignedBigInteger.add(second, diff), first);
        }
    }

    @Test
    void mulByIntTest() {
        Assert.assertEquals("182739812922557052552551822717723802223228401708239182390", UnsignedBigInteger.mulByInt(a, 1000000001).toString());
    }

    @Test
    void mulTest() {
        String mul = "51738633122894703676225428408097288277879497015410025212588169383320332048601778920";
        Assert.assertEquals(mul, UnsignedBigInteger.mul(a, b).toString());
    }

    @Test
    void addIntTest() {
        UnsignedBigInteger first = new UnsignedBigInteger("900000000000000000");
        Assert.assertEquals("900000002000000000", UnsignedBigInteger.addInt(first, 2000000000).toString());
    }

    @Test
    void divByIntTest() {
        UnsignedBigInteger first = new UnsignedBigInteger("17027176145819335214232514");
        Assert.assertEquals("123123182828828182", UnsignedBigInteger.divByInt(first, 138293827).toString());
    }

    @Test
    void divTest(){
        UnsignedBigInteger first = new UnsignedBigInteger("8110655784862507773201087515783930125888643533703330618528244");
        UnsignedBigInteger second = new UnsignedBigInteger("81989182931802938109238109238");
        Assert.assertEquals("98923485938490189202131902839438", UnsignedBigInteger.div(first, second).toString());
    }

    @Test
    void mulAndDivTest() {
        for (int i = 0; i < 10; i++){
            UnsignedBigInteger first = new UnsignedBigInteger(generateBigIntString(100));
            UnsignedBigInteger second = new UnsignedBigInteger(generateBigIntString(100));
            UnsignedBigInteger mul = UnsignedBigInteger.mul(first, second);
            Assert.assertEquals(second.toString(), UnsignedBigInteger.div(mul, first).toString());
        }
    }

    @Test
    void leftSubtractTest() {
        UnsignedBigInteger first = new UnsignedBigInteger("300350140004");
        UnsignedBigInteger second = new UnsignedBigInteger("100000001");
        System.out.println(UnsignedBigInteger.leftSubtract(first, second));
    }
}