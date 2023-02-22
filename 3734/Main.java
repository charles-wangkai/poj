import java.util.Scanner;

public class Main {
  static final int MODULUS = 10007;
  static final int[] INITIAL_STATE = {1, 0, 0, 0};
  static final int[][] TRANSITION = {{2, 1, 1, 0}, {1, 2, 0, 1}, {1, 0, 2, 1}, {0, 1, 1, 2}};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int N = sc.nextInt();

      System.out.println(solve(N));
    }

    sc.close();
  }

  static int solve(int N) {
    return mutiply(INITIAL_STATE, pow(TRANSITION, N))[0];
  }

  static int addMod(int x, int y) {
    return (x + y) % MODULUS;
  }

  static int multiplyMod(int x, int y) {
    return x * y % MODULUS;
  }

  static int[] mutiply(int[] v, int[][] m) {
    int size = v.length;

    int[] result = new int[size];
    for (int i = 0; i < size; ++i) {
      for (int j = 0; j < size; ++j) {
        result[i] = addMod(result[i], multiplyMod(v[j], m[j][i]));
      }
    }

    return result;
  }

  static int[][] multiply(int[][] m1, int[][] m2) {
    int size = m1.length;

    int[][] result = new int[size][size];
    for (int i = 0; i < size; ++i) {
      for (int j = 0; j < size; ++j) {
        for (int k = 0; k < size; ++k) {
          result[i][j] = addMod(result[i][j], multiplyMod(m1[i][k], m2[k][j]));
        }
      }
    }

    return result;
  }

  static int[][] pow(int[][] m, int e) {
    int size = m.length;

    int[][] result = new int[size][size];
    for (int i = 0; i < size; ++i) {
      result[i][i] = 1;
    }

    while (e != 0) {
      if (e % 2 == 1) {
        result = multiply(result, m);
      }

      m = multiply(m, m);
      e /= 2;
    }

    return result;
  }
}
