import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
  static final int SIZE = 5;
  static final int HOP_NUM = 5;
  static final int[] R_OFFSETS = {-1, 0, 1, 0};
  static final int[] C_OFFSETS = {0, 1, 0, -1};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int[][] grid = new int[SIZE][SIZE];
    for (int r = 0; r < SIZE; ++r) {
      for (int c = 0; c < SIZE; ++c) {
        grid[r][c] = sc.nextInt();
      }
    }

    System.out.println(solve(grid));

    sc.close();
  }

  static int solve(int[][] grid) {
    Set<String> values = new HashSet<String>();
    for (int r = 0; r < SIZE; ++r) {
      for (int c = 0; c < SIZE; ++c) {
        search(values, grid, r, c, "", HOP_NUM);
      }
    }

    return values.size();
  }

  static void search(Set<String> values, int[][] grid, int r, int c, String path, int restHop) {
    path += grid[r][c];

    if (restHop == 0) {
      values.add(path);

      return;
    }

    for (int i = 0; i < R_OFFSETS.length; ++i) {
      int adjR = r + R_OFFSETS[i];
      int adjC = c + C_OFFSETS[i];
      if (adjR >= 0 && adjR < SIZE && adjC >= 0 && adjC < SIZE) {
        search(values, grid, adjR, adjC, path, restHop - 1);
      }
    }
  }
}
