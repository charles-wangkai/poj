import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    for (int tc = 1; ; ++tc) {
      int n = sc.nextInt();
      int d = sc.nextInt();
      if (n == 0) {
        break;
      }

      int[] x = new int[n];
      int[] y = new int[n];
      for (int i = 0; i < n; ++i) {
        x[i] = sc.nextInt();
        y[i] = sc.nextInt();
      }

      System.out.println(String.format("Case %d: %d", tc, solve(x, y, d)));
    }

    sc.close();
  }

  static int solve(int[] x, int[] y, int d) {
    Range[] ranges = new Range[x.length];
    for (int i = 0; i < ranges.length; ++i) {
      if (y[i] > d) {
        return -1;
      }

      double half = Math.sqrt(d * d - y[i] * y[i]);
      ranges[i] = new Range(x[i] - half, x[i] + half);
    }

    Arrays.sort(
        ranges,
        new Comparator<Range>() {
          public int compare(Range r1, Range r2) {
            return Double.compare(r1.left, r2.left);
          }
        });

    int result = 0;
    double end = Integer.MIN_VALUE;
    for (Range range : ranges) {
      if (range.left <= end) {
        end = Math.min(end, range.right);
      } else {
        ++result;
        end = range.right;
      }
    }

    return result;
  }
}

class Range {
  double left;
  double right;

  Range(double left, double right) {
    this.left = left;
    this.right = right;
  }
}
