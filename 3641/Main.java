import java.math.BigInteger;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int p = sc.nextInt();
      int a = sc.nextInt();
      if (p == 0) {
        break;
      }

      System.out.println(solve(p, a) ? "yes" : "no");
    }

    sc.close();
  }

  static boolean solve(int p, int a) {
    return !isPrime(p)
        && BigInteger.valueOf(a).modPow(BigInteger.valueOf(p), BigInteger.valueOf(p)).intValue()
            == a;
  }

  static boolean isPrime(int x) {
    for (int i = 2; i * i <= x; ++i) {
      if (x % i == 0) {
        return false;
      }
    }

    return true;
  }
}
