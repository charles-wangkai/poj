import java.util.Arrays;
import java.util.Scanner;

public class Main {
  static final int LIMIT = 100000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int[] S = new int[N];
    int[] F = new int[N];
    for (int i = 0; i < N; ++i) {
      S[i] = sc.nextInt();
      F[i] = sc.nextInt();
    }

    System.out.println(solve(S, F));

    sc.close();
  }

  static int solve(int[] S, int[] F) {
    int[] dp = new int[2 * LIMIT + 1];
    Arrays.fill(dp, Integer.MIN_VALUE);
    dp[LIMIT] = 0;

    int[] nextDp = new int[dp.length];

    for (int i = 0; i < S.length; ++i) {
      for (int j = 0; j < nextDp.length; ++j) {
        nextDp[j] = dp[j];
      }

      for (int j = 0; j < dp.length; ++j) {
        int nextJ = j + S[i];
        if (nextJ >= 0 && nextJ < nextDp.length && dp[j] != Integer.MIN_VALUE) {
          nextDp[nextJ] = Math.max(nextDp[nextJ], dp[j] + F[i]);
        }
      }

      int[] temp = dp;
      dp = nextDp;
      nextDp = temp;
    }

    int result = Integer.MIN_VALUE;
    for (int i = LIMIT; i < dp.length; ++i) {
      if (dp[i] >= 0) {
        result = Math.max(result, i - LIMIT + dp[i]);
      }
    }

    return result;
  }
}
