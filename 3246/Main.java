// TLE on POJ
// https://www.hankcs.com/program/algorithm/poj-3246-game.html

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      int N = Integer.parseInt(st.nextToken());
      if (N == 0) {
        break;
      }

      int[] xs = new int[N];
      int[] ys = new int[N];
      for (int i = 0; i < N; ++i) {
        st = new StringTokenizer(br.readLine());
        xs[i] = Integer.parseInt(st.nextToken());
        ys[i] = Integer.parseInt(st.nextToken());
      }

      System.out.println(String.format("%.2f", solve(xs, ys)));
    }
  }

  static double solve(int[] xs, int[] ys) {
    Point[] points = new Point[xs.length];
    for (int i = 0; i < points.length; ++i) {
      points[i] = new Point(xs[i], ys[i]);
    }

    List<Point> convexHull = buildConvexHull(points);

    double result = Double.MAX_VALUE;
    for (Point removed : convexHull) {
      Point[] ps = new Point[points.length - 1];
      int index = 0;
      for (Point p : points) {
        if (!p.equals(removed)) {
          ps[index] = p;
          ++index;
        }
      }

      result = Math.min(result, computeArea(buildConvexHull(ps)));
    }

    return result;
  }

  static double computeArea(List<Point> points) {
    int sum = 0;
    for (int i = 0; i < points.size(); ++i) {
      Point p1 = points.get(i);
      Point p2 = points.get((i + 1) % points.size());

      sum += p1.x * p2.y - p1.y * p2.x;
    }

    return Math.abs(sum / 2.0);
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

  static long computeCrossProduct(Point o, Point p1, Point p2) {
    return (long) (p1.x - o.x) * (p2.y - o.y) - (long) (p2.x - o.x) * (p1.y - o.y);
  }

  static boolean isOnSegment(Point a, Point b, Point p) {
    return computeCrossProduct(a, b, p) == 0
        && isBetween(p.x, a.x, b.x)
        && isBetween(p.y, a.y, b.y);
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

  @Override
  public boolean equals(Object o) {
    Point other = (Point) o;

    return x == other.x && y == other.y;
  }
}