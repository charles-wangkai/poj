// https://www.hankcs.com/program/algorithm/poj-3260-the-fewest-coins.html

import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int T = sc.nextInt();
    int[] V = new int[N];
    for (int i = 0; i < V.length; ++i) {
      V[i] = sc.nextInt();
    }
    int[] C = new int[N];
    for (int i = 0; i < C.length; ++i) {
      C[i] = sc.nextInt();
    }

    System.out.println(solve(V, C, T));

    sc.close();
  }

  static int solve(int[] V, int[] C, int T) {
    int maxV = Integer.MIN_VALUE;
    for (int v : V) {
      maxV = Math.max(maxV, v);
    }

    int W = T + maxV * maxV;

    int[] payDp = buildPayDp(V, C, W);
    int[] changeDp = buildChangeDp(V, W);

    int result = Integer.MAX_VALUE;
    for (int i = 0; i <= maxV * maxV; ++i) {
      if (payDp[T + i] != Integer.MAX_VALUE && changeDp[i] != Integer.MAX_VALUE) {
        result = Math.min(result, payDp[T + i] + changeDp[i]);
      }
    }

    return (result == Integer.MAX_VALUE) ? -1 : result;
  }

  static int[] buildPayDp(int[] V, int[] C, int W) {
    int[] payDp = new int[W + 1];
    Arrays.fill(payDp, Integer.MAX_VALUE);
    payDp[0] = 0;

    for (int i = 0; i < V.length; ++i) {
      int rest = C[i];
      for (int k = 1; rest != 0; k *= 2) {
        int current = Math.min(k, rest);
        for (int j = W; j >= V[i] * current; --j) {
          if (payDp[j - V[i] * current] != Integer.MAX_VALUE) {
            payDp[j] = Math.min(payDp[j], payDp[j - V[i] * current] + current);
          }
        }

        rest -= current;
      }
    }

    return payDp;
  }

  static int[] buildChangeDp(int[] V, int W) {
    int[] changeDp = new int[W + 1];
    Arrays.fill(changeDp, Integer.MAX_VALUE);
    changeDp[0] = 0;

    for (int v : V) {
      for (int j = v; j <= W; ++j) {
        if (changeDp[j - v] != Integer.MAX_VALUE) {
          changeDp[j] = Math.min(changeDp[j], changeDp[j - v] + 1);
        }
      }
    }

    return changeDp;
  }
}