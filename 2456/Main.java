import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int C = sc.nextInt();
    int[] x = new int[N];
    for (int i = 0; i < x.length; ++i) {
      x[i] = sc.nextInt();
    }

    System.out.println(solve(x, C));

    sc.close();
  }

  static int solve(int[] x, int C) {
    Arrays.sort(x);

    int result = -1;
    int lower = 0;
    int upper = x[x.length - 1] - x[0];
    while (lower <= upper) {
      int middle = (lower + upper) / 2;
      if (check(x, C, middle)) {
        result = middle;
        lower = middle + 1;
      } else {
        upper = middle - 1;
      }
    }

    return result;
  }

  static boolean check(int[] x, int C, int distance) {
    int count = 0;
    int prev = -distance;
    for (int xi : x) {
      if (xi - prev >= distance) {
        ++count;
        prev = xi;
      }
    }

    return count >= C;
  }
}
