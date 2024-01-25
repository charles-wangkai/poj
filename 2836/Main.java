import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      if (n == 0) {
        break;
      }

      int[] x = new int[n];
      int[] y = new int[n];
      for (int i = 0; i < n; ++i) {
        x[i] = sc.nextInt();
        y[i] = sc.nextInt();
      }

      System.out.println(solve(x, y));
    }

    sc.close();
  }

  static int solve(int[] x, int[] y) {
    int n = x.length;

    int[] areas = new int[1 << n];
    for (int mask = 1; mask < areas.length; ++mask) {
      areas[mask] = computeArea(x, y, mask);
    }

    int[] dp = new int[1 << n];
    Arrays.fill(dp, Integer.MAX_VALUE);
    dp[0] = 0;

    for (int mask = 0; mask < dp.length; ++mask) {
      if (dp[mask] != Integer.MAX_VALUE) {
        int superMask = (1 << n) - 1 - mask;
        int deltaMask = superMask;
        while (deltaMask != 0) {
          dp[mask + deltaMask] = Math.min(dp[mask + deltaMask], dp[mask] + areas[deltaMask]);

          deltaMask = (deltaMask - 1) & superMask;
        }
      }
    }

    return dp[dp.length - 1];
  }

  static int computeArea(int[] x, int[] y, int mask) {
    if ((mask & (mask - 1)) == 0) {
      int result = Integer.MAX_VALUE;
      for (int i = 0; i < x.length; ++i) {
        if (((mask >> i) & 1) == 0) {
          result = Math.min(result, computeArea(x, y, mask + (1 << i)));
        }
      }

      return result;
    }

    int minX = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (int i = 0; i < x.length; ++i) {
      if (((mask >> i) & 1) == 1) {
        minX = Math.min(minX, x[i]);
        maxX = Math.max(maxX, x[i]);
        minY = Math.min(minY, y[i]);
        maxY = Math.max(maxY, y[i]);
      }
    }

    int dx = maxX - minX;
    int dy = maxY - minY;

    if (dx == 0) {
      return Math.max(1, dy);
    }
    if (dy == 0) {
      return dx;
    }

    return dx * dy;
  }
}