import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int[] h = new int[N];
    for (int i = 0; i < h.length; ++i) {
      h[i] = sc.nextInt();
    }

    System.out.println(solve(h));

    sc.close();
  }

  static long solve(int[] h) {
    long result = 0;
    Deque<Integer> stack = new ArrayDeque<Integer>();
    for (int i = h.length - 1; i >= 0; --i) {
      while (!stack.isEmpty() && h[stack.peek()] < h[i]) {
        stack.pop();
      }

      result += (stack.isEmpty() ? h.length : stack.peek()) - i - 1;
      stack.push(i);
    }

    return result;
  }
}