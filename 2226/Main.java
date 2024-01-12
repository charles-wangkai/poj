import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int R = sc.nextInt();
    int C = sc.nextInt();
    char[][] grid = new char[R][C];
    for (int r = 0; r < R; ++r) {
      String line = sc.next();
      for (int c = 0; c < C; ++c) {
        grid[r][c] = line.charAt(c);
      }
    }

    System.out.println(solve(grid));

    sc.close();
  }

  static int solve(char[][] grid) {
    int R = grid.length;
    int C = grid[0].length;

    int[][] horizontalBegins = new int[R][C];
    for (int r = 0; r < R; ++r) {
      int horizontalBegin = 0;
      for (int c = 0; c < C; ++c) {
        if (grid[r][c] == '*') {
          horizontalBegins[r][c] = horizontalBegin;
        } else {
          horizontalBegin = c + 1;
        }
      }
    }

    int[][] verticalBegins = new int[R][C];
    for (int c = 0; c < C; ++c) {
      int vertialBegin = 0;
      for (int r = 0; r < R; ++r) {
        if (grid[r][c] == '*') {
          verticalBegins[r][c] = vertialBegin;
        } else {
          vertialBegin = r + 1;
        }
      }
    }

    Vertex[] leftVertices = new Vertex[R * C];
    for (int i = 0; i < leftVertices.length; ++i) {
      leftVertices[i] = new Vertex();
    }
    Vertex[] rightVertices = new Vertex[R * C];
    for (int i = 0; i < rightVertices.length; ++i) {
      rightVertices[i] = new Vertex();
    }
    for (int r = 0; r < R; ++r) {
      for (int c = 0; c < C; ++c) {
        if (grid[r][c] == '*') {
          int leftIndex = r * C + horizontalBegins[r][c];
          int rightIndex = verticalBegins[r][c] * C + c;

          leftVertices[leftIndex].adjs.add(rightIndex);
          rightVertices[rightIndex].adjs.add(leftIndex);
        }
      }
    }

    int matchingCount = 0;
    for (int i = 0; i < leftVertices.length; ++i) {
      if (leftVertices[i].matching == -1
          && search(leftVertices, rightVertices, new boolean[rightVertices.length], i)) {
        ++matchingCount;
      }
    }

    return matchingCount;
  }

  static boolean search(
      Vertex[] leftVertices, Vertex[] rightVertices, boolean[] rightVisited, int leftIndex) {
    for (int rightIndex : leftVertices[leftIndex].adjs) {
      if (!rightVisited[rightIndex]) {
        rightVisited[rightIndex] = true;

        if (rightVertices[rightIndex].matching == -1
            || search(
                leftVertices, rightVertices, rightVisited, rightVertices[rightIndex].matching)) {
          leftVertices[leftIndex].matching = rightIndex;
          rightVertices[rightIndex].matching = leftIndex;

          return true;
        }
      }
    }

    return false;
  }
}

class Vertex {
  List<Integer> adjs = new ArrayList<Integer>();
  int matching = -1;
}