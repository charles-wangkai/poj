import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int t = Integer.parseInt(st.nextToken());
    for (int tc = 0; tc < t; ++tc) {
      do {
        st = new StringTokenizer(br.readLine());
      } while (!st.hasMoreTokens());
      int N = Integer.parseInt(st.nextToken());
      int M = Integer.parseInt(st.nextToken());
      int R = Integer.parseInt(st.nextToken());
      int[] x = new int[R];
      int[] y = new int[R];
      int[] d = new int[R];
      for (int i = 0; i < R; ++i) {
        st = new StringTokenizer(br.readLine());
        x[i] = Integer.parseInt(st.nextToken());
        y[i] = Integer.parseInt(st.nextToken());
        d[i] = Integer.parseInt(st.nextToken());
      }

      System.out.println(solve(N, M, x, y, d));
    }
  }

  static int solve(int N, int M, int[] x, int[] y, int[] d) {
    List<Edge> edges = new ArrayList<Edge>();
    for (int i = 1; i <= N + M; ++i) {
      edges.add(new Edge(0, i, 10000));
    }
    for (int i = 0; i < x.length; ++i) {
      edges.add(new Edge(x[i] + 1, y[i] + N + 1, 10000 - d[i]));
    }
    Collections.sort(
        edges,
        new Comparator<Edge>() {
          public int compare(Edge e1, Edge e2) {
            return e1.distance - e2.distance;
          }
        });

    int result = 0;
    int[] parents = new int[N + M + 1];
    Arrays.fill(parents, -1);
    for (Edge edge : edges) {
      int root1 = findRoot(parents, edge.node1);
      int root2 = findRoot(parents, edge.node2);
      if (root1 != root2) {
        result += edge.distance;
        parents[root2] = root1;
      }
    }

    return result;
  }

  static int findRoot(int[] parents, int node) {
    int root = node;
    while (parents[root] != -1) {
      root = parents[root];
    }

    int p = node;
    while (p != root) {
      int next = parents[p];
      parents[p] = root;

      p = next;
    }

    return root;
  }
}

class Edge {
  int node1;
  int node2;
  int distance;

  Edge(int node1, int node2, int distance) {
    this.node1 = node1;
    this.node2 = node2;
    this.distance = distance;
  }
}
