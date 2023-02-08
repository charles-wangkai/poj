import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class Main {
  static final int LIMIT = 300;
  static final int[] X_OFFSETS = {-1, 0, 1, 0};
  static final int[] Y_OFFSETS = {0, 1, 0, -1};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int M = sc.nextInt();
    int[] X = new int[M];
    int[] Y = new int[M];
    int[] T = new int[M];
    for (int i = 0; i < M; ++i) {
      X[i] = sc.nextInt();
      Y[i] = sc.nextInt();
      T[i] = sc.nextInt();
    }

    System.out.println(solve(X, Y, T));

    sc.close();
  }

  static int solve(int[] X, int[] Y, int[] T) {
    boolean[][] safes = new boolean[LIMIT + 3][LIMIT + 3];
    for (int i = 0; i < safes.length; ++i) {
      Arrays.fill(safes[i], true);
    }
    for (int i = 0; i < X.length; ++i) {
      safes[X[i]][Y[i]] = false;
      for (int j = 0; j < X_OFFSETS.length; ++j) {
        int x = X[i] + X_OFFSETS[j];
        int y = Y[i] + Y_OFFSETS[j];
        if (x >= 0 && y >= 0) {
          safes[x][y] = false;
        }
      }
    }

    Map<Integer, List<Integer>> timeToIndices = new HashMap<Integer, List<Integer>>();
    for (int i = 0; i < X.length; ++i) {
      if (!timeToIndices.containsKey(T[i])) {
        timeToIndices.put(T[i], new ArrayList<Integer>());
      }
      timeToIndices.get(T[i]).add(i);
    }

    boolean[][] striked = new boolean[LIMIT + 3][LIMIT + 3];
    boolean[][] visited = new boolean[LIMIT + 3][LIMIT + 3];
    Queue<Point> queue = new LinkedList<Point>();
    for (int time = -1; time == -1 || !queue.isEmpty(); ++time) {
      if (timeToIndices.containsKey(time + 1)) {
        for (int index : timeToIndices.get(time + 1)) {
          for (int i = 0; i < X_OFFSETS.length; ++i) {
            int x = X[index] + X_OFFSETS[i];
            int y = Y[index] + Y_OFFSETS[i];
            if (x >= 0 && y >= 0) {
              striked[x][y] = true;
            }
          }
        }
      }

      if (time == -1) {
        if (striked[0][0]) {
          return -1;
        }

        visited[0][0] = true;
        queue.offer(new Point(0, 0));
      } else {
        int size = queue.size();
        for (int i = 0; i < size; ++i) {
          Point head = queue.poll();
          if (safes[head.x][head.y]) {
            return time;
          }

          for (int j = 0; j < X_OFFSETS.length; ++j) {
            int adjX = head.x + X_OFFSETS[j];
            int adjY = head.y + Y_OFFSETS[j];

            if (adjX >= 0 && adjY >= 0 && !striked[adjX][adjY] && !visited[adjX][adjY]) {
              visited[adjX][adjY] = true;
              queue.offer(new Point(adjX, adjY));
            }
          }
        }
      }
    }

    return -1;
  }
}

class Point {
  int x;
  int y;

  Point(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
