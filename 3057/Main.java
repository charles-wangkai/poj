import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  static final int[] R_OFFSETS = {-1, 0, 1, 0};
  static final int[] C_OFFSETS = {0, 1, 0, -1};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int t = sc.nextInt();
    for (int tc = 0; tc < t; ++tc) {
      int Y = sc.nextInt();
      int X = sc.nextInt();
      char[][] squares = new char[Y][X];
      for (int r = 0; r < Y; ++r) {
        String line = sc.next();
        for (int c = 0; c < X; ++c) {
          squares[r][c] = line.charAt(c);
        }
      }

      System.out.println(solve(squares));
    }

    sc.close();
  }

  static String solve(char[][] squares) {
    int row = squares.length;
    int col = squares[0].length;

    int[][] distances = new int[row * col][row * col];
    for (int i = 0; i < distances.length; ++i) {
      Arrays.fill(distances[i], Integer.MAX_VALUE);
    }
    for (int r = 0; r < row; ++r) {
      for (int c = 0; c < col; ++c) {
        if (squares[r][c] == '.') {
          for (int i = 0; i < R_OFFSETS.length; ++i) {
            int adjR = r + R_OFFSETS[i];
            int adjC = c + C_OFFSETS[i];
            if (adjR >= 0 && adjR < row && adjC >= 0 && adjC < col && squares[adjR][adjC] != 'X') {
              distances[r * col + c][adjR * col + adjC] = 1;
            }
          }
        }
      }
    }
    for (int k = 0; k < row * col; ++k) {
      for (int i = 0; i < row * col; ++i) {
        for (int j = 0; j < row * col; ++j) {
          if (distances[i][k] != Integer.MAX_VALUE && distances[k][j] != Integer.MAX_VALUE) {
            distances[i][j] = Math.min(distances[i][j], distances[i][k] + distances[k][j]);
          }
        }
      }
    }

    int[] indices = new int[row * col];
    Arrays.fill(indices, -1);
    int doorCount = 0;
    int personCount = 0;
    for (int r = 0; r < row; ++r) {
      for (int c = 0; c < col; ++c) {
        if (squares[r][c] == 'D') {
          indices[r * col + c] = doorCount;
          ++doorCount;
        } else if (squares[r][c] == '.') {
          indices[r * col + c] = personCount;
          ++personCount;
        }
      }
    }

    int limit = row * col * 2;
    Vertex[] leftVertices = new Vertex[limit * doorCount];
    for (int i = 0; i < leftVertices.length; ++i) {
      leftVertices[i] = new Vertex();
    }
    Vertex[] rightVertices = new Vertex[personCount];
    for (int i = 0; i < rightVertices.length; ++i) {
      rightVertices[i] = new Vertex();
    }
    for (int i = 0; i < row * col; ++i) {
      if (squares[i / col][i % col] == 'D') {
        for (int j = 0; j < row * col; ++j) {
          if (squares[j / col][j % col] == '.') {
            for (int time = distances[j][i]; time <= limit; ++time) {
              int leftIndex = (time - 1) * doorCount + indices[i];
              int rightIndex = indices[j];

              leftVertices[leftIndex].adjacents.add(rightIndex);
              rightVertices[rightIndex].adjacents.add(leftIndex);
            }
          }
        }
      }
    }

    if (rightVertices.length == 0) {
      return "0";
    }

    int matchingCount = 0;
    for (int i = 0; i < leftVertices.length; ++i) {
      if (leftVertices[i].matching == -1
          && search(leftVertices, rightVertices, new boolean[rightVertices.length], i)) {
        ++matchingCount;

        if (matchingCount == rightVertices.length) {
          return String.valueOf(i / doorCount + 1);
        }
      }
    }

    return "impossible";
  }

  static boolean search(
      Vertex[] leftVertices, Vertex[] rightVertices, boolean[] rightVisited, int leftIndex) {
    for (int rightIndex : leftVertices[leftIndex].adjacents) {
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
  List<Integer> adjacents = new ArrayList<Integer>();
  int matching = -1;
}
