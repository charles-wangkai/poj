import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int R = sc.nextInt();
      if (R == -1) {
        break;
      }

      int n = sc.nextInt();
      int[] x = new int[n];
      for (int i = 0; i < x.length; ++i) {
        x[i] = sc.nextInt();
      }

      System.out.println(solve(R, x));
    }

    sc.close();
  }

  static int solve(int R, int[] x) {
    Arrays.sort(x);

    int result = 0;
    int index = 0;
    while (index != x.length) {
      int palantirIndex = index;
      while (palantirIndex + 1 != x.length && x[palantirIndex + 1] - x[index] <= R) {
        ++palantirIndex;
      }

      index = palantirIndex + 1;
      while (index != x.length && x[index] - x[palantirIndex] <= R) {
        ++index;
      }

      ++result;
    }

    return result;
  }
}
