// https://www.hankcs.com/program/algorithm/poj-2914-minimum-cut.html
// https://en.wikipedia.org/wiki/Stoer%E2%80%93Wagner_algorithm

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (sc.hasNextInt()) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      int[] A = new int[M];
      int[] B = new int[M];
      int[] C = new int[M];
      for (int i = 0; i < M; ++i) {
        A[i] = sc.nextInt();
        B[i] = sc.nextInt();
        C[i] = sc.nextInt();
      }

      System.out.println(solve(N, A, B, C));
    }

    sc.close();
  }

  static int solve(int N, int[] A, int[] B, int[] C) {
    int[][] G = new int[N][N];
    for (int i = 0; i < A.length; ++i) {
      G[A[i]][B[i]] += C[i];
      G[B[i]][A[i]] += C[i];
    }

    return stoerWagner(G);
  }

  static int stoerWagner(int[][] G) {
    int N = G.length;

    int[] v = new int[N];
    for (int i = 0; i < v.length; ++i) {
      v[i] = i;
    }

    int minCut = Integer.MAX_VALUE;
    int n = N;
    while (n > 1) {
      int pre = 0;
      boolean[] visited = new boolean[N];
      int[] w = new int[N];
      for (int i = 1; i < n; ++i) {
        int k = -1;
        for (int j = 1; j < n; ++j) {
          if (!visited[v[j]]) {
            w[v[j]] += G[v[pre]][v[j]];
            if (k == -1 || w[v[j]] > w[v[k]]) {
              k = j;
            }
          }
        }

        visited[v[k]] = true;

        if (i == n - 1) {
          int s = v[pre];
          int t = v[k];
          minCut = Math.min(minCut, w[t]);
          for (int j = 0; j < n; ++j) {
            G[s][v[j]] += G[v[j]][t];
            G[v[j]][s] += G[v[j]][t];
          }

          --n;
          v[k] = v[n];
        }

        pre = k;
      }
    }

    return minCut;
  }
}