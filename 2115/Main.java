import java.math.BigInteger;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      long A = sc.nextLong();
      long B = sc.nextLong();
      long C = sc.nextLong();
      int k = sc.nextInt();
      if (A == 0 && B == 0 && C == 0 && k == 0) {
        break;
      }

      System.out.println(solve(A, B, C, k));
    }

    sc.close();
  }

  static String solve(long A, long B, long C, int k) {
    if (A == B) {
      return "0";
    }

    long modulus = 1L << k;
    long factor = C;
    long product = (B - A + modulus) % modulus;

    long g = gcd(gcd(factor, product), modulus);
    factor /= g;
    product /= g;
    modulus /= g;
    if (gcd(factor, modulus) != 1) {
      return "FOREVER";
    }

    return String.valueOf(
        BigInteger.valueOf(factor)
            .modInverse(BigInteger.valueOf(modulus))
            .multiply(BigInteger.valueOf(product))
            .mod(BigInteger.valueOf(modulus))
            .longValue());
  }

  static long gcd(long x, long y) {
    return (y == 0) ? x : gcd(y, x % y);
  }
}