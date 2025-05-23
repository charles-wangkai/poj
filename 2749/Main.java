// https://www.hankcs.com/program/algorithm/poj-2749-building-roads.html
// https://cp-algorithms.com/graph/2SAT.html

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
  static final int LIMIT = 1000000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int A = sc.nextInt();
    int B = sc.nextInt();
    int sx1 = sc.nextInt();
    int sy1 = sc.nextInt();
    int sx2 = sc.nextInt();
    int sy2 = sc.nextInt();
    int[] xs = new int[N];
    int[] ys = new int[N];
    for (int i = 0; i < N; ++i) {
      xs[i] = sc.nextInt();
      ys[i] = sc.nextInt();
    }
    int[] hater1s = new int[A];
    int[] hater2s = new int[A];
    for (int i = 0; i < A; ++i) {
      hater1s[i] = sc.nextInt();
      hater2s[i] = sc.nextInt();
    }
    int[] friend1s = new int[B];
    int[] friend2s = new int[B];
    for (int i = 0; i < B; ++i) {
      friend1s[i] = sc.nextInt();
      friend2s[i] = sc.nextInt();
    }

    System.out.println(solve(xs, ys, hater1s, hater2s, friend1s, friend2s, sx1, sy1, sx2, sy2));

    sc.close();
  }

  static int solve(
      int[] xs,
      int[] ys,
      int[] hater1s,
      int[] hater2s,
      int[] friend1s,
      int[] friend2s,
      int sx1,
      int sy1,
      int sx2,
      int sy2) {
    int result = -1;
    int lower = 0;
    int upper = LIMIT * 12;
    while (lower <= upper) {
      int middle = (lower + upper) / 2;
      if (check(xs, ys, hater1s, hater2s, friend1s, friend2s, sx1, sy1, sx2, sy2, middle)) {
        result = middle;
        upper = middle - 1;
      } else {
        lower = middle + 1;
      }
    }

    return result;
  }

  static boolean check(
      int[] xs,
      int[] ys,
      int[] hater1s,
      int[] hater2s,
      int[] friend1s,
      int[] friend2s,
      int sx1,
      int sy1,
      int sx2,
      int sy2,
      int distanceLimit) {
    int N = xs.length;

    int[] sxs = {sx1, sx2};
    int[] sys = {sy1, sy2};

    @SuppressWarnings("unchecked")
    List<Integer>[] adjLists = new List[N * 2];
    for (int i = 0; i < adjLists.length; ++i) {
      adjLists[i] = new ArrayList<Integer>();
    }

    @SuppressWarnings("unchecked")
    List<Integer>[] reversedAdjLists = new List[N * 2];
    for (int i = 0; i < reversedAdjLists.length; ++i) {
      reversedAdjLists[i] = new ArrayList<Integer>();
    }

    for (int i = 0; i < N; ++i) {
      for (int j = i + 1; j < N; ++j) {
        for (int k = 0; k < 2; ++k) {
          if (computeDistance(xs[i], ys[i], sxs[k], sys[k])
                  + computeDistance(xs[j], ys[j], sxs[k], sys[k])
              > distanceLimit) {
            addEdge(adjLists, reversedAdjLists, i * 2 + k, j * 2 + (1 - k));
            addEdge(adjLists, reversedAdjLists, j * 2 + k, i * 2 + (1 - k));
          }
          if (computeDistance(xs[i], ys[i], sxs[k], sys[k])
                  + computeDistance(xs[j], ys[j], sxs[1 - k], sys[1 - k])
                  + computeDistance(sxs[k], sys[k], sxs[1 - k], sys[1 - k])
              > distanceLimit) {
            addEdge(adjLists, reversedAdjLists, i * 2 + k, j * 2 + k);
            addEdge(adjLists, reversedAdjLists, j * 2 + (1 - k), i * 2 + (1 - k));
          }
        }
      }
    }

    for (int i = 0; i < hater1s.length; ++i) {
      for (int k = 0; k < 2; ++k) {
        addEdge(
            adjLists, reversedAdjLists, (hater1s[i] - 1) * 2 + k, (hater2s[i] - 1) * 2 + (1 - k));
        addEdge(
            adjLists, reversedAdjLists, (hater1s[i] - 1) * 2 + (1 - k), (hater2s[i] - 1) * 2 + k);
        addEdge(
            adjLists, reversedAdjLists, (hater2s[i] - 1) * 2 + k, (hater1s[i] - 1) * 2 + (1 - k));
        addEdge(
            adjLists, reversedAdjLists, (hater2s[i] - 1) * 2 + (1 - k), (hater1s[i] - 1) * 2 + k);
      }
    }

    for (int i = 0; i < friend1s.length; ++i) {
      for (int k = 0; k < 2; ++k) {
        addEdge(adjLists, reversedAdjLists, (friend1s[i] - 1) * 2 + k, (friend2s[i] - 1) * 2 + k);
        addEdge(
            adjLists,
            reversedAdjLists,
            (friend1s[i] - 1) * 2 + (1 - k),
            (friend2s[i] - 1) * 2 + (1 - k));
        addEdge(adjLists, reversedAdjLists, (friend2s[i] - 1) * 2 + k, (friend1s[i] - 1) * 2 + k);
        addEdge(
            adjLists,
            reversedAdjLists,
            (friend2s[i] - 1) * 2 + (1 - k),
            (friend1s[i] - 1) * 2 + (1 - k));
      }
    }

    List<Integer> sortedNodes = new ArrayList<Integer>();
    boolean[] visited = new boolean[N * 2];
    for (int i = 0; i < visited.length; ++i) {
      if (!visited[i]) {
        search1(adjLists, sortedNodes, visited, i);
      }
    }
    Collections.reverse(sortedNodes);

    int k = 0;
    int[] components = new int[N * 2];
    for (int node : sortedNodes) {
      if (components[node] == 0) {
        ++k;
        search2(reversedAdjLists, components, node, k);
      }
    }

    for (int i = 0; i < N; ++i) {
      if (components[i * 2] == components[i * 2 + 1]) {
        return false;
      }
    }

    return true;
  }

  static int computeDistance(int x1, int y1, int x2, int y2) {
    return Math.abs(x1 - x2) + Math.abs(y1 - y2);
  }

  static void search2(List<Integer>[] reversedAdjLists, int[] components, int node, int k) {
    components[node] = k;

    for (int adj : reversedAdjLists[node]) {
      if (components[adj] == 0) {
        search2(reversedAdjLists, components, adj, k);
      }
    }
  }

  static void addEdge(
      List<Integer>[] adjLists, List<Integer>[] reversedAdjLists, int from, int to) {
    adjLists[from].add(to);
    reversedAdjLists[to].add(from);
  }

  static void search1(
      List<Integer>[] adjLists, List<Integer> sortedNodes, boolean[] visited, int node) {
    visited[node] = true;

    for (int adj : adjLists[node]) {
      if (!visited[adj]) {
        search1(adjLists, sortedNodes, visited, adj);
      }
    }

    sortedNodes.add(node);
  }
}