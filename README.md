# UnsignedBigInteger
UnsignedBigInteger class for SPbSTU
This class implements long ariphmetic with integer non-negative numbers.
# Examples
1. Creating
    ```
    UnsignedBigInteger a = new UnsignedBigInteger("123");
    UnsignedBigInteger b = new UnsignedBigInteger(100L); may throw ArithmeticException when number in bracets less than zero
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
