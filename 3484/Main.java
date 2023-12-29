import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      String line = br.readLine();
      while (line.isEmpty()) {
        line = br.readLine();
      }

      List<Integer> Xs = new ArrayList<Integer>();
      List<Integer> Ys = new ArrayList<Integer>();
      List<Integer> Zs = new ArrayList<Integer>();
      StringTokenizer st = new StringTokenizer(line);
      Xs.add(Integer.parseInt(st.nextToken()));
      Ys.add(Integer.parseInt(st.nextToken()));
      Zs.add(Integer.parseInt(st.nextToken()));

      while (true) {
        line = br.readLine();
        if (line == null || line.isEmpty()) {
          System.out.println(solve(Xs, Ys, Zs));

          if (line == null) {
            return;
          } else {
            break;
          }
        }

        st = new StringTokenizer(line);
        Xs.add(Integer.parseInt(st.nextToken()));
        Ys.add(Integer.parseInt(st.nextToken()));
        Zs.add(Integer.parseInt(st.nextToken()));
      }
    }
  }

  static String solve(List<Integer> Xs, List<Integer> Ys, List<Integer> Zs) {
    int lower = 0;
    int upper = Integer.MAX_VALUE;
    long totalNum = computeTotalNum(Xs, Ys, Zs, lower, upper);
    if (totalNum % 2 == 0) {
      return "no corruption";
    }

    while (lower != upper) {
      int middle = lower + (upper - lower) / 2;
      if (computeTotalNum(Xs, Ys, Zs, lower, middle) % 2 == 0) {
        lower = middle + 1;
      } else {
        upper = middle;
      }
    }

    return String.format("%d %d", lower, computeTotalNum(Xs, Ys, Zs, lower, lower));
  }

  static long computeTotalNum(
      List<Integer> Xs, List<Integer> Ys, List<Integer> Zs, int lower, int upper) {
    long result = 0;
    for (int i = 0; i < Xs.size(); ++i) {
      int x = Xs.get(i);
      int y = Ys.get(i);
      int z = Zs.get(i);
      if (x <= y) {
        int limit = x + (y - x) / z * z;

        int m = x % z;

        long min;
        if (lower % z <= m) {
          min = lower + ((long) m - lower % z);
        } else {
          min = lower + ((long) m + z - lower % z);
        }

        long max;
        if (upper % z >= m) {
          max = upper - (upper % z - m);
        } else {
          max = upper - ((long) upper % z + z - m);
        }

        long from = Math.max(x, min);
        long to = Math.min(limit, max);
        if (from <= to) {
          result += (to - from) / z + 1;
        }
      }
    }

    return result;
  }
}