import java.util.Scanner;

public class Main {
  static final int[] R_OFFSETS = {0, -1, 0, 1, 0};
  static final int[] C_OFFSETS = {0, 0, 1, 0, -1};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int M = sc.nextInt();
    int N = sc.nextInt();
    int[][] grid = new int[M][N];
    for (int r = 0; r < M; ++r) {
      for (int c = 0; c < N; ++c) {
        grid[r][c] = sc.nextInt();
      }
    }

    System.out.println(solve(grid));

    sc.close();
  }

  static String solve(int[][] grid) {
    int M = grid.length;
    int N = grid[0].length;

    boolean[][] bestFlipped = null;
    for (int mask = 0; mask < 1 << grid[0].length; ++mask) {
      boolean[][] flipped = computeFlipped(grid, mask);
      if (isBetter(flipped, bestFlipped)) {
        bestFlipped = flipped;
      }
    }

    if (bestFlipped == null) {
      return "IMPOSSIBLE";
    }

    StringBuilder result = new StringBuilder();
    for (int r = 0; r < M; ++r) {
      if (r != 0) {
        result.append("\n");
      }
      for (int c = 0; c < N; ++c) {
        if (c != 0) {
          result.append(" ");
        }
        result.append(bestFlipped[r][c] ? 1 : 0);
      }
    }

    return result.toString();
  }

  static boolean[][] computeFlipped(int[][] grid, int mask) {
    int M = grid.length;
    int N = grid[0].length;

    int[][] state = new int[M][];
    for (int r = 0; r < state.length; ++r) {
      state[r] = grid[r].clone();
    }

    boolean[][] result = new boolean[M][N];
    for (int c = 0; c < N; ++c) {
      if (((mask >> c) & 1) == 1) {
        flip(state, 0, c);
        result[0][c] = true;
      }
    }

    for (int r = 0; r < M - 1; ++r) {
      for (int c = 0; c < N; ++c) {
        if (state[r][c] == 1) {
          flip(state, r + 1, c);
          result[r + 1][c] = true;
        }
      }
    }

    for (int c = 0; c < N; ++c) {
      if (state[M - 1][c] == 1) {
        return null;
      }
    }

    return result;
  }

  static void flip(int[][] state, int r, int c) {
    int M = state.length;
    int N = state[0].length;

    for (int i = 0; i < R_OFFSETS.length; ++i) {
      int adjR = r + R_OFFSETS[i];
      int adjC = c + C_OFFSETS[i];
      if (adjR >= 0 && adjR < M && adjC >= 0 && adjC < N) {
        state[adjR][adjC] = 1 - state[adjR][adjC];
      }
    }
  }

  static boolean isBetter(boolean[][] flipped1, boolean[][] flipped2) {
    if (flipped1 == null) {
      return false;
    }
    if (flipped2 == null) {
      return true;
    }

    int flipCount1 = countFlips(flipped1);
    int flipCount2 = countFlips(flipped2);

    return (flipCount1 != flipCount2) ? (flipCount1 < flipCount2) : isBefore(flipped1, flipped2);
  }

  static int countFlips(boolean[][] flipped) {
    int result = 0;
    for (int r = 0; r < flipped.length; ++r) {
      for (int c = 0; c < flipped[r].length; ++c) {
        if (flipped[r][c]) {
          ++result;
        }
      }
    }

    return result;
  }

  static boolean isBefore(boolean[][] flipped1, boolean[][] flipped2) {
    for (int r = 0; ; ++r) {
      for (int c = 0; c < flipped1[r].length; ++c) {
        if (flipped1[r][c] != flipped2[r][c]) {
          return !flipped1[r][c];
        }
      }
    }
  }
}
