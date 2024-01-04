import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (sc.hasNextInt()) {
      int N = sc.nextInt();
      int[] X = new int[N];
      for (int i = 0; i < X.length; ++i) {
        X[i] = sc.nextInt();
      }

      System.out.println(solve(X));
    }

    sc.close();
  }

  static int solve(int[] X) {
    Arrays.sort(X);

    int result = -1;
    int lower = 0;
    int upper = X[X.length - 1] - X[0];
    while (lower <= upper) {
      int middle = lower + (upper - lower) / 2;
      if (computeLessEqualNum(X, middle) >= (X.length * (X.length - 1L) / 2 + 1) / 2) {
        result = middle;
        upper = middle - 1;
      } else {
        lower = middle + 1;
      }
    }

    return result;
  }

  static long computeLessEqualNum(int[] X, int diff) {
    long result = 0;
    int endIndex = 0;
    for (int i = 0; i < X.length; ++i) {
      while (endIndex != X.length - 1 && X[endIndex + 1] - X[i] <= diff) {
        ++endIndex;
      }

      result += endIndex - i;
    }

    return result;
  }
}