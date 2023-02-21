import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      int m = sc.nextInt();
      int p = sc.nextInt();
      int a = sc.nextInt() - 1;
      int b = sc.nextInt() - 1;
      if (n == 0) {
        break;
      }

      int[] t = new int[n];
      for (int i = 0; i < t.length; ++i) {
        t[i] = sc.nextInt();
      }
      int[] x = new int[p];
      int[] y = new int[p];
      int[] z = new int[p];
      for (int i = 0; i < p; ++i) {
        x[i] = sc.nextInt() - 1;
        y[i] = sc.nextInt() - 1;
        z[i] = sc.nextInt();
      }

      System.out.println(solve(t, m, x, y, z, a, b));
    }

    sc.close();
  }

  static String solve(int[] t, int m, int[] x, int[] y, int[] z, int a, int b) {
    int n = t.length;

    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[m];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < x.length; ++i) {
      edgeLists[x[i]].add(i);
      edgeLists[y[i]].add(i);
    }

    Double[][] times = new Double[1 << n][m];
    times[0][a] = 0.0;
    for (int ticketsMask = 0; ticketsMask < times.length; ++ticketsMask) {
      for (int city = 0; city < m; ++city) {
        if (times[ticketsMask][city] != null) {
          for (int i = 0; i < n; ++i) {
            if (((ticketsMask >> i) & 1) == 0) {
              int nextTicketsMask = ticketsMask + (1 << i);

              for (int edge : edgeLists[city]) {
                int other = (city == x[edge]) ? y[edge] : x[edge];
                double nextTime = times[ticketsMask][city] + (double) z[edge] / t[i];
                if (times[nextTicketsMask][other] == null
                    || nextTime < times[nextTicketsMask][other]) {
                  times[nextTicketsMask][other] = nextTime;
                }
              }
            }
          }
        }
      }
    }

    Double minTime = null;
    for (int ticketsMask = 0; ticketsMask < times.length; ++ticketsMask) {
      if (minTime == null || (times[ticketsMask][b] != null && times[ticketsMask][b] < minTime)) {
        minTime = times[ticketsMask][b];
      }
    }

    return (minTime == null) ? "Impossible" : String.format("%.9f", minTime);
  }
}
