// https://www.hankcs.com/program/algorithm/poj-3713-transferring-sylla.html
// https://www.geeksforgeeks.org/articulation-points-or-cut-vertices-in-a-graph/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
  static int time;

  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      int N = Integer.parseInt(st.nextToken());
      int M = Integer.parseInt(st.nextToken());
      if (N == 0 && M == 0) {
        break;
      }

      int[] a = new int[M];
      int[] b = new int[M];
      for (int i = 0; i < M; ++i) {
        st = new StringTokenizer(br.readLine());
        a[i] = Integer.parseInt(st.nextToken());
        b[i] = Integer.parseInt(st.nextToken());
      }

      System.out.println(solve(N, a, b) ? "YES" : "NO");

      br.readLine();
    }
  }

  static boolean solve(int N, int[] a, int[] b) {
    @SuppressWarnings("unchecked")
    List<Integer>[] adjLists = new List[N];
    for (int i = 0; i < adjLists.length; ++i) {
      adjLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < a.length; ++i) {
      adjLists[a[i]].add(b[i]);
      adjLists[b[i]].add(a[i]);
    }

    for (int i = 0; i < N; ++i) {
      boolean[] cuts = new boolean[N];
      int[] times = new int[N];
      int[] lows = new int[N];

      times[i] = Integer.MAX_VALUE;

      time = 0;
      tarjan(adjLists, cuts, times, lows, -1, (i == 0) ? 1 : 0);

      for (int j = 0; j < N; ++j) {
        if (times[j] == 0 || cuts[j]) {
          return false;
        }
      }
    }

    return true;
  }

  static void tarjan(
      List<Integer>[] adjLists, boolean[] cuts, int[] times, int[] lows, int parent, int node) {
    ++time;
    times[node] = time;
    lows[node] = time;

    int childCount = 0;
    for (int adj : adjLists[node]) {
      if (times[adj] == 0) {
        ++childCount;

        tarjan(adjLists, cuts, times, lows, node, adj);
        lows[node] = Math.min(lows[node], lows[adj]);

        if (parent != -1 && lows[adj] >= times[node]) {
          cuts[node] = true;
        }
      } else if (adj != parent) {
        lows[node] = Math.min(lows[node], times[adj]);
      }
    }

    if (parent == -1 && childCount >= 2) {
      cuts[node] = true;
    }
  }
}