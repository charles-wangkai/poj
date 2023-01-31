import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    int W = sc.nextInt();
    int[] apples = new int[T];
    for (int i = 0; i < apples.length; ++i) {
      apples[i] = sc.nextInt() - 1;
    }

    System.out.println(solve(apples, W));

    sc.close();
  }

  static int solve(int[] apples, int W) {
    int[][] dp = new int[W + 1][2];
    for (int i = 0; i < dp.length; ++i) {
      Arrays.fill(dp[i], -1);
    }
    dp[0][0] = 0;

    for (int apple : apples) {
      int[][] nextDp = new int[W + 1][2];
      for (int i = 0; i < nextDp.length; ++i) {
        Arrays.fill(nextDp[i], -1);
      }

      for (int i = 0; i < dp.length; ++i) {
        for (int j = 0; j < dp[i].length; ++j) {
          nextDp[i][j] = Math.max(nextDp[i][j], dp[i][j] + ((apple == j) ? 1 : 0));

          if (i + 1 != nextDp.length) {
            nextDp[i + 1][1 - j] =
                Math.max(nextDp[i + 1][1 - j], dp[i][j] + ((apple == 1 - j) ? 1 : 0));
          }
        }
      }

      dp = nextDp;
    }

    int result = 0;
    for (int i = 0; i < dp.length; ++i) {
      for (int j = 0; j < dp[i].length; ++j) {
        result = Math.max(result, dp[i][j]);
      }
    }

    return result;
  }
}
