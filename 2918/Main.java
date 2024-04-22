import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int t = sc.nextInt();
    for (int tc = 0; tc < t; ++tc) {
      int[][] table = new int[9][9];
      for (int r = 0; r < 9; ++r) {
        String line = sc.next();
        for (int c = 0; c < 9; ++c) {
          table[r][c] = line.charAt(c) - '0';
        }
      }

      System.out.println(String.format("Scenario #%d:\n%s", tc + 1, solve(table)));
    }

    sc.close();
  }

  static String solve(int[][] table) {
    boolean[][] rowSeens = new boolean[9][10];
    boolean[][] colSeens = new boolean[9][10];
    boolean[][][] blockSeens = new boolean[3][3][10];

    for (int r = 0; r < 9; ++r) {
      for (int c = 0; c < 9; ++c) {
        if (table[r][c] != 0) {
          rowSeens[r][table[r][c]] = true;
          colSeens[c][table[r][c]] = true;
          blockSeens[r / 3][c / 3][table[r][c]] = true;
        }
      }
    }

    search(table, rowSeens, colSeens, blockSeens, 0, 0);

    StringBuilder result = new StringBuilder();
    for (int r = 0; r < 9; ++r) {
      for (int c = 0; c < 9; ++c) {
        result.append(table[r][c]);
      }
      result.append("\n");
    }

    return result.toString();
  }

  static boolean search(
      int[][] table,
      boolean[][] rowSeens,
      boolean[][] colSeens,
      boolean[][][] blockSeens,
      int r,
      int c) {
    if (r == 9) {
      return true;
    }
    if (c == 9) {
      return search(table, rowSeens, colSeens, blockSeens, r + 1, 0);
    }
    if (table[r][c] != 0) {
      return search(table, rowSeens, colSeens, blockSeens, r, c + 1);
    }

    for (int d = 1; d <= 9; ++d) {
      if (!rowSeens[r][d] && !colSeens[c][d] && !blockSeens[r / 3][c / 3][d]) {
        table[r][c] = d;

        rowSeens[r][d] = true;
        colSeens[c][d] = true;
        blockSeens[r / 3][c / 3][d] = true;

        if (search(table, rowSeens, colSeens, blockSeens, r, c + 1)) {
          return true;
        }

        rowSeens[r][d] = false;
        colSeens[c][d] = false;
        blockSeens[r / 3][c / 3][d] = false;
      }
    }

    table[r][c] = 0;

    return false;
  }
}