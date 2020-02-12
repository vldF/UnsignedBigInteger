import org.junit.jupiter.api.Test;
import org.junit.Assert;

import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void testNumberFromLongInit() {
        for (int n = 0; n < 10; n++) {
            UnsignedBigInteger num = new UnsignedBigInteger(n * 1000000L);
            Assert.assertEquals(Long.toString((n * 1000000L)), num.toString());
        }
    }

    @Test
    void addTest() {
        String sum = "182739812740100367131008458728804037556523467218";

        UnsignedBigInteger a = new UnsignedBigInteger(numberA);
        UnsignedBigInteger b = new UnsignedBigInteger(numberB);
        UnsignedBigInteger bigRes = a.add(b);
        Assert.assertEquals(sum, bigRes.toString());
    }

    @Test
    void compareTest() {
        Assert.assertTrue(a.isMoreThan(b));
        Assert.assertTrue(b.isLessThan(a));
        Assert.assertTrue(a.isEquals(a));
        Assert.assertTrue(a.isLessOrEquals(a));
        Assert.assertTrue(b.isLessOrEquals(b));
        Assert.assertFalse(b.isMoreThan(a));
        UnsignedBigInteger oneMore = a.copy();
        Assert.assertEquals(a, oneMore);
    }

    @Test
    void subtractTest() {
        String subtract = "182739812739534112494460707081174401079954897562";
        Assert.assertEquals(subtract, a.subtract(b).toString());
    }

    @Test
    void addAndSubtractTest() {
        for (int i = 0; i < 100; i++) {
            UnsignedBigInteger first = new UnsignedBigInteger(generateBigIntString(1000));
            UnsignedBigInteger second = new UnsignedBigInteger(generateBigIntString(1000));

            if (first.isLessThan(second)){
                assertThrows(ArithmeticException.class, () -> first.subtract(second));
            } else {
                UnsignedBigInteger diff = first.subtract(second);
                Assert.assertEquals(second.add(diff), first);
            }
        }
    }

    @Test
    void mulByIntTest() {
        Assert.assertEquals("182739812922557052552551822717723802223228401708239182390", (a.mul(1000000001)).toString());
    }

    @Test
    void mulTest() {
        String mul = "51738633122894703676225428408097288277879497015410025212588169383320332048601778920";
        Assert.assertEquals(mul, a.mul(b).toString());
    }

    @Test
    void addIntTest() {
        UnsignedBigInteger first = new UnsignedBigInteger("900000000000000000");
        Assert.assertEquals("900000002000000000", first.add(2000000000).toString());
    }

    @Test
    void divByIntTest() {
        UnsignedBigInteger first = new UnsignedBigInteger("17027176145819335214232514");
        Assert.assertEquals("123123182828828182", first.div(138293827).toString());
    }

    @Test
    void divTest(){
        UnsignedBigInteger first = new UnsignedBigInteger("8110655784862507773201087515783930125888643533703330618528244");
        UnsignedBigInteger second = new UnsignedBigInteger("81989182931802938109238109238");
        Assert.assertEquals("98923485938490189202131902839438", first.div(second).toString());
    }

    @Test
    void mulAndDivTest() throws Exception {
        for (int i = 0; i < 100; i++){
            UnsignedBigInteger first = new UnsignedBigInteger(generateBigIntString(100));
            UnsignedBigInteger second = new UnsignedBigInteger(generateBigIntString(90));
            UnsignedBigInteger mul = first.mul(second);
            if (!second.toString().equals(mul.div(first).toString())) {
                System.out.println(first);
                System.out.println(second);
                System.out.println(mul);
                System.out.println(mul.div(first));
                throw new Exception();
            }
        }
    }

    @Test
    void checkZeroOnDiv() {
        UnsignedBigInteger first = new UnsignedBigInteger("100000000000");
        UnsignedBigInteger second = new UnsignedBigInteger("10000000001000000000");
        UnsignedBigInteger div = first.div(second);
        Assert.assertEquals("0", div.toString());

        assertThrows(ArithmeticException.class, () -> new UnsignedBigInteger("100").div(new UnsignedBigInteger("0")));
    }

    @Test
    void divModTest() {
        for (int i = 0; i < 100; i++) {
            UnsignedBigInteger num1 = new UnsignedBigInteger(generateBigIntString(80));
            UnsignedBigInteger num2 = new UnsignedBigInteger(generateBigIntString(100));

            UnsignedBigInteger[] divMod = num1.divMod(num2);
            Assert.assertEquals(num1, divMod[0].mul(num2).add(divMod[1]));

        }
    }
}