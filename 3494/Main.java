import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      String line = br.readLine();
      if (line == null) {
        break;
      }

      StringTokenizer st = new StringTokenizer(line);
      int m = Integer.parseInt(st.nextToken());
      int n = Integer.parseInt(st.nextToken());
      int[][] matrix = new int[m][n];
      for (int r = 0; r < m; ++r) {
        st = new StringTokenizer(br.readLine());
        for (int c = 0; c < n; ++c) {
          matrix[r][c] = Integer.parseInt(st.nextToken());
        }
      }

      System.out.println(solve(matrix));
    }
  }

  static int solve(int[][] matrix) {
    int m = matrix.length;
    int n = matrix[0].length;

    int result = 0;
    int[] heights = new int[n];
    for (int r = 0; r < m; ++r) {
      for (int c = 0; c < heights.length; ++c) {
        if (matrix[r][c] == 1) {
          ++heights[c];
        } else {
          heights[c] = 0;
        }
      }

      result = Math.max(result, computeLargestRectangleInHistogram(heights));
    }

    return result;
  }

  static int computeLargestRectangleInHistogram(int[] heights) {
    heights = Arrays.copyOf(heights, heights.length + 1);

    int result = 0;
    Deque<Integer> indices = new ArrayDeque<Integer>();
    for (int i = 0; i < heights.length; ++i) {
      while (!indices.isEmpty() && heights[i] <= heights[indices.peek()]) {
        int h = heights[indices.pop()];
        result = Math.max(result, h * (i - (indices.isEmpty() ? -1 : indices.peek()) - 1));
      }

      indices.push(i);
    }

    return result;
  }
}