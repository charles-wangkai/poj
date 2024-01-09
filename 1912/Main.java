// https://www.hankcs.com/program/algorithm/poj-1912-a-highway-and-the-seven-dwarfs.html

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int N = Integer.parseInt(st.nextToken());
    double[] x = new double[N];
    double[] y = new double[N];
    for (int i = 0; i < N; ++i) {
      st = new StringTokenizer(br.readLine());
      x[i] = Double.parseDouble(st.nextToken());
      y[i] = Double.parseDouble(st.nextToken());
    }
    List<Double> X1 = new ArrayList<Double>();
    List<Double> Y1 = new ArrayList<Double>();
    List<Double> X2 = new ArrayList<Double>();
    List<Double> Y2 = new ArrayList<Double>();
    while (true) {
      String line = br.readLine();
      if (line == null) {
        break;
      }

      st = new StringTokenizer(line);
      X1.add(Double.parseDouble(st.nextToken()));
      Y1.add(Double.parseDouble(st.nextToken()));
      X2.add(Double.parseDouble(st.nextToken()));
      Y2.add(Double.parseDouble(st.nextToken()));
    }

    System.out.println(solve(x, y, X1, Y1, X2, Y2));
  }

  static String solve(
      double[] x, double[] y, List<Double> X1, List<Double> Y1, List<Double> X2, List<Double> Y2) {
    boolean[] sameSides = new boolean[X1.size()];
    if (x.length <= 1) {
      Arrays.fill(sameSides, true);
    } else {
      Point[] points = new Point[x.length];
      for (int i = 0; i < points.length; ++i) {
        points[i] = new Point(x[i], y[i]);
      }

      List<Point> convexHull = buildConvexHull(points);
      double[] angles = new double[convexHull.size()];
      for (int i = 0; i < angles.length; ++i) {
        angles[i] = computeAngle(convexHull.get(i), convexHull.get((i + 1) % convexHull.size()));
      }

      for (int i = 0; i < sameSides.length; ++i) {
        Point p1 = new Point(X1.get(i), Y1.get(i));
        Point p2 = new Point(X2.get(i), Y2.get(i));

        sameSides[i] =
            Math.signum(
                    computeCrossProduct(
                        p1, p2, convexHull.get(findGreaterIndex(angles, computeAngle(p1, p2)))))
                == Math.signum(
                    computeCrossProduct(
                        p1, p2, convexHull.get(findGreaterIndex(angles, computeAngle(p2, p1)))));
      }
    }

    StringBuilder result = new StringBuilder();
    for (boolean sameSide : sameSides) {
      if (result.length() != 0) {
        result.append("\n");
      }

      result.append(sameSide ? "GOOD" : "BAD");
    }

    return result.toString();
  }

  static int findGreaterIndex(double[] angles, double target) {
    int result = 0;
    int lower = 0;
    int upper = angles.length - 1;
    while (lower <= upper) {
      int middle = (lower + upper) / 2;
      if (angles[middle] > target) {
        result = middle;
        upper = middle - 1;
      } else {
        lower = middle + 1;
      }
    }

    return result;
  }

  static List<Point> buildConvexHull(Point[] points) {
    int zIndex = 0;
    for (int i = 1; i < points.length; ++i) {
      if (points[i].y < points[zIndex].y
          || (points[i].y == points[zIndex].y && points[i].x < points[zIndex].x)) {
        zIndex = i;
      }
    }
    final Point z = points[zIndex];

    List<Point> sortedPoints = new ArrayList<Point>();
    for (int i = 0; i < points.length; ++i) {
      if (i != zIndex) {
        sortedPoints.add(points[i]);
      }
    }
    Collections.sort(
        sortedPoints,
        new Comparator<Point>() {
          public int compare(Point p1, Point p2) {
            return Double.compare(computeAngle(z, p1), computeAngle(z, p2));
          }
        });
    sortedPoints.add(z);

    List<Point> result = new ArrayList<Point>();
    result.add(z);
    result.add(sortedPoints.get(0));

    for (int i = 1; i < sortedPoints.size(); ++i) {
      Point p = sortedPoints.get(i);

      if (result.size() >= 2
          && isOnSegment(result.get(result.size() - 2), result.get(result.size() - 1), p)) {
        continue;
      }

      while (result.size() >= 2
          && computeCrossProduct(result.get(result.size() - 2), result.get(result.size() - 1), p)
              <= 0) {
        result.remove(result.size() - 1);
      }

      if (i != sortedPoints.size() - 1) {
        result.add(p);
      }
    }

    return result;
  }

  static double computeAngle(Point o, Point p) {
    double result = Math.atan2(p.y - o.y, p.x - o.x);
    if (result < 0) {
      result += 2 * Math.PI;
    }

    return result;
  }

  static boolean isOnSegment(Point a, Point b, Point p) {
    return computeCrossProduct(a, b, p) == 0
        && isBetween(p.x, a.x, b.x)
        && isBetween(p.y, a.y, b.y);
  }

  static double computeCrossProduct(Point o, Point p1, Point p2) {
    return (p1.x - o.x) * (p2.y - o.y) - (p2.x - o.x) * (p1.y - o.y);
  }

  static boolean isBetween(double s, double v1, double v2) {
    return s >= Math.min(v1, v2) && s <= Math.max(v1, v2);
  }
}

class Point {
  double x;
  double y;

  Point(double x, double y) {
    this.x = x;
    this.y = y;
  }
}