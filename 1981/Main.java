// https://math.stackexchange.com/questions/1781438/finding-the-center-of-a-circle-given-two-points-and-a-radius-algebraically/1781546#1781546

import java.util.Scanner;

public class Main {
  static final double EPSILON = 1e-9;
  static final int RADIUS = 1;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int N = sc.nextInt();
      if (N == 0) {
        break;
      }

      double[] xs = new double[N];
      double[] ys = new double[N];
      for (int i = 0; i < N; ++i) {
        xs[i] = sc.nextDouble();
        ys[i] = sc.nextDouble();
      }

      System.out.println(solve(xs, ys));
    }

    sc.close();
  }

  static int solve(double[] xs, double[] ys) {
    int result = 1;
    for (int i = 0; i < xs.length; ++i) {
      for (int j = i + 1; j < xs.length; ++j) {
        double xa = (xs[j] - xs[i]) / 2;
        double ya = (ys[j] - ys[i]) / 2;
        double x0 = xs[i] + xa;
        double y0 = ys[i] + ya;
        double a = Math.sqrt(xa * xa + ya * ya);
        if (a * a <= RADIUS * RADIUS) {
          double b = Math.sqrt(RADIUS * RADIUS - a * a);

          result = Math.max(result, computeEnclosedCount(xs, ys, x0 + b * ya / a, y0 - b * xa / a));
          result = Math.max(result, computeEnclosedCount(xs, ys, x0 - b * ya / a, y0 + b * xa / a));
        }
      }
    }

    return result;
  }

  static int computeEnclosedCount(double[] xs, double[] ys, double centerX, double centerY) {
    int result = 0;
    for (int i = 0; i < xs.length; ++i) {
      if ((xs[i] - centerX) * (xs[i] - centerX) + (ys[i] - centerY) * (ys[i] - centerY)
          <= RADIUS * RADIUS + EPSILON) {
        ++result;
      }
    }

    return result;
  }
}