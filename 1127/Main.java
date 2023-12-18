import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      if (n == 0) {
        break;
      }

      int[] x1 = new int[n];
      int[] y1 = new int[n];
      int[] x2 = new int[n];
      int[] y2 = new int[n];
      for (int i = 0; i < n; ++i) {
        x1[i] = sc.nextInt();
        y1[i] = sc.nextInt();
        x2[i] = sc.nextInt();
        y2[i] = sc.nextInt();
      }

      List<Integer> aList = new ArrayList<Integer>();
      List<Integer> bList = new ArrayList<Integer>();
      while (true) {
        int a = sc.nextInt();
        int b = sc.nextInt();
        if (a == 0 && b == 0) {
          break;
        }

        aList.add(a);
        bList.add(b);
      }

      System.out.println(solve(x1, y1, x2, y2, aList, bList));
    }

    sc.close();
  }

  static String solve(
      int[] x1, int[] y1, int[] x2, int[] y2, List<Integer> aList, List<Integer> bList) {
    int n = x1.length;

    boolean[][] connected = new boolean[n][n];
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        connected[i][j] =
            isIntersect(
                new Point(x1[i], y1[i]),
                new Point(x2[i], y2[i]),
                new Point(x1[j], y1[j]),
                new Point(x2[j], y2[j]));
      }
    }

    for (int k = 0; k < n; ++k) {
      for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
          connected[i][j] |= connected[i][k] && connected[k][j];
        }
      }
    }

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < aList.size(); ++i) {
      if (i != 0) {
        result.append("\n");
      }

      result.append(connected[aList.get(i) - 1][bList.get(i) - 1] ? "CONNECTED" : "NOT CONNECTED");
    }

    return result.toString();
  }

  static boolean isIntersect(Point a, Point b, Point c, Point d) {
    return Math.max(a.x, b.x) >= Math.min(c.x, d.x)
        && Math.max(c.x, d.x) >= Math.min(a.x, b.x)
        && Math.max(a.y, b.y) >= Math.min(c.y, d.y)
        && Math.max(c.y, d.y) >= Math.min(a.y, b.y)
        && computeCrossProduct(a, b, c) * computeCrossProduct(a, b, d) <= 0
        && computeCrossProduct(c, d, a) * computeCrossProduct(c, d, b) <= 0;
  }

  static int computeCrossProduct(Point o, Point p1, Point p2) {
    return (p1.x - o.x) * (p2.y - o.y) - (p2.x - o.x) * (p1.y - o.y);
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