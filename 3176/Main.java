import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int[][] triangle = new int[N][];
    for (int r = 0; r < triangle.length; ++r) {
      triangle[r] = new int[r + 1];
      for (int c = 0; c < triangle[r].length; ++c) {
        triangle[r][c] = sc.nextInt();
      }
    }

    System.out.println(solve(triangle));

    sc.close();
  }

  static int solve(int[][] triangle) {
    int[] dp = {triangle[0][0]};
    for (int r = 1; r < triangle.length; ++r) {
      int[] nextDp = new int[triangle[r].length];
      for (int c = 0; c < nextDp.length; ++c) {
        nextDp[c] =
            Math.max((c == 0) ? 0 : dp[c - 1], (c == nextDp.length - 1) ? 0 : dp[c])
                + triangle[r][c];
      }

      dp = nextDp;
    }

    int result = 0;
    for (int x : dp) {
      result = Math.max(result, x);
    }

    return result;
  }
}
