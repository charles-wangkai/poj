import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int n = sc.nextInt();
      int k = sc.nextInt();
      int[] a = new int[n];
      for (int i = 0; i < a.length; ++i) {
        a[i] = sc.nextInt();
      }

      System.out.println(solve(a, k));
    }

    sc.close();
  }

  static long solve(int[] a, int k) {
    long[] prefixSums = new long[a.length + 1];
    for (int i = 1; i < prefixSums.length; ++i) {
      prefixSums[i] = prefixSums[i - 1] + a[i - 1];
    }

    long[] dp = new long[a.length + 1];
    Deque<Integer> deque = new ArrayDeque<Integer>();
    deque.offerLast(0);
    for (int i = k; i < dp.length; ++i) {
      if (i - k >= k) {
        while (deque.size() >= 2) {
          Iterator<Integer> iter = deque.descendingIterator();
          int last = iter.next();
          int secondLast = iter.next();
          if (isPossible(a, dp, prefixSums, secondLast, last, i - k)) {
            break;
          }

          deque.pollLast();
        }

        deque.offerLast(i - k);
      }

      while (deque.size() >= 2) {
        Iterator<Integer> iter = deque.iterator();
        int first = iter.next();
        int second = iter.next();
        if (f(a, dp, prefixSums, first, i) < f(a, dp, prefixSums, second, i)) {
          break;
        }

        deque.pollFirst();
      }

      dp[i] = prefixSums[i] + f(a, dp, prefixSums, deque.peekFirst(), i);
    }

    return dp[dp.length - 1];
  }

  static boolean isPossible(int[] a, long[] dp, long[] prefixSums, int f1, int f2, int f3) {
    int a1 = -a[f1];
    long b1 = dp[f1] - prefixSums[f1] + (long) a[f1] * f1;
    int a2 = -a[f2];
    long b2 = dp[f2] - prefixSums[f2] + (long) a[f2] * f2;
    int a3 = -a[f3];
    long b3 = dp[f3] - prefixSums[f3] + (long) a[f3] * f3;

    return (a2 - a1) * (b3 - b2) < (b2 - b1) * (a3 - a2);
  }

  static long f(int[] a, long[] dp, long[] prefixSums, int j, int x) {
    return -(long) a[j] * x + dp[j] - prefixSums[j] + (long) a[j] * j;
  }
}