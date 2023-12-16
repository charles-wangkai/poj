import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      int[][] Z = new int[N][M];
      for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j) {
          Z[i][j] = sc.nextInt();
        }
      }

      System.out.println(String.format("%.6f", solve(Z)));
    }

    sc.close();
  }

  static double solve(int[][] Z) {
    int N = Z.length;
    int M = Z[0].length;

    // Indices:
    // 0 - source
    // [1,N] - toys
    // [N+1,2N] - workshop_1
    // [2N+1,3N] - workshop_2
    // ...
    // [MN+1,(M+1)N] - workshop_M
    // (M+1)N+1 - sink

    List<Edge> edges = new ArrayList<Edge>();

    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[(M + 1) * N + 2];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < N; ++i) {
      addEdges(edges, edgeLists, 0, i + 1, 1, 0);
    }
    for (int j = 0; j < M; ++j) {
      for (int k = 0; k < N; ++k) {
        addEdges(edges, edgeLists, (j + 1) * N + k + 1, (M + 1) * N + 1, 1, 0);

        for (int i = 0; i < N; ++i) {
          addEdges(edges, edgeLists, i + 1, (j + 1) * N + k + 1, 1, (k + 1) * Z[i][j]);
        }
      }
    }

    return (double) computeMinCostFlow(edges, edgeLists, 0, (M + 1) * N + 1, N) / N;
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