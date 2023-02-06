import java.util.Scanner;

public class Main {
  static final int[] R_OFFSETS = {-1, 0, 1, 0};
  static final int[] C_OFFSETS = {0, 1, 0, -1};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int W = sc.nextInt();
      int H = sc.nextInt();
      if (W == 0) {
        break;
      }

      char[][] tiles = new char[H][W];
      for (int r = 0; r < H; ++r) {
        String line = sc.next();
        for (int c = 0; c < W; ++c) {
          tiles[r][c] = line.charAt(c);
        }
      }

      System.out.println(solve(tiles));
    }

    sc.close();
  }

  static int solve(char[][] tiles) {
    int H = tiles.length;
    int W = tiles[0].length;

    for (int r = 0; ; ++r) {
      for (int c = 0; c < W; ++c) {
        if (tiles[r][c] == '@') {
          return search(tiles, new boolean[H][W], r, c);
        }
      }
    }
  }

  static int search(char[][] tiles, boolean[][] visited, int r, int c) {
    int H = tiles.length;
    int W = tiles[0].length;

    visited[r][c] = true;

    int result = 1;
    for (int i = 0; i < R_OFFSETS.length; ++i) {
      int adjR = r + R_OFFSETS[i];
      int adjC = c + C_OFFSETS[i];
      if (adjR >= 0
          && adjR < H
          && adjC >= 0
          && adjC < W
          && tiles[adjR][adjC] == '.'
          && !visited[adjR][adjC]) {
        result += search(tiles, visited, adjR, adjC);
      }
    }

    return result;
  }
}
