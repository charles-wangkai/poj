import java.util.Scanner;

public class Main {
  static final int[] R_OFFSETS = {-1, 0, 1, 0};
  static final int[] C_OFFSETS = {0, 1, 0, -1};
  static final int MOVE_NUM_LIMIT = 10;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int w = sc.nextInt();
      int h = sc.nextInt();
      if (w == 0) {
        break;
      }

      int[][] board = new int[h][w];
      for (int r = 0; r < h; ++r) {
        for (int c = 0; c < w; ++c) {
          board[r][c] = sc.nextInt();
        }
      }

      System.out.println(solve(board));
    }

    sc.close();
  }

  static int solve(int[][] board) {
    int h = board.length;
    int w = board[0].length;

    int startR = -1;
    int startC = -1;
    for (int r = 0; r < h; ++r) {
      for (int c = 0; c < w; ++c) {
        if (board[r][c] == 2) {
          startR = r;
          startC = c;
        }
      }
    }

    int minMoveNum = search(board, startR, startC, 1);

    return (minMoveNum == Integer.MAX_VALUE) ? -1 : minMoveNum;
  }

  static int search(int[][] board, int r, int c, int moveNum) {
    int h = board.length;
    int w = board[0].length;

    int result = Integer.MAX_VALUE;
    for (int i = 0; i < R_OFFSETS.length; ++i) {
      int nextR = r;
      int nextC = c;
      while (true) {
        nextR += R_OFFSETS[i];
        nextC += C_OFFSETS[i];

        if (!(nextR >= 0 && nextR < h && nextC >= 0 && nextC < w)) {
          break;
        }
        if (board[nextR][nextC] == 3) {
          return moveNum;
        }
        if (board[nextR][nextC] == 1) {
          int nextStartR = nextR - R_OFFSETS[i];
          int nextStartC = nextC - C_OFFSETS[i];
          if (!(nextStartR == r && nextStartC == c) && moveNum != MOVE_NUM_LIMIT) {
            board[nextR][nextC] = 0;
            result = Math.min(result, search(board, nextStartR, nextStartC, moveNum + 1));
            board[nextR][nextC] = 1;
          }

          break;
        }
      }
    }

    return result;
  }
}
