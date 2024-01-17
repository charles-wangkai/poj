// https://www.hankcs.com/program/algorithm/poj-1418-viva-confetti.html

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
  static final double EPSILON = 1e-13;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      if (n == 0) {
        break;
      }

      double[] xs = new double[n];
      double[] ys = new double[n];
      double[] rs = new double[n];
      for (int i = 0; i < n; ++i) {
        xs[i] = sc.nextDouble();
        ys[i] = sc.nextDouble();
        rs[i] = sc.nextDouble();
      }

      System.out.println(solve(xs, ys, rs));
    }

    sc.close();
  }

  static int solve(double[] xs, double[] ys, double[] rs) {
    int n = xs.length;

    boolean[] visibles = new boolean[n];
    for (int i = 0; i < n; ++i) {
      List<Double> angles = new ArrayList<Double>();
      angles.add(-Math.PI);
      angles.add(Math.PI);
      for (int j = 0; j < n; ++j) {
        double a = rs[i];
        double b = computeDistance(xs[i], ys[i], xs[j], ys[j]);
        double c = rs[j];
        if (a + b >= c && b + c >= a && c + a >= b) {
          double d = Math.atan2(ys[j] - ys[i], xs[j] - xs[i]);
          double e = Math.acos((a * a + b * b - c * c) / (2 * a * b));

          angles.add(normalize(d + e));
          angles.add(normalize(d - e));
        }
      }
      Collections.sort(angles);

      for (int j = 0; j < angles.size() - 1; ++j) {
        double middle = (angles.get(j) + angles.get(j + 1)) / 2;
        for (int sign : new int[] {-1, 1}) {
          double radius = rs[i] + sign * EPSILON;
          int firstIndex =
              findFirstHit(
                  xs, ys, rs, xs[i] + radius * Math.cos(middle), ys[i] + radius * Math.sin(middle));
          if (firstIndex != -1) {
            visibles[firstIndex] = true;
          }
        }
      }
    }

    int result = 0;
    for (boolean visible : visibles) {
      if (visible) {
        ++result;
      }
    }

    return result;
  }

  static int findFirstHit(double[] xs, double[] ys, double[] rs, double x, double y) {
    for (int i = xs.length - 1; i >= 0; --i) {
      if (computeDistance(x, y, xs[i], ys[i]) < rs[i]) {
        return i;
      }
    }

    return -1;
  }

  static double normalize(double angle) {
    if (angle < -Math.PI) {
      return angle + 2 * Math.PI;
    }
    if (angle > Math.PI) {
      return angle - 2 * Math.PI;
    }

    return angle;
  }

  static double computeDistance(double x1, double y1, double x2, double y2) {
    return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }
}