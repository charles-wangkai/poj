// https://www.hankcs.com/program/algorithm/poj-1286-of-beads-necklace.html

import java.util.Scanner;

public class Main {
  static final int M = 3;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      if (n == -1) {
        break;
      }

      System.out.println(solve(n));
    }

    sc.close();
  }

  static long solve(int n) {
    if (n == 0) {
      return 0;
    }

    return (computeWayNumForRotation(n) + computeWayNumForReflection(n)) / (2 * n);
  }

  static long computeWayNumForReflection(int n) {
    if (n % 2 == 1) {
      return n * pow(M, n / 2 + 1);
    }

    return (n / 2) * pow(M, n / 2 + 1) + (n / 2) * pow(M, n / 2);
  }

  static long computeWayNumForRotation(int n) {
    long result = 0;
    for (int k = 1; k <= n; ++k) {
      result += pow(M, gcd(k, n));
    }

    return result;
  }

  static int gcd(int x, int y) {
    return (y == 0) ? x : gcd(y, x % y);
  }

  static long pow(int base, int exponent) {
    long result = 1;
    for (int i = 0; i < exponent; ++i) {
      result *= base;
    }

    return result;
  }
}
