// https://blog.csdn.net/u014688145/article/details/77776006
// https://blog.csdn.net/a601025382s/article/details/8866768

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int c = sc.nextInt();
      int s = sc.nextInt();
      if (c == 0 && s == 0) {
        break;
      }

      System.out.println(solve(c, s));
    }

    sc.close();
  }

  static int solve(int c, int s) {
    int result = 0;
    for (int k = 0; k < s; ++k) {
      result += pow(c, gcd(k, s));
    }
    if (s % 2 == 1) {
      result += s * pow(c, 1 + (s - 1) / 2);
    } else {
      result += (s / 2) * pow(c, s / 2) + (s / 2) * pow(c, 2 + (s - 2) / 2);
    }
    result /= 2 * s;

    return result;
  }

  static int gcd(int x, int y) {
    return (y == 0) ? x : gcd(y, x % y);
  }

  static int pow(int base, int exponent) {
    int result = 1;
    for (int i = 0; i < exponent; ++i) {
      result *= base;
    }

    return result;
  }
}