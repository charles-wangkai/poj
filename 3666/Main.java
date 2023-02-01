import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int[] A = new int[N];
    for (int i = 0; i < A.length; ++i) {
      A[i] = sc.nextInt();
    }

    System.out.println(solve(A));

    sc.close();
  }

  static long solve(int[] A) {
    return Math.min(computeMinCost(A), computeMinCost(negate(A)));
  }

  static int[] negate(int[] A) {
    int[] result = new int[A.length];
    for (int i = 0; i < result.length; ++i) {
      result[i] = -A[i];
    }

    return result;
  }

  static long computeMinCost(int[] values) {
    int[] sorted = values.clone();
    Arrays.sort(sorted);

    long[] dp = new long[values.length];
    for (int value : values) {
      long[] nextDp = new long[dp.length];
      long prevMin = Long.MAX_VALUE;
      for (int i = 0; i < nextDp.length; ++i) {
        prevMin = Math.min(prevMin, dp[i]);
        nextDp[i] = prevMin + Math.abs(value - sorted[i]);
      }

      dp = nextDp;
    }

    long result = Long.MAX_VALUE;
    for (long x : dp) {
      result = Math.min(result, x);
    }

    return result;
  }
}
