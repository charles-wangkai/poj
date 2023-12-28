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

    StringTokenizer st = new StringTokenizer(br.readLine());
    int N = Integer.parseInt(st.nextToken());
    int[] x = new int[N];
    int[] y = new int[N];
    for (int i = 0; i < N; ++i) {
      st = new StringTokenizer(br.readLine());
      x[i] = Integer.parseInt(st.nextToken());
      y[i] = Integer.parseInt(st.nextToken());
    }

    System.out.println(solve(x, y));
  }

  static int solve(int[] x, int[] y) {
    Point[] points = new Point[x.length];
    for (int i = 0; i < points.length; ++i) {
      points[i] = new Point(x[i], y[i]);
    }

    List<Point> convexHull = buildConvexHull(points);

    int result = -1;
    for (int i = 0; i < convexHull.size(); ++i) {
      Point pi = convexHull.get(i);
      for (int j = i + 1; j < convexHull.size(); ++j) {
        Point pj = convexHull.get(j);

        result = Math.max(result, (pi.x - pj.x) * (pi.x - pj.x) + (pi.y - pj.y) * (pi.y - pj.y));
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
}