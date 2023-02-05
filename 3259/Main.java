// https://en.wikipedia.org/wiki/Bellman%E2%80%93Ford_algorithm

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int F = sc.nextInt();
    for (int tc = 0; tc < F; ++tc) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      int W = sc.nextInt();
      int[] S1 = new int[M];
      int[] E1 = new int[M];
      int[] T1 = new int[M];
      for (int i = 0; i < M; ++i) {
        S1[i] = sc.nextInt() - 1;
        E1[i] = sc.nextInt() - 1;
        T1[i] = sc.nextInt();
      }
      int[] S2 = new int[W];
      int[] E2 = new int[W];
      int[] T2 = new int[W];
      for (int i = 0; i < W; ++i) {
        S2[i] = sc.nextInt() - 1;
        E2[i] = sc.nextInt() - 1;
        T2[i] = sc.nextInt();
      }

      System.out.println(solve(N, S1, E1, T1, S2, E2, T2) ? "YES" : "NO");
    }

    sc.close();
  }

  static boolean solve(int N, int[] S1, int[] E1, int[] T1, int[] S2, int[] E2, int[] T2) {
    int[] distances = new int[N];
    int count = 0;
    while (count != N) {
      boolean updated = false;
      for (int i = 0; i < S1.length; ++i) {
        if (distances[S1[i]] + T1[i] < distances[E1[i]]) {
          distances[E1[i]] = distances[S1[i]] + T1[i];
          updated = true;
        }
        if (distances[E1[i]] + T1[i] < distances[S1[i]]) {
          distances[S1[i]] = distances[E1[i]] + T1[i];
          updated = true;
        }
      }
      for (int i = 0; i < S2.length; ++i) {
        if (distances[S2[i]] - T2[i] < distances[E2[i]]) {
          distances[E2[i]] = distances[S2[i]] - T2[i];
          updated = true;
        }
      }

      if (!updated) {
        break;
      }

      ++count;
    }

    return count == N;
  }
}
