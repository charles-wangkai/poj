// http://www.hankcs.com/program/algorithm/poj-2079-triangle.html

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      if (n == -1) {
        break;
      }

      int[] x = new int[n];
      int[] y = new int[n];
      for (int i = 0; i < n; ++i) {
        x[i] = sc.nextInt();
        y[i] = sc.nextInt();
      }

      System.out.println(String.format("%.2f", solve(x, y)));
    }

    sc.close();
  }

  static double solve(int[] x, int[] y) {
    Point[] points = new Point[x.length];
    for (int i = 0; i < points.length; ++i) {
      points[i] = new Point(x[i], y[i]);
    }

    List<Point> convexHull = buildConvexHull(points);

    int maxDoubleArea = 0;
    for (int offset = 1; offset < (convexHull.size() + 1) / 2; ++offset) {
      int first = (offset + 1) % convexHull.size();
      for (int third = 0; third < convexHull.size(); ++third) {
        int second = (third + offset) % convexHull.size();
        int doubleArea =
            computeDoubleArea(convexHull.get(third), convexHull.get(second), convexHull.get(first));
        while (true) {
          first = (first + 1) % convexHull.size();
          if (first == second || first == third) {
            break;
          }

          int currDoubleArea =
              computeDoubleArea(
                  convexHull.get(third), convexHull.get(second), convexHull.get(first));
          if (currDoubleArea <= doubleArea) {
            break;
          }

          doubleArea = currDoubleArea;
        }
        first = (first - 1 + convexHull.size()) % convexHull.size();

        maxDoubleArea = Math.max(maxDoubleArea, doubleArea);
      }
    }

    return maxDoubleArea / 2.0;
  }

  static int computeDoubleArea(Point p1, Point p2, Point p3) {
    return Math.abs(
        (p1.x * p2.y - p1.y * p2.x) + (p2.x * p3.y - p2.y * p3.x) + (p3.x * p1.y - p3.y * p1.x));
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
    return Math.atan2(p.y - o.y, p.x - o.x);
  }

  static boolean isOnSegment(Point a, Point b, Point p) {
    return computeCrossProduct(a, b, p) == 0
        && isBetween(p.x, a.x, b.x)
        && isBetween(p.y, a.y, b.y);
  }

  static long computeCrossProduct(Point o, Point p1, Point p2) {
    return (long) (p1.x - o.x) * (p2.y - o.y) - (long) (p2.x - o.x) * (p1.y - o.y);
  }

  static boolean isBetween(int s, int v1, int v2) {
    return s >= Math.min(v1, v2) && s <= Math.max(v1, v2);
  }
}

class Point {
  int x;
  int y;

  Point(int x, int y) {
    this.x = x;
    this.y = y;
  }
}