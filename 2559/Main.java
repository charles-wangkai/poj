import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      if (n == 0) {
        break;
      }

      int[] h = new int[n];
      for (int i = 0; i < h.length; ++i) {
        h[i] = sc.nextInt();
      }

      System.out.println(solve(h));
    }

    sc.close();
  }

  static long solve(int[] h) {
    long result = 0;
    Deque<Integer> stack = new ArrayDeque<Integer>();
    for (int i = 0; i <= h.length; ++i) {
      int current = (i == h.length) ? 0 : h[i];
      while (!stack.isEmpty() && current <= h[stack.peek()]) {
        int height = h[stack.pop()];
        result = Math.max(result, height * (i - (stack.isEmpty() ? -1 : stack.peek()) - 1L));
      }

      stack.push(i);
    }

    return result;
  }
}