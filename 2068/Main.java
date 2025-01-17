// https://www.hankcs.com/program/algorithm/poj-2068-nim.html

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      if (n == 0) {
        break;
      }

      int S = sc.nextInt();
      int[] M = new int[2 * n];
      for (int i = 0; i < M.length; ++i) {
        M[i] = sc.nextInt();
      }

      System.out.println(solve(n, S, M) ? 1 : 0);
    }

    sc.close();
  }

  static boolean solve(int n, int S, int[] M) {
    boolean[][][] wins = new boolean[2][n][S + 1];
    for (int k = 0; k <= S; ++k) {
      for (int i = 0; i < 2; ++i) {
        for (int j = 0; j < n; ++j) {
          if (k == 0) {
            wins[i][j][k] = true;
          } else {
            for (int p = 1; p <= M[j * 2 + i] && p <= k; ++p) {
              wins[i][j][k] |= !wins[1 - i][(i == 0) ? j : ((j + 1) % n)][k - p];
            }
          }
        }
      }
    }

    return wins[0][0][S];
  }
}
