// https://www.hankcs.com/program/algorithm/poj-3688-cheat-in-the-game.html

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      if (N == 0 && M == 0) {
        break;
      }

      int[] A = new int[N];
      for (int i = 0; i < A.length; ++i) {
        A[i] = sc.nextInt();
      }

      System.out.println(solve(A, M));
    }

    sc.close();
  }

  static int solve(int[] A, int M) {
    boolean[][] dp = new boolean[M + 1][2];
    dp[0][0] = true;
    for (int a : A) {
      for (int i = dp.length - 1; i >= a; --i) {
        if (dp[i - a][0]) {
          dp[i][1] = true;
        }
        if (dp[i - a][1]) {
          dp[i][0] = true;
        }
      }
    }

    int result = 0;
    for (int i = 0; i < dp.length; ++i) {
      if (!dp[i][0] && dp[i][1]) {
        ++result;
      }
    }

    return result;
  }
}