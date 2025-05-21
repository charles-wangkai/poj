// https://www.hankcs.com/program/algorithm/poj-1150-the-last-non-zero-digit.html

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
  static final int MODULUS = 10;
  static final Map<Integer, int[]> FACTOR_TO_LAST_DIGITS = new HashMap<Integer, int[]>();

  static {
    FACTOR_TO_LAST_DIGITS.put(2, new int[] {6, 2, 4, 8});
    FACTOR_TO_LAST_DIGITS.put(3, new int[] {1, 3, 9, 7});
    FACTOR_TO_LAST_DIGITS.put(7, new int[] {1, 7, 9, 3});
    FACTOR_TO_LAST_DIGITS.put(9, new int[] {1, 9});
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (sc.hasNextInt()) {
      int N = sc.nextInt();
      int M = sc.nextInt();

      System.out.println(solve(N, M));
    }

    sc.close();
  }

  static int solve(int N, int M) {
    int exponent2 = computeExponent(2, N) - computeExponent(2, N - M);
    int exponent5 = computeExponent(5, N) - computeExponent(5, N - M);
    if (exponent5 > exponent2) {
      return 5;
    }

    int result = 1;
    for (int factor : new int[] {3, 7, 9}) {
      int[] lastDigits = FACTOR_TO_LAST_DIGITS.get(factor);

      result =
          multiplyMod(
              result,
              lastDigits[
                  (computeLastDigitNum(factor, N) - computeLastDigitNum(factor, N - M))
                      % lastDigits.length]);
    }

    if (exponent2 != exponent5) {
      int[] lastDigits = FACTOR_TO_LAST_DIGITS.get(2);

      result = multiplyMod(result, lastDigits[(exponent2 - exponent5) % lastDigits.length]);
    }

    return result;
  }

  static int computeLastDigitNum(int factor, int limit) {
    if (limit == 0) {
      return 0;
    }

    return computeLastDigitNum(factor, limit / 2) + computeLastOddDigitNum(factor, limit);
  }

  static int computeLastOddDigitNum(int factor, int limit) {
    if (limit == 0) {
      return 0;
    }

    return limit / 10
        + ((limit % 10 >= factor) ? 1 : 0)
        + computeLastOddDigitNum(factor, limit / 5);
  }

  static int multiplyMod(int x, int y) {
    return x * y % MODULUS;
  }

  static int computeExponent(int factor, int limit) {
    if (limit == 0) {
      return 0;
    }

    return limit / factor + computeExponent(factor, limit / factor);
  }
}