import java.util.Scanner;

public class Main {
  static long LIMIT = 10000000000L;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int N = sc.nextInt();
      long M = sc.nextLong();

      System.out.println(solve(N, M));
    }

    sc.close();
  }

  static long solve(int N, long M) {
    long result = Long.MIN_VALUE;
    long lower = -LIMIT;
    long upper = LIMIT;
    while (lower <= upper) {
      long middle = (lower + upper) / 2;
      if (computeLessEqualNum(N, M, middle) >= M) {
        result = middle;
        upper = middle - 1;
      } else {
        lower = middle + 1;
      }
    }

    return result;
  }

  static long computeLessEqualNum(int N, long M, long target) {
    long result = 0;
    for (int j = 1; j <= N; ++j) {
      int resultI = 0;
      int lowerI = 1;
      int upperI = N;
      while (lowerI <= upperI) {
        int middleI = (lowerI + upperI) / 2;
        if (f(middleI, j) <= target) {
          resultI = middleI;
          lowerI = middleI + 1;
        } else {
          upperI = middleI - 1;
        }
      }

      result += resultI;
    }

    return result;
  }

  static long f(int i, int j) {
    return (long) i * i + 100000L * i + (long) j * j - 100000L * j + (long) i * j;
  }
}