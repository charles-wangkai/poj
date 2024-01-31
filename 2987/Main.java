// https://www.hankcs.com/program/algorithm/poj-2987-firing.html
// http://www.hankcs.com/wp-content/uploads/2015/01/%E7%AE%97%E6%B3%95%E5%90%88%E9%9B%86%E4%B9%8B%E3%80%8A%E6%9C%80%E5%B0%8F%E5%89%B2%E6%A8%A1%E5%9E%8B%E5%9C%A8%E4%BF%A1%E6%81%AF%E5%AD%A6%E7%AB%9E%E8%B5%9B%E4%B8%AD%E7%9A%84%E5%BA%94%E7%94%A8%E3%80%8B.pdf

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Main {
  static final long INF = 100000000000L;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int n = sc.nextInt();
    int m = sc.nextInt();
    int[] b = new int[n];
    for (int i = 0; i < b.length; ++i) {
      b[i] = sc.nextInt();
    }
    int[] u = new int[m];
    int[] v = new int[m];
    for (int i = 0; i < m; ++i) {
      u[i] = sc.nextInt();
      v[i] = sc.nextInt();
    }

    System.out.println(solve(b, u, v));

    sc.close();
  }

  static String solve(int[] b, int[] u, int[] v) {
    int n = b.length;

    // Indices:
    // 0 - source
    // [1,n] - employees
    // n+1 - sink

    List<Edge> edges = new ArrayList<Edge>();

    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[n + 2];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < n; ++i) {
      if (b[i] > 0) {
        addEdges(edges, edgeLists, 0, i + 1, b[i]);
      } else if (b[i] < 0) {
        addEdges(edges, edgeLists, i + 1, n + 1, -b[i]);
      }
    }
    for (int i = 0; i < u.length; ++i) {
      addEdges(edges, edgeLists, u[i], v[i], INF);
    }

    long maxProfit = 0;
    for (int bi : b) {
      if (bi > 0) {
        maxProfit += bi;
      }
    }
    maxProfit -= dinic(edges, edgeLists, 0, n + 1);

    int fireCount = -1;
    int[] levels = bfs(edges, edgeLists, 0, n + 1);
    for (int level : levels) {
      if (level != -1) {
        ++fireCount;
      }
    }

    return String.format("%d %d", fireCount, maxProfit);
  }

  static long dinic(List<Edge> edges, List<Integer>[] edgeLists, int s, int t) {
    long result = 0;
    while (true) {
      int[] levels = bfs(edges, edgeLists, s, t);
      if (levels[t] == -1) {
        break;
      }

      while (true) {
        long minflow = dfs(edges, edgeLists, levels, s, t, Long.MAX_VALUE);
        if (minflow == 0) {
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
        if (edge.capacity != 0 && levels[edge.to] == -1) {
          levels[edge.to] = levels[head] + 1;
          queue.offer(edge.to);
        }
      }
    }

    return levels;
  }

  static long dfs(
      List<Edge> edges, List<Integer>[] edgeLists, int[] levels, int s, int t, long low) {
    if (s == t) {
      return low;
    }

    long result = 0;
    for (int e : edgeLists[s]) {
      Edge edge = edges.get(e);
      if (edge.capacity != 0 && levels[edge.to] == levels[s] + 1) {
        long next =
            dfs(edges, edgeLists, levels, edge.to, t, Math.min(low - result, edge.capacity));
        edge.capacity -= next;
        edges.get(e ^ 1).capacity += next;

        result += next;
        if (result == low) {
          break;
        }
      }
    }

    if (result == 0) {
      levels[s] = -1;
    }

    return result;
  }

  static void addEdges(List<Edge> edges, List<Integer>[] edgeLists, int u, int v, long z) {
    edges.add(new Edge(u, v, z));
    edgeLists[u].add(edges.size() - 1);

    edges.add(new Edge(v, u, 0));
    edgeLists[v].add(edges.size() - 1);
  }
}

class Edge {
  int from;
  int to;
  long capacity;

  Edge(int from, int to, long capacity) {
    this.from = from;
    this.to = to;
    this.capacity = capacity;
  }
}