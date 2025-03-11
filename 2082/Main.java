// https://www.hankcs.com/program/algorithm/poj-2082-terrible-sets.html

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      if (n == -1) {
        break;
      }

      int[] w = new int[n];
      int[] h = new int[n];
      for (int i = 0; i < n; ++i) {
        w[i] = sc.nextInt();
        h[i] = sc.nextInt();
      }

      System.out.println(solve(w, h));
    }

    sc.close();
  }

  static int solve(int[] w, int[] h) {
    int[] xs = new int[w.length];
    for (int i = 0; i < xs.length; ++i) {
      xs[i] = ((i == 0) ? 0 : xs[i - 1]) + w[i];
    }

    int result = 0;
    Deque<Integer> stack = new ArrayDeque<Integer>();
    for (int i = 0; i <= h.length; ++i) {
      int current = (i == h.length) ? 0 : h[i];
      while (!stack.isEmpty() && current <= h[stack.peek()]) {
        int height = h[stack.pop()];
        result =
            Math.max(
                result,
                height * (((i == 0) ? 0 : xs[i - 1]) - (stack.isEmpty() ? 0 : xs[stack.peek()])));
      }

      stack.push(i);
    }

    return result;
  }
}