// TLE on POJ. AC on Bailian: http://bailian.openjudge.cn/practice/3477

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int K = sc.nextInt();
    int[][] grids = new int[N][N];
    for (int r = 0; r < N; ++r) {
      for (int c = 0; c < N; ++c) {
        grids[r][c] = sc.nextInt();
      }
    }

    System.out.println(solve(grids, K));

    sc.close();
  }

  static int solve(int[][] grids, int K) {
    int N = grids.length;

    // Indices:
    // [0,N^2-1] - in nodes
    // [N^2,2N^2-1] - out nodes

    List<Edge> edges = new ArrayList<Edge>();

    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[2 * N * N];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < N * N; ++i) {
      addEdges(edges, edgeLists, i, i + N * N, 1, -grids[i / N][i % N]);
      addEdges(edges, edgeLists, i, i + N * N, K, 0);
    }
    for (int r = 0; r < N; ++r) {
      for (int c = 0; c < N; ++c) {
        if (c != N - 1) {
          addEdges(edges, edgeLists, r * N + c + N * N, r * N + c + 1, K, 0);
        }
        if (r != N - 1) {
          addEdges(edges, edgeLists, r * N + c + N * N, (r + 1) * N + c, K, 0);
        }
      }
    }

    return -computeMinCostFlow(edges, edgeLists, 0, 2 * N * N - 1, K);
  }

  static int computeMinCostFlow(List<Edge> edges, List<Integer>[] edgeLists, int s, int t, int f) {
    int N = edgeLists.length;

    int result = 0;
    while (f != 0) {
      int[] prevEdges = new int[N];
      int[] distances = new int[N];
      Arrays.fill(distances, Integer.MAX_VALUE);
      distances[s] = 0;
      while (true) {
        boolean updated = false;
        for (int v = 0; v < N; ++v) {
          if (distances[v] != Integer.MAX_VALUE) {
            for (int e : edgeLists[v]) {
              Edge edge = edges.get(e);
              if (edge.capacity != 0 && distances[v] + edge.cost < distances[edge.to]) {
                distances[edge.to] = distances[v] + edge.cost;
                prevEdges[edge.to] = e;

                updated = true;
              }
            }
          }
        }

        if (!updated) {
          break;
        }
      }

      if (distances[t] == Integer.MAX_VALUE) {
        return Integer.MAX_VALUE;
      }

      int d = f;
      for (int v = t; v != s; v = edges.get(prevEdges[v]).from) {
        d = Math.min(d, edges.get(prevEdges[v]).capacity);
      }
      f -= d;
      result += d * distances[t];

      for (int v = t; v != s; v = edges.get(prevEdges[v]).from) {
        Edge edge = edges.get(prevEdges[v]);

        edge.capacity -= d;
        edges.get(prevEdges[v] ^ 1).capacity += d;
      }
    }

    return result;
  }

  static void addEdges(List<Edge> edges, List<Integer>[] edgeLists, int u, int v, int z, int cost) {
    edges.add(new Edge(u, v, z, cost));
    edgeLists[u].add(edges.size() - 1);

    edges.add(new Edge(v, u, 0, -cost));
    edgeLists[v].add(edges.size() - 1);
  }
}

class Edge {
  int from;
  int to;
  int capacity;
  int cost;

  Edge(int from, int to, int capacity, int cost) {
    this.from = from;
    this.to = to;
    this.capacity = capacity;
    this.cost = cost;
  }
}