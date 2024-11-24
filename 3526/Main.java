// https://www.hankcs.com/program/algorithm/poj-3526-teacher-s-side-of-math-the.html
// https://oi-wiki.org/math/numerical/gauss/

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int a = sc.nextInt();
      int m = sc.nextInt();
      int b = sc.nextInt();
      int n = sc.nextInt();
      if (a == 0 && m == 0 && b == 0 && n == 0) {
        break;
      }

      System.out.println(solve(a, m, b, n));
    }

    sc.close();
  }

  static String solve(int a, int m, int b, int n) {
    double[][] matrix = new double[m * n + 1][m * n + 2];
    matrix[m * n][m * n] = 1;
    matrix[m * n][m * n + 1] = 1;
    for (int d = 0; d <= m * n; ++d) {
      for (int k = 0; k <= d; ++k) {
        matrix[(k % m) * n + (d - k) % n][d] +=
            C(d, k) * Math.pow(a, k / m) * Math.pow(b, (d - k) / n);
      }
    }

    double[] c = gaussElimination(matrix);

    StringBuilder result = new StringBuilder();
    for (int i = c.length - 1; i >= 0; --i) {
      if (result.length() != 0) {
        result.append(" ");
      }

      result.append(Math.round(c[i]));
    }

    return result.toString();
  }

  static int C(int n, int r) {
    int result = 1;
    for (int i = 0; i < r; ++i) {
      result = result * (n - i) / (i + 1);
    }

    return result;
  }

  static double[] gaussElimination(double[][] matrix) {
    int N = matrix.length;

    for (int c = 0; c < N; ++c) {
      swap(matrix, c, findR(matrix, c));

      double factor = matrix[c][c];
      for (int c1 = 0; c1 < matrix[c].length; ++c1) {
        matrix[c][c1] /= factor;
      }

      for (int r = 0; r < N; ++r) {
        if (r != c) {
          double f = matrix[r][c];
          for (int c1 = 0; c1 < matrix[r].length; ++c1) {
            matrix[r][c1] -= matrix[c][c1] * f;
          }
        }
      }
    }

    double[] result = new double[N];
    for (int i = 0; i < result.length; ++i) {
      result[i] = matrix[i][N];
    }

    return result;
  }

  static void swap(double[][] matrix, int r1, int r2) {
    double[] temp = matrix[r1];
    matrix[r1] = matrix[r2];
    matrix[r2] = temp;
  }

  static int findR(double[][] matrix, int c) {
    int result = c;
    for (int r = c + 1; r < matrix.length; ++r) {
      if (Math.abs(matrix[r][c]) > Math.abs(matrix[result][c])) {
        result = r;
      }
    }

    return result;
  }
}