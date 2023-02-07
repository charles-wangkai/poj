import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
  static final int LIMIT = 40000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int K = sc.nextInt();
    int[] h = new int[K];
    int[] a = new int[K];
    int[] c = new int[K];
    for (int i = 0; i < K; ++i) {
      h[i] = sc.nextInt();
      a[i] = sc.nextInt();
      c[i] = sc.nextInt();
    }

    System.out.println(solve(h, a, c));

    sc.close();
  }

  static int solve(int[] h, final int[] a, int[] c) {
    Integer[] sortedIndices = new Integer[h.length];
    for (int i = 0; i < sortedIndices.length; ++i) {
      sortedIndices[i] = i;
    }
    Arrays.sort(
        sortedIndices,
        new Comparator<Integer>() {
          public int compare(Integer i1, Integer i2) {
            return a[i1] - a[i2];
          }
        });

    boolean[] dp = new boolean[LIMIT + 1];
    dp[0] = true;
    for (int index : sortedIndices) {
      for (int i = 0; i < c[index]; ++i) {
        for (int j = a[index]; j >= h[index]; --j) {
          dp[j] |= dp[j - h[index]];
        }
      }
    }

    for (int i = dp.length - 1; ; --i) {
      if (dp[i]) {
        return i;
      }
    }
  }
}
