// https://www.hankcs.com/program/algorithm/poj-3608-bridge-across-islands.html

import java.util.Scanner;

public class Main {
  static final double EPSILON = 1e-9;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      if (N == 0 && M == 0) {
        break;
      }

      double[] x1s = new double[N];
      double[] y1s = new double[N];
      for (int i = 0; i < N; ++i) {
        x1s[i] = sc.nextDouble();
        y1s[i] = sc.nextDouble();
      }
      double[] x2s = new double[M];
      double[] y2s = new double[M];
      for (int i = 0; i < M; ++i) {
        x2s[i] = sc.nextDouble();
        y2s[i] = sc.nextDouble();
      }

      System.out.println(String.format("%.9f", solve(x1s, y1s, x2s, y2s)));
    }

    sc.close();
  }

  static double solve(double[] x1s, double[] y1s, double[] x2s, double[] y2s) {
    Point[] P = new Point[x1s.length];
    for (int i = 0; i < P.length; ++i) {
      P[i] = new Point(x1s[i], y1s[i]);
    }
    sortAntiClockwise(P);

    Point[] Q = new Point[x2s.length];
    for (int i = 0; i < Q.length; ++i) {
      Q[i] = new Point(x2s[i], y2s[i]);
    }
    sortAntiClockwise(Q);

    int pIndex = 0;
    for (int i = 0; i < P.length; ++i) {
      if (P[i].y < P[pIndex].y) {
        pIndex = i;
      }
    }

    int qIndex = 0;
    for (int i = 0; i < Q.length; ++i) {
      if (Q[i].y > Q[qIndex].y) {
        qIndex = i;
      }
    }

    double result = Double.MAX_VALUE;
    for (int i = 0; i < P.length; ++i) {
      while (computeCrossProduct(P[(pIndex + 1) % P.length], Q[(qIndex + 1) % Q.length], P[pIndex])
          > computeCrossProduct(P[(pIndex + 1) % P.length], Q[qIndex], P[pIndex]) + EPSILON) {
        qIndex = (qIndex + 1) % Q.length;
      }

      result =
          Math.min(
              result,
              computeDistanceBetweenSegments(
                  P[pIndex], P[(pIndex + 1) % P.length], Q[qIndex], Q[(qIndex + 1) % Q.length]));

      pIndex = (pIndex + 1) % P.length;
    }

    return result;
  }

  static void sortAntiClockwise(Point[] polygon) {
    for (int i = 0; i < polygon.length; ++i) {
      double crossProduct =
          computeCrossProduct(
              polygon[i], polygon[(i + 1) % polygon.length], polygon[(i + 2) % polygon.length]);
      if (crossProduct > EPSILON) {
        return;
      }

      if (crossProduct < -EPSILON) {
        for (int left = 0, right = polygon.length - 1; left < right; ++left, --right) {
          Point temp = polygon[left];
          polygon[left] = polygon[right];
          polygon[right] = temp;
        }

        return;
      }
    }
  }

  static double computeDistanceBetweenSegments(Point p1, Point p2, Point q1, Point q2) {
    return Math.min(
        Math.min(
            computeDistanceBetweenPointAndSegemnt(p1, q1, q2),
            computeDistanceBetweenPointAndSegemnt(p2, q1, q2)),
        Math.min(
            computeDistanceBetweenPointAndSegemnt(q1, p1, p2),
            computeDistanceBetweenPointAndSegemnt(q2, p1, p2)));
  }

  static double computeDistanceBetweenPointAndSegemnt(Point p, Point q1, Point q2) {
    if (computeDistance(q1, q2) < EPSILON) {
      return computeDistance(p, q1);
    }
    if (computeDotProduct(q1, p, q2) < -EPSILON) {
      return computeDistance(q1, p);
    }
    if (computeDotProduct(q2, p, q1) < -EPSILON) {
      return computeDistance(q2, p);
    }

    return Math.abs(computeCrossProduct(p, q1, q2)) / computeDistance(q1, q2);
  }

  static double computeDistance(Point point1, Point point2) {
    return Math.sqrt(
        (point1.x - point2.x) * (point1.x - point2.x)
            + (point1.y - point2.y) * (point1.y - point2.y));
  }

  static double computeCrossProduct(Point o, Point point1, Point point2) {
    return (point1.x - o.x) * (point2.y - o.y) - (point2.x - o.x) * (point1.y - o.y);
  }

  static double computeDotProduct(Point o, Point point1, Point point2) {
    return (point1.x - o.x) * (point2.x - o.x) + (point1.y - o.y) * (point2.y - o.y);
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