import java.util.Arrays;
import java.util.Scanner;

public class Main {
  static final double G = 10;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    for (int tc = 0; tc < C; ++tc) {
      int N = sc.nextInt();
      int H = sc.nextInt();
      int R = sc.nextInt();
      int T = sc.nextInt();

      System.out.println(solve(N, H, R, T));
    }

    sc.close();
  }

  static String solve(int N, int H, int R, int T) {
    double[] heights = new double[N];
    for (int i = 0; i < heights.length; ++i) {
      heights[i] = computeHeight(H, T - i);
    }

    Arrays.sort(heights);

    for (int i = 0; i < heights.length; ++i) {
      heights[i] += 2 * (R / 100.0) * i;
    }

    StringBuilder result = new StringBuilder();
    for (double x : heights) {
      if (result.length() != 0) {
        result.append(" ");
      }
      result.append(String.format("%.2f", x));
    }

    return result.toString();
  }

  static double computeHeight(int H, int time) {
    double rest = Math.max(0, time);

    double roundTime = 2 * Math.sqrt(2 * H / G);
    rest = rest - Math.floor(rest / roundTime) * roundTime;

    return (rest <= roundTime / 2)
        ? (H - G * rest * rest / 2)
        : (H - G * (roundTime - rest) * (roundTime - rest) / 2);
  }
}
