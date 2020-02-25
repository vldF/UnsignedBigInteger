# ru.vldf.unsignedbiginteger.UnsignedBigInteger
ru.vldf.unsignedbiginteger.UnsignedBigInteger class for SPbSTU
This class implements long ariphmetic with integer non-negative numbers.
# Examples
1. Creating
    ```
    ru.vldf.unsignedbiginteger.UnsignedBigInteger a = new ru.vldf.unsignedbiginteger.UnsignedBigInteger("123");
    ru.vldf.unsignedbiginteger.UnsignedBigInteger b = new ru.vldf.unsignedbiginteger.UnsignedBigInteger(100L); may throw ArithmeticException when number in bracets less than zero
    ```
2. Operations
    ```
    a.div(b); // division, may throw ArithmeticException when b == 0
    a.mul(b); // multiplication
    a.add(b); // add
    a.subtrack(b); // subtraction (minus), may throw ArithmeticException when b > a
    ```
3. Comparation
    ```
    a.isMoreThan(b); // a > b
    a.isLessThan(b); // a < b
    a.isEquals(b); // a == b
    a.isLessOrEquals(b); // a <= b
    a.isMoreOrEquals(b); // a >= b
    ```
