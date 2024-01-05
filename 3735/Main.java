// TLE on POJ. AC on Bailian: http://bailian.openjudge.cn/practice/3745

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      int m = sc.nextInt();
      int k = sc.nextInt();
      if (n == 0 && m == 0 && k == 0) {
        break;
      }

      sc.nextLine();
      String[] moves = new String[k];
      for (int i = 0; i < moves.length; ++i) {
        moves[i] = sc.nextLine();
      }

      System.out.println(solve(n, m, moves));
    }

    sc.close();
  }

  static String solve(int n, int m, String[] moves) {
    long[][] transition = new long[n + 1][n + 1];
    for (int i = 0; i < transition.length; ++i) {
      transition[i][i] = 1;
    }
    for (String move : moves) {
      String[] parts = move.split(" ");
      if (parts[0].equals("g")) {
        int index = Integer.parseInt(parts[1]) - 1;
        transition = multiply(transition, buildTakeTransition(n, index));
      } else if (parts[0].equals("e")) {
        int index = Integer.parseInt(parts[1]) - 1;
        transition = multiply(transition, buildEatTransition(n, index));
      } else {
        int index1 = Integer.parseInt(parts[1]) - 1;
        int index2 = Integer.parseInt(parts[2]) - 1;
        transition = multiply(transition, buildSwapTransition(n, index1, index2));
      }
    }

    long[] state = new long[n + 1];
    state[n] = 1;

    state = multiply(state, pow(transition, m));

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < n; ++i) {
      if (i != 0) {
        result.append(" ");
      }
      result.append(state[i]);
    }

    return result.toString();
  }

  static long[][] buildSwapTransition(int n, int index1, int index2) {
    long[][] result = new long[n + 1][n + 1];
    for (int i = 0; i < result.length; ++i) {
      if (i != index1 && i != index2) {
        result[i][i] = 1;
      }
    }
    result[index2][index1] = 1;
    result[index1][index2] = 1;

    return result;
  }

  static long[][] buildEatTransition(int n, int index) {
    long[][] result = new long[n + 1][n + 1];
    for (int i = 0; i < result.length; ++i) {
      if (i != index) {
        result[i][i] = 1;
      }
    }

    return result;
  }

  static long[][] buildTakeTransition(int n, int index) {
    long[][] result = new long[n + 1][n + 1];
    for (int i = 0; i < result.length; ++i) {
      result[i][i] = 1;
    }
    result[n][index] = 1;

    return result;
  }

  static long[] multiply(long[] v, long[][] m) {
    int size = v.length;

    long[] result = new long[size];
    for (int i = 0; i < size; ++i) {
      for (int j = 0; j < size; ++j) {
        result[i] += v[j] * m[j][i];
      }
    }

    return result;
  }

  static long[][] multiply(long[][] m1, long[][] m2) {
    int size = m1.length;

    long[][] result = new long[size][size];
    for (int i = 0; i < size; ++i) {
      for (int j = 0; j < size; ++j) {
        for (int k = 0; k < size; ++k) {
          result[i][j] += m1[i][k] * m2[k][j];
        }
      }
    }

    return result;
  }

  static long[][] pow(long[][] m, int e) {
    int size = m.length;

    long[][] result = new long[size][size];
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