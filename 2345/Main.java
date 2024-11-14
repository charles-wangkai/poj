// https://blog.csdn.net/yuege38/article/details/78944988
// https://oi-wiki.org/math/numerical/gauss/

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    @SuppressWarnings("unchecked")
    List<Integer>[] valves = new List[N];
    for (int i = 0; i < valves.length; ++i) {
      valves[i] = new ArrayList<Integer>();
      while (true) {
        int valve = sc.nextInt();
        if (valve == -1) {
          break;
        }

        valves[i].add(valve);
      }
    }

    System.out.println(solve(valves));

    sc.close();
  }

  static String solve(List<Integer>[] valves) {
    int N = valves.length;

    boolean[][] a = new boolean[N][N + 1];
    for (int r = 0; r < a.length; ++r) {
      a[r][N] = true;
    }
    for (int i = 0; i < valves.length; ++i) {
      for (int valve : valves[i]) {
        a[valve - 1][i] = true;
      }
    }

    for (int c = 0; c < N; ++c) {
      swap(a, c, findR(a, c));

      for (int r = 0; r < N; ++r) {
        if (r != c && a[r][c]) {
          xor(a, c, r);
        }
      }
    }

    StringBuilder result = new StringBuilder();
    for (int r = 0; r < N; ++r) {
      if (a[r][N]) {
        if (result.length() != 0) {
          result.append(" ");
        }

        result.append(r + 1);
      }
    }

    return result.toString();
  }

  static void xor(boolean[][] a, int currentR, int targetR) {
    for (int c = 0; c < a[targetR].length; ++c) {
      a[targetR][c] ^= a[currentR][c];
    }
  }

  static void swap(boolean[][] a, int r1, int r2) {
    boolean[] temp = a[r1];
    a[r1] = a[r2];
    a[r2] = temp;
  }

  static int findR(boolean[][] a, int c) {
    int result = c;
    while (!a[result][c]) {
      ++result;
    }

    return result;
  }
}