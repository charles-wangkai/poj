// TLE on POJ. AC on Bailian: http://bailian.openjudge.cn/practice/1180

// https://www.hankcs.com/program/algorithm/poj-1180-batch-scheduling.html

import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int S = sc.nextInt();
    int[] T = new int[N];
    int[] F = new int[N];
    for (int i = 0; i < N; ++i) {
      T[i] = sc.nextInt();
      F[i] = sc.nextInt();
    }

    System.out.println(solve(T, F, S));

    sc.close();
  }

  static int solve(int[] T, int[] F, int S) {
    int N = T.length;

    int[] fSuffixSums = new int[F.length];
    for (int i = fSuffixSums.length - 1; i >= 0; --i) {
      fSuffixSums[i] = ((i == fSuffixSums.length - 1) ? 0 : fSuffixSums[i + 1]) + F[i];
    }

    int[] dp = new int[N];
    Arrays.fill(dp, Integer.MAX_VALUE);
    for (int j = N - 1; j >= 0; --j) {
      int t = S;
      for (int i = 1; j + i <= N; ++i) {
        t += T[j + i - 1];
        dp[j] = Math.min(dp[j], t * fSuffixSums[j] + (((j + i) == N) ? 0 : dp[j + i]));
      }
    }

    return dp[0];
  }
}
