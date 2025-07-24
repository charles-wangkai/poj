// https://www.hankcs.com/program/algorithm/poj-2315-football-game.html

import java.util.Scanner;

public class Main {
  static final int BIT_NUM = 27;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (sc.hasNextInt()) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      int L = sc.nextInt();
      int R = sc.nextInt();
      int[] S = new int[N];
      for (int i = 0; i < S.length; ++i) {
        S[i] = sc.nextInt();
      }

      System.out.println(solve(N, M, L, R, S));
    }

    sc.close();
  }

  static String solve(int N, int M, int L, int R, int[] S) {
    int K = (int) Math.floor((L / (2 * Math.PI * R)));

    int[] bitSums = new int[BIT_NUM];
    for (int Si : S) {
      int g = (int) Math.ceil(Si / (2 * Math.PI * R)) % (K + 1);

      for (int i = 0; i < bitSums.length; ++i) {
        bitSums[i] += (g >> i) & 1;
      }
    }

    for (int bitSum : bitSums) {
      if (bitSum % (M + 1) != 0) {
        return "Alice";
      }
    }

    return "Bob";
  }
}
