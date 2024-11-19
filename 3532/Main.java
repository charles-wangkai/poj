// https://www.hankcs.com/program/algorithm/poj-3532-resistance.html
// https://oi-wiki.org/math/numerical/gauss/

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int M = sc.nextInt();
    int[] X = new int[M];
    int[] Y = new int[M];
    int[] R = new int[M];
    for (int i = 0; i < M; ++i) {
      X[i] = sc.nextInt();
      Y[i] = sc.nextInt();
      R[i] = sc.nextInt();
    }

    System.out.println(String.format("%.2f", solve(N, X, Y, R)));

    sc.close();
  }

  static double solve(int N, int[] X, int[] Y, int[] R) {
    double[][] resistanceInvs = new double[N][N];
    for (int i = 0; i < X.length; ++i) {
      resistanceInvs[X[i] - 1][Y[i] - 1] += 1.0 / R[i];
      resistanceInvs[Y[i] - 1][X[i] - 1] += 1.0 / R[i];
    }

    double[][] matrix = new double[N][N + 1];
    matrix[0][0] = 1;
    matrix[N - 1][N - 1] = 1;
    matrix[0][N] = 1;
    for (int node = 1; node < N - 1; ++node) {
      for (int other = 0; other < N; ++other) {
        matrix[node][node] -= resistanceInvs[node][other];
        matrix[node][other] += resistanceInvs[node][other];
      }
    }

    double[] voltages = gaussElimination(matrix);

    double current = 0;
    for (int node = 0; node < N; ++node) {
      current += (voltages[0] - voltages[node]) * resistanceInvs[0][node];
    }

    return 1 / current;
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