import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      String s = sc.next();
      if (s.equals("0")) {
        break;
      }

      System.out.println(solve(s));
    }

    sc.close();
  }

  static String solve(String s) {
    String fraction = s.substring(2, s.length() - 3);

    long numer = -1;
    long denom = Long.MAX_VALUE;
    for (int beginIndex = 0; beginIndex < fraction.length(); ++beginIndex) {
      for (int endIndex = beginIndex; endIndex < fraction.length(); ++endIndex) {
        if (isRepeatingPortion(fraction, beginIndex, endIndex)) {
          int n1 = (beginIndex == 0) ? 0 : Integer.parseInt(fraction.substring(0, beginIndex));
          int d1 = pow10(beginIndex);
          int n2 = Integer.parseInt(fraction.substring(beginIndex, endIndex + 1));
          int d2 = (pow10(endIndex - beginIndex + 1) - 1) * pow10(beginIndex);

          long n = (long) n1 * d2 + (long) n2 * d1;
          long d = (long) d1 * d2;

          long g = gcd(n, d);
          n /= g;
          d /= g;
          if (d < denom) {
            numer = n;
            denom = d;
          }
        }
      }
    }

    return String.format("%d/%d", numer, denom);
  }

  static boolean isRepeatingPortion(String fraction, int beginIndex, int endIndex) {
    for (int i = endIndex + 1; i < fraction.length(); ++i) {
      if (fraction.charAt(i)
          != fraction.charAt((i - beginIndex) % (endIndex - beginIndex + 1) + beginIndex)) {
        return false;
      }
    }

    return true;
  }

  static long gcd(long x, long y) {
    return (y == 0) ? x : gcd(y, x % y);
  }

  static int pow10(int exponent) {
    int result = 1;
    for (int i = 0; i < exponent; ++i) {
      result *= 10;
    }

    return result;
  }
}
