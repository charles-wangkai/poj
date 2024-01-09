import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int Q = sc.nextInt();
    int[] heights = new int[N];
    for (int i = 0; i < heights.length; ++i) {
      heights[i] = sc.nextInt();
    }
    int[] A = new int[Q];
    int[] B = new int[Q];
    for (int i = 0; i < Q; ++i) {
      A[i] = sc.nextInt();
      B[i] = sc.nextInt();
    }

    System.out.println(solve(heights, A, B));

    sc.close();
  }

  static String solve(int[] heights, int[] A, int[] B) {
    int[][] minHeights =
        buildSparseTable(
            heights,
            new Merger() {
              public int merge(int x, int y) {
                return Math.min(x, y);
              }
            });
    int[][] maxHeights =
        buildSparseTable(
            heights,
            new Merger() {
              public int merge(int x, int y) {
                return Math.max(x, y);
              }
            });

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < A.length; ++i) {
      if (i != 0) {
        result.append("\n");
      }

      int exponent = Integer.numberOfTrailingZeros(Integer.highestOneBit(B[i] - A[i] + 1));
      result.append(
          Math.max(maxHeights[A[i] - 1][exponent], maxHeights[B[i] - (1 << exponent)][exponent])
              - Math.min(
                  minHeights[A[i] - 1][exponent], minHeights[B[i] - (1 << exponent)][exponent]));
    }

    return result.toString();
  }

  static int[][] buildSparseTable(int[] heights, Merger merger) {
    int maxExponent = Integer.numberOfTrailingZeros(Integer.highestOneBit(heights.length));

    int[][] result = new int[heights.length][maxExponent + 1];
    for (int i = 0; i < result.length; ++i) {
      result[i][0] = heights[i];
    }
    for (int i = 1; i <= maxExponent; ++i) {
      for (int beginIndex = 0; beginIndex + (1 << i) <= heights.length; ++beginIndex) {
        result[beginIndex][i] =
            merger.merge(result[beginIndex][i - 1], result[beginIndex + (1 << (i - 1))][i - 1]);
      }
    }

    return result;
  }
}

interface Merger {
  int merge(int x, int y);
}
