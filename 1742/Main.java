import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      int m = sc.nextInt();
      if (n == 0) {
        break;
      }

      int[] A = new int[n];
      for (int i = 0; i < A.length; ++i) {
        A[i] = sc.nextInt();
      }
      int[] C = new int[n];
      for (int i = 0; i < C.length; ++i) {
        C[i] = sc.nextInt();
      }

      System.out.println(solve(A, C, m));
    }

    sc.close();
  }

  static int solve(int[] A, int[] C, int m) {
    int result = 0;
    int maxValue = 0;
    boolean[] visited = new boolean[m + 1];
    visited[0] = true;
    int[] lasts = new int[m + 1];
    Arrays.fill(lasts, -1);
    int[] counts = new int[m + 1];
    for (int i = 0; i < A.length; ++i) {
      maxValue = Math.min(m, maxValue + A[i] * C[i]);
      for (int j = A[i]; j <= maxValue; ++j) {
        if (!visited[j]) {
          int prev = j - A[i];
          if (visited[prev] && !(lasts[prev] == i && counts[prev] == C[i])) {
            lasts[j] = i;
            counts[j] = (lasts[prev] == i) ? (counts[prev] + 1) : 1;
            visited[j] = true;
            ++result;
          }
        }
      }
    }

    return result;
  }
}
