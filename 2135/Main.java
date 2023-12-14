import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int M = sc.nextInt();
    int[] a = new int[M];
    int[] b = new int[M];
    int[] c = new int[M];
    for (int i = 0; i < M; ++i) {
      a[i] = sc.nextInt();
      b[i] = sc.nextInt();
      c[i] = sc.nextInt();
    }

    System.out.println(solve(N, a, b, c));

    sc.close();
  }

  static int solve(int N, int[] a, int[] b, int[] c) {
    List<Edge> edges = new ArrayList<Edge>();

    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[N];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < a.length; ++i) {
      addEdges(edges, edgeLists, a[i] - 1, b[i] - 1, 1, c[i]);
      addEdges(edges, edgeLists, b[i] - 1, a[i] - 1, 1, c[i]);
    }

    return computeMinCostFlow(edges, edgeLists, 0, N - 1, 2);
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