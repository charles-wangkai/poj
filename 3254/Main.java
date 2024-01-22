import java.util.Scanner;

public class Main {
  static final int MODULUS = 100000000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int M = sc.nextInt();
    int N = sc.nextInt();
    int[][] squares = new int[M][N];
    for (int r = 0; r < M; ++r) {
      for (int c = 0; c < N; ++c) {
        squares[r][c] = sc.nextInt();
      }
    }

    System.out.println(solve(squares));

    sc.close();
  }

  static int solve(int[][] squares) {
    int M = squares.length;
    int N = squares[0].length;

    int[] dp = new int[1 << N];
    dp[0] = 1;

    for (int r = 0; r < M; ++r) {
      int[] nextDp = new int[1 << N];
      for (int i = 0; i < nextDp.length; ++i) {
        if (check(squares[r], i)) {
          for (int prevMask = 0; prevMask < 1 << N; ++prevMask) {
            if ((prevMask & i) == 0) {
              nextDp[i] = addMod(nextDp[i], dp[prevMask]);
            }
          }
        }
      }

      dp = nextDp;
    }

    int result = 0;
    for (int dpi : dp) {
      result = addMod(result, dpi);
    }

    return result;
  }

  static boolean check(int[] line, int mask) {
    for (int c = 0; c < line.length; ++c) {
      if (line[c] == 0 && ((mask >> c) & 1) == 1) {
        return false;
      }
    }
    for (int c = 0; c < line.length - 1; ++c) {
      if (((mask >> c) & 1) == 1 && ((mask >> (c + 1)) & 1) == 1) {
        return false;
      }
    }

    return true;
  }

  static int addMod(int x, int y) {
    return (x + y) % MODULUS;
  }
}