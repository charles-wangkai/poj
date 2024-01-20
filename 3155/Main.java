// http://www.hankcs.com/wp-content/uploads/2015/01/%E7%AE%97%E6%B3%95%E5%90%88%E9%9B%86%E4%B9%8B%E3%80%8A%E6%9C%80%E5%B0%8F%E5%89%B2%E6%A8%A1%E5%9E%8B%E5%9C%A8%E4%BF%A1%E6%81%AF%E5%AD%A6%E7%AB%9E%E8%B5%9B%E4%B8%AD%E7%9A%84%E5%BA%94%E7%94%A8%E3%80%8B.pdf
// https://blog.csdn.net/ep1c_heret1c/article/details/54674411

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Main {
  static final int INF = 10000000;
  static final double EPSILON = 1e-9;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int n = sc.nextInt();
    int m = sc.nextInt();
    int[] a = new int[m];
    int[] b = new int[m];
    for (int i = 0; i < m; ++i) {
      a[i] = sc.nextInt();
      b[i] = sc.nextInt();
    }

    System.out.println(solve(n, a, b));

    sc.close();
  }

  static String solve(int n, int[] a, int[] b) {
    if (a.length == 0) {
      return "1\n1";
    }

    List<Integer> hardestTeam = null;
    double lower = 0;
    double upper = a.length + 1;
    while (upper - lower > 1.0 / n / n) {
      double middle = (lower + upper) / 2;

      List<Integer> team = computeTeam(n, a, b, middle);
      if (team == null) {
        upper = middle;
      } else {
        hardestTeam = team;
        lower = middle;
      }
    }

    StringBuilder result = new StringBuilder().append(hardestTeam.size());
    for (int person : hardestTeam) {
      result.append("\n").append(person);
    }

    return result.toString();
  }

  static List<Integer> computeTeam(int n, int[] a, int[] b, double g) {
    int m = a.length;

    // Indices:
    // 0 - source
    // [1,n] - people
    // [n+1,n+m] - poor pairs
    // n+m+1 - sink

    List<Edge> edges = new ArrayList<Edge>();

    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[n + m + 2];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < n; ++i) {
      addEdges(edges, edgeLists, i + 1, n + m + 1, g);
    }
    for (int i = 0; i < m; ++i) {
      addEdges(edges, edgeLists, 0, i + n + 1, 1);

      addEdges(edges, edgeLists, i + n + 1, a[i], INF);
      addEdges(edges, edgeLists, i + n + 1, b[i], INF);
    }

    dinic(edges, edgeLists, 0, n + m + 1);
    int[] levels = bfs(edges, edgeLists, 0, n + m + 1);

    List<Integer> result = new ArrayList<Integer>();
    for (int i = 1; i <= n; ++i) {
      if (levels[i] != -1) {
        result.add(i);
      }
    }

    return result.isEmpty() ? null : result;
  }

  static double dinic(List<Edge> edges, List<Integer>[] edgeLists, int s, int t) {
    double result = 0;
    while (true) {
      int[] levels = bfs(edges, edgeLists, s, t);
      if (levels[t] == -1) {
        break;
      }

      while (true) {
        double minflow = dfs(edges, edgeLists, levels, s, t, Double.MAX_VALUE);
        if (Math.abs(minflow) < EPSILON) {
          break;
        }

        result += minflow;
      }
    }

    return result;
  }

  static int[] bfs(List<Edge> edges, List<Integer>[] edgeLists, int s, int t) {
    int[] levels = new int[edgeLists.length];
    Arrays.fill(levels, -1);
    levels[s] = 0;

    Queue<Integer> queue = new ArrayDeque<Integer>();
    queue.offer(s);

    while (!queue.isEmpty()) {
      int head = queue.poll();

      for (int e : edgeLists[head]) {
        Edge edge = edges.get(e);
        if (edge.capacity > EPSILON && levels[edge.to] == -1) {
          levels[edge.to] = levels[head] + 1;
          queue.offer(edge.to);
        }
      }
    }

    return levels;
  }

  static double dfs(
      List<Edge> edges, List<Integer>[] edgeLists, int[] levels, int s, int t, double low) {
    if (s == t) {
      return low;
    }

    double result = 0;
    for (int e : edgeLists[s]) {
      Edge edge = edges.get(e);
      if (edge.capacity > EPSILON && levels[edge.to] == levels[s] + 1) {
        double next =
            dfs(edges, edgeLists, levels, edge.to, t, Math.min(low - result, edge.capacity));
        edge.capacity -= next;
        edges.get(e ^ 1).capacity += next;

        result += next;
        if (Math.abs(result - low) < EPSILON) {
          break;
        }
      }
    }

    if (Math.abs(result) < EPSILON) {
      levels[s] = -1;
    }

    return result;
  }

  static void addEdges(List<Edge> edges, List<Integer>[] edgeLists, int u, int v, double z) {
    edges.add(new Edge(u, v, z));
    edgeLists[u].add(edges.size() - 1);

    edges.add(new Edge(v, u, 0));
    edgeLists[v].add(edges.size() - 1);
  }
}

class Edge {
  int from;
  int to;
  double capacity;

  Edge(int from, int to, double capacity) {
    this.from = from;
    this.to = to;
    this.capacity = capacity;
  }
}