import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int n = Integer.parseInt(st.nextToken());
    int k = Integer.parseInt(st.nextToken());
    int[] a = new int[n];
    st = new StringTokenizer(br.readLine());
    for (int i = 0; i < a.length; ++i) {
      a[i] = Integer.parseInt(st.nextToken());
    }

    solve(a, k);
  }

  static void solve(int[] a, int k) {
    print(buildMinValues(a, k));
    print(negate(buildMinValues(negate(a), k)));
  }

  static int[] buildMinValues(int[] values, int k) {
    int[] result = new int[values.length - k + 1];
    Deque<Integer> deque = new ArrayDeque<Integer>();
    for (int i = 0; i < values.length; ++i) {
      while (!deque.isEmpty() && values[deque.peekLast()] >= values[i]) {
        deque.pollLast();
      }
      deque.offerLast(i);

      if (i >= k - 1) {
        result[i - k + 1] = values[deque.peekFirst()];

        if (deque.peekFirst() == i - k + 1) {
          deque.pollFirst();
        }
      }
    }

    return result;
  }

  static int[] negate(int[] values) {
    int[] result = new int[values.length];
    for (int i = 0; i < result.length; ++i) {
      result[i] = -values[i];
    }

    return result;
  }

  static void print(int[] values) {
    for (int i = 0; i < values.length; ++i) {
      if (i != 0) {
        System.out.print(" ");
      }

      System.out.print(values[i]);
    }

    System.out.println();
  }
}