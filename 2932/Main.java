import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    double[] R = new double[N];
    double[] x = new double[N];
    double[] y = new double[N];
    for (int i = 0; i < N; ++i) {
      R[i] = sc.nextDouble();
      x[i] = sc.nextDouble();
      y[i] = sc.nextDouble();
    }

    System.out.println(solve(R, x, y));

    sc.close();
  }

  static String solve(double[] R, double[] x, double[] y) {
    List<Integer> worshipIndices = new ArrayList<Integer>();
    for (int i = 0; i < R.length; ++i) {
      if (check(R, x, y, i)) {
        worshipIndices.add(i);
      }
    }

    StringBuilder result = new StringBuilder().append(worshipIndices.size()).append("\n");
    for (int i = 0; i < worshipIndices.size(); ++i) {
      if (i != 0) {
        result.append(" ");
      }

      result.append(worshipIndices.get(i) + 1);
    }

    return result.toString();
  }

  static boolean check(double[] R, double[] x, double[] y, int index) {
    for (int i = 0; i < R.length; ++i) {
      if (R[i] > R[index]
          && (x[index] - x[i]) * (x[index] - x[i]) + (y[index] - y[i]) * (y[index] - y[i])
              <= R[i] * R[i]) {
        return false;
      }
    }

    return true;
  }
}