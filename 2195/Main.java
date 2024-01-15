import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      if (N == 0 && M == 0) {
        break;
      }

      char[][] grid = new char[N][M];
      for (int r = 0; r < N; ++r) {
        String line = sc.next();
        for (int c = 0; c < M; ++c) {
          grid[r][c] = line.charAt(c);
        }
      }

      System.out.println(solve(grid));
    }

    sc.close();
  }

  static int solve(char[][] grid) {
    int N = grid.length;
    int M = grid[0].length;

    List<Point> men = new ArrayList<Point>();
    List<Point> houses = new ArrayList<Point>();
    for (int r = 0; r < N; ++r) {
      for (int c = 0; c < M; ++c) {
        if (grid[r][c] == 'm') {
          men.add(new Point(r, c));
        } else if (grid[r][c] == 'H') {
          houses.add(new Point(r, c));
        }
      }
    }

    int n = men.size();

    // Indices:
    // 0 - source
    // [1,n] - men
    // [n+1,2n] - houses
    // 2n+1 - sink

    List<Edge> edges = new ArrayList<Edge>();

    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[2 * n + 2];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 1; i <= n; ++i) {
      addEdges(edges, edgeLists, 0, i, 1, 0);
    }
    for (int i = n + 1; i <= 2 * n; ++i) {
      addEdges(edges, edgeLists, i, 2 * n + 1, 1, 0);
    }
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        addEdges(edges, edgeLists, i + 1, n + j + 1, 1, computeDistance(men.get(i), houses.get(j)));
      }
    }

    return computeMinCostFlow(edges, edgeLists, 0, 2 * n + 1, n);
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

  static int computeDistance(Point p1, Point p2) {
    return Math.abs(p1.r - p2.r) + Math.abs(p1.c - p2.c);
  }

  static void addEdges(List<Edge> edges, List<Integer>[] edgeLists, int u, int v, int z, int cost) {
    edges.add(new Edge(u, v, z, cost));
    edgeLists[u].add(edges.size() - 1);

    edges.add(new Edge(v, u, 0, -cost));
    edgeLists[v].add(edges.size() - 1);
  }
}

class Point {
  int r;
  int c;

  Point(int r, int c) {
    this.r = r;
    this.c = c;
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