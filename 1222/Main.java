import java.util.Scanner;

public class Main {
  static final int ROW = 5;
  static final int COL = 6;
  static final int[] R_OFFSETS = {-1, 0, 0, 0, 1};
  static final int[] C_OFFSETS = {0, -1, 0, 1, 0};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 1; tc <= T; ++tc) {
      int[][] puzzle = new int[ROW][COL];
      for (int r = 0; r < ROW; ++r) {
        for (int c = 0; c < COL; ++c) {
          puzzle[r][c] = sc.nextInt();
        }
      }

      System.out.println(String.format("PUZZLE #%d\n%s", tc, solve(puzzle)));
    }

    sc.close();
  }

  static String solve(int[][] puzzle) {
    for (int firstColMask = 0; ; ++firstColMask) {
      boolean[][] pressed = buildPressed(puzzle, firstColMask);
      if (pressed != null) {
        StringBuilder result = new StringBuilder();
        for (int r = 0; r < ROW; ++r) {
          if (r != 0) {
            result.append("\n");
          }
          for (int c = 0; c < COL; ++c) {
            if (c != 0) {
              result.append(" ");
            }

            result.append(pressed[r][c] ? 1 : 0);
          }
        }

        return result.toString();
      }
    }
  }

  static boolean[][] buildPressed(int[][] puzzle, int firstColMask) {
    int[][] states = new int[ROW][];
    for (int r = 0; r < states.length; ++r) {
      states[r] = puzzle[r].clone();
    }

    boolean[][] result = new boolean[ROW][COL];
    for (int r = 0; r < ROW; ++r) {
      if (((firstColMask >> r) & 1) == 1) {
        press(states, r, 0);
        result[r][0] = true;
      }
    }
    for (int c = 0; c < COL - 1; ++c) {
      for (int r = 0; r < ROW; ++r) {
        if (states[r][c] == 1) {
          press(states, r, c + 1);
          result[r][c + 1] = true;
        }
      }
    }

    for (int r = 0; r < ROW; ++r) {
      if (states[r][COL - 1] == 1) {
        return null;
      }
    }

    return result;
  }

  static void press(int[][] states, int r, int c) {
    for (int i = 0; i < R_OFFSETS.length; ++i) {
      int adjR = r + R_OFFSETS[i];
      int adjC = c + C_OFFSETS[i];
      if (adjR >= 0 && adjR < ROW && adjC >= 0 && adjC < COL) {
        states[adjR][adjC] ^= 1;
      }
    }
  }
}