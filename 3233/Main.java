import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int n = sc.nextInt();
    int k = sc.nextInt();
    int m = sc.nextInt();
    int[][] A = new int[n][n];
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        A[i][j] = sc.nextInt();
      }
    }

    System.out.println(solve(A, k, m));

    sc.close();
  }

  static String solve(int[][] A, int k, int m) {
    int n = A.length;

    int[][] initialState = new int[n][2 * n];
    for (int i = 0; i < n; ++i) {
      initialState[i][i] = 1;
    }
    for (int i = 0; i < n; ++i) {
      initialState[i][i + n] = 1;
    }

    int[][] transition = new int[2 * n][2 * n];
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        transition[i][j] = A[i][j];
      }
    }
    for (int i = 0; i < n; ++i) {
      transition[i + n][i] = 1;
    }
    for (int i = 0; i < n; ++i) {
      transition[i + n][i + n] = 1;
    }

    int[][] product = multiply(initialState, pow(transition, k, m), m);

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        if (j != 0) {
          result.append(" ");
        }
        result.append(addMod(product[i][j], ((i == j) ? -1 : 0), m));
      }
      result.append("\n");
    }

    return result.toString();
  }

  static int[][] pow(int[][] matrix, int e, int m) {
    int size = matrix.length;

    int[][] result = new int[size][size];
    for (int i = 0; i < size; ++i) {
      result[i][i] = 1;
    }

    while (e != 0) {
      if (e % 2 == 1) {
        result = multiply(result, matrix, m);
      }

      matrix = multiply(matrix, matrix, m);
      e /= 2;
    }

    return result;
  }

  static int[][] multiply(int[][] a, int[][] b, int m) {
    int[][] result = new int[a.length][b[0].length];

    for (int i = 0; i < result.length; ++i) {
      for (int j = 0; j < result[0].length; ++j) {
        for (int k = 0; k < a[0].length; ++k) {
          result[i][j] = addMod(result[i][j], multiplyMod(a[i][k], b[k][j], m), m);
        }
      }
    }

    return result;
  }

  static int addMod(int x, int y, int m) {
    return (x + y + m) % m;
  }

  static int multiplyMod(int x, int y, int m) {
    return x * y % m;
  }
}
