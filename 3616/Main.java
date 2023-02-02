import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int M = sc.nextInt();
    int R = sc.nextInt();
    int[] starts = new int[M];
    int[] ends = new int[M];
    int[] efficiencies = new int[M];
    for (int i = 0; i < M; ++i) {
      starts[i] = sc.nextInt();
      ends[i] = sc.nextInt();
      efficiencies[i] = sc.nextInt();
    }

    System.out.println(solve(N, starts, ends, efficiencies, R));

    sc.close();
  }

  static int solve(int N, final int[] starts, int[] ends, int[] efficiencies, int R) {
    Integer[] sortedIndices = new Integer[starts.length];
    for (int i = 0; i < sortedIndices.length; ++i) {
      sortedIndices[i] = i;
    }
    Arrays.sort(
        sortedIndices,
        new Comparator<Integer>() {
          public int compare(Integer i1, Integer i2) {
            return starts[i1] - starts[i2];
          }
        });

    int[] dp = new int[sortedIndices.length];
    for (int i = 0; i < dp.length; ++i) {
      dp[i] = efficiencies[sortedIndices[i]];
      for (int j = 0; j < i; ++j) {
        if (ends[sortedIndices[j]] + R <= starts[sortedIndices[i]]) {
          dp[i] = Math.max(dp[i], dp[j] + efficiencies[sortedIndices[i]]);
        }
      }
    }

    int result = -1;
    for (int x : dp) {
      result = Math.max(result, x);
    }

    return result;
  }
}
