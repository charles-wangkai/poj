import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
  static final int LIMIT = 1000000;
  static final int ITERATION_NUM = 50;

  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int n = Integer.parseInt(st.nextToken());
    int k = Integer.parseInt(st.nextToken());
    int[] v = new int[n];
    int[] w = new int[n];
    for (int i = 0; i < n; ++i) {
      st = new StringTokenizer(br.readLine());
      v[i] = Integer.parseInt(st.nextToken());
      w[i] = Integer.parseInt(st.nextToken());
    }

    System.out.println(solve(v, w, k));
  }

  static String solve(final int[] v, final int[] w, int k) {
    double ratio = -1;
    double lower = 0;
    double upper = LIMIT;
    for (int i = 0; i < ITERATION_NUM; ++i) {
      double middle = (lower + upper) / 2;
      if (check(v, w, k, middle)) {
        ratio = middle;
        lower = middle;
      } else {
        upper = middle;
      }
    }

    List<Integer> sortedIndices = new ArrayList<Integer>();
    for (int i = 0; i < v.length; ++i) {
      sortedIndices.add(i);
    }
    final double ratio_ = ratio;
    Collections.sort(
        sortedIndices,
        new Comparator<Integer>() {
          public int compare(Integer i1, Integer i2) {
            return -Double.compare(v[i1] - ratio_ * w[i1], v[i2] - ratio_ * w[i2]);
          }
        });

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < k; ++i) {
      if (i != 0) {
        result.append(" ");
      }

      result.append(sortedIndices.get(i) + 1);
    }

    return result.toString();
  }

  static boolean check(int[] v, int[] w, int k, double ratio) {
    Double[] values = new Double[v.length];
    for (int i = 0; i < values.length; ++i) {
      values[i] = v[i] - ratio * w[i];
    }
    Arrays.sort(values, Collections.reverseOrder());

    double sum = 0;
    for (int i = 0; i < k; ++i) {
      sum += values[i];
    }

    return sum >= 0;
  }
}