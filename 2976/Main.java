import java.util.Arrays;
import java.util.Scanner;

public class Main {
  static final int ITERATION_NUM = 100;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      int k = sc.nextInt();
      if (n == 0 && k == 0) {
        break;
      }

      int[] a = new int[n];
      for (int i = 0; i < a.length; ++i) {
        a[i] = sc.nextInt();
      }
      int[] b = new int[n];
      for (int i = 0; i < b.length; ++i) {
        b[i] = sc.nextInt();
      }

      System.out.println(solve(a, b, k));
    }

    sc.close();
  }

  static int solve(int[] a, int[] b, int k) {
    double lower = 0;
    double upper = 1;
    for (int i = 0; i < ITERATION_NUM; ++i) {
      double middle = (lower + upper) / 2;
      if (check(a, b, k, middle)) {
        lower = middle;
      } else {
        upper = middle;
      }
    }

    return (int) Math.round((lower + upper) / 2 * 100);
  }

  static boolean check(int[] a, int[] b, int k, double average) {
    double[] values = new double[a.length];
    for (int i = 0; i < values.length; ++i) {
      values[i] = a[i] - average * b[i];
    }
    Arrays.sort(values);

    double sum = 0;
    for (int i = k; i < values.length; ++i) {
      sum += values[i];
    }

    return sum >= 0;
  }
}