import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        final BigInt firstBigInt = new BigInt("3539419308493578215239098606889");
        final BigInt secondBigInt = new BigInt("-133422733544239134919");
        final BigInt res = firstBigInt.divide(secondBigInt);
        System.out.println(res);
        //Expected: res.toString() == "-26527857843"
        //Got: res.toString() == "-11111111111111111111111111111111111111111111111111111111111111"
    }
}