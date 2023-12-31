import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int tc = 1;
    while (true) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      if (N == 0 && M == 0) {
        break;
      }

      int[] froms = new int[M];
      int[] tos = new int[M];
      int[] costs = new int[M];
      for (int i = 0; i < M; ++i) {
        froms[i] = sc.nextInt();
        tos[i] = sc.nextInt();
        costs[i] = sc.nextInt();
      }

      System.out.println(String.format("Instance #%d: %s", tc, solve(N, froms, tos, costs)));
      ++tc;
    }

    sc.close();
  }

  static String solve(int N, int[] froms, int[] tos, int[] costs) {
    // Indices:
    // [0,N-1] - in nodes
    // [N,2N-1] - out nodes

    List<Edge> edges = new ArrayList<Edge>();

    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[2 * N];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < N; ++i) {
      addEdges(edges, edgeLists, i, i + N, 1, 0);
    }
    for (int i = 0; i < froms.length; ++i) {
      addEdges(edges, edgeLists, froms[i] + N, tos[i], 1, costs[i]);
    }

    int result = computeMinCostFlow(edges, edgeLists, N, N - 1, 2);

    return (result == Integer.MAX_VALUE) ? "Not possible" : String.valueOf(result);
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