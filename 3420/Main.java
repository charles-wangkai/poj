import java.util.Scanner;

public class Main {
  static final int ROW = 4;
  static final int SIZE = 1 << ROW;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      if (N == 0 && M == 0) {
        break;
      }

      System.out.println(solve(N, M));
    }

    sc.close();
  }

  static int solve(int N, int M) {
    int[][] transition = new int[SIZE][SIZE];
    for (int i = 0; i < SIZE; ++i) {
      for (int j = 0; j < SIZE; ++j) {
        if (isCompatible(i, j)) {
          transition[i][j] = 1 % M;
        }
      }
    }

    int[] state = new int[SIZE];
    state[SIZE - 1] = 1;

    return multiply(state, pow(transition, N, M), M)[SIZE - 1];
  }

  static int[][] pow(int[][] base, int e, int M) {
    int size = base.length;

    int[][] result = new int[size][size];
    for (int i = 0; i < size; ++i) {
      result[i][i] = 1 % M;
    }

    while (e != 0) {
      if ((e & 1) == 1) {
        result = multiply(result, base, M);
      }

      base = multiply(base, base, M);
      e >>= 1;
    }

    return result;
  }

  static int[] multiply(int[] v, int[][] matrix, int M) {
    int size = v.length;

    int[] result = new int[size];
    for (int i = 0; i < result.length; ++i) {
      for (int j = 0; j < size; ++j) {
        result[i] = addMod(result[i], multiplyMod(v[j], matrix[j][i], M), M);
      }
    }

    return result;
  }

  static int[][] multiply(int[][] m1, int[][] m2, int M) {
    int size = m1.length;

    int[][] result = new int[size][size];
    for (int i = 0; i < size; ++i) {
      for (int j = 0; j < size; ++j) {
        for (int k = 0; k < size; ++k) {
          result[i][j] = addMod(result[i][j], multiplyMod(m1[i][k], m2[k][j], M), M);
        }
      }
    }

    return result;
  }

  static int addMod(int x, int y, int M) {
    return (x + y) % M;
  }

  static int multiplyMod(int x, int y, int M) {
    return (int) ((long) x * y % M);
  }

  static boolean isCompatible(int mask1, int mask2) {
    for (int i = 0; i < ROW; ++i) {
      if (((mask1 >> i) & 1) == 0) {
        if (((mask2 >> i) & 1) == 0) {
          return false;
        }

        mask2 -= 1 << i;
      }
    }

    for (int i = 0; i < ROW - 1; ++i) {
      if (((mask2 >> i) & 1) == 1 && ((mask2 >> (i + 1)) & 1) == 1) {
        mask2 -= (1 << i) + (1 << (i + 1));
      }
    }

    return mask2 == 0;
  }
}