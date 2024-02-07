import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Main {
  static final int LIMIT = 50000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int K = sc.nextInt();
    int C = sc.nextInt();
    int M = sc.nextInt();
    int[][] distances = new int[K + C][K + C];
    for (int i = 0; i < K + C; ++i) {
      for (int j = 0; j < K + C; ++j) {
        distances[i][j] = sc.nextInt();
      }
    }

    System.out.println(solve(K, C, distances, M));

    sc.close();
  }

  static int solve(int K, int C, int[][] distances, int M) {
    for (int i = 0; i < K + C; ++i) {
      for (int j = 0; j < K + C; ++j) {
        if (j != i && distances[i][j] == 0) {
          distances[i][j] = Integer.MAX_VALUE;
        }
      }
    }

    for (int p = 0; p < K + C; ++p) {
      for (int i = 0; i < K + C; ++i) {
        for (int j = 0; j < K + C; ++j) {
          if (distances[i][p] != Integer.MAX_VALUE && distances[p][j] != Integer.MAX_VALUE) {
            distances[i][j] = Math.min(distances[i][j], distances[i][p] + distances[p][j]);
          }
        }
      }
    }

    int result = -1;
    int lower = 1;
    int upper = LIMIT;
    while (lower <= upper) {
      int middle = (lower + upper) / 2;
      if (check(K, C, distances, M, middle)) {
        result = middle;
        upper = middle - 1;
      } else {
        lower = middle + 1;
      }
    }

    return result;
  }

  static boolean check(int K, int C, int[][] distances, int M, int maxDistance) {
    // Indices:
    // 0 - source
    // [1,K] - milking machines
    // [K+1,K+C] - cows
    // K+C+1 - sink

    List<Edge> edges = new ArrayList<Edge>();

    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[K + C + 2];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 1; i <= K; ++i) {
      addEdges(edges, edgeLists, 0, i, M);
    }
    for (int i = K + 1; i <= K + C; ++i) {
      addEdges(edges, edgeLists, i, K + C + 1, 1);
    }
    for (int i = 0; i < K; ++i) {
      for (int j = K; j < K + C; ++j) {
        if (distances[i][j] <= maxDistance) {
          addEdges(edges, edgeLists, i + 1, j + 1, 1);
        }
      }
    }

    return dinic(edges, edgeLists, 0, K + C + 1) == C;
  }

  static void addEdges(List<Edge> edges, List<Integer>[] edgeLists, int u, int v, int z) {
    edges.add(new Edge(u, v, z));
    edgeLists[u].add(edges.size() - 1);

    edges.add(new Edge(v, u, 0));
    edgeLists[v].add(edges.size() - 1);
  }

  static int dinic(List<Edge> edges, List<Integer>[] edgeLists, int s, int t) {
    int result = 0;
    while (true) {
      int[] levels = bfs(edges, edgeLists, s, t);
      if (levels == null) {
        break;
      }

      while (true) {
        int minflow = dfs(edges, edgeLists, levels, s, t, Integer.MAX_VALUE);
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
      if (head == t) {
        return levels;
      }

      for (int e : edgeLists[head]) {
        Edge edge = edges.get(e);
        if (edge.capacity != 0 && levels[edge.to] == -1) {
          levels[edge.to] = levels[head] + 1;
          queue.offer(edge.to);
        }
      }
    }

    return null;
  }

  static int dfs(List<Edge> edges, List<Integer>[] edgeLists, int[] levels, int s, int t, int low) {
    if (s == t) {
      return low;
    }

    int result = 0;
    for (int e : edgeLists[s]) {
      Edge edge = edges.get(e);
      if (edge.capacity != 0 && levels[edge.to] == levels[s] + 1) {
        int next = dfs(edges, edgeLists, levels, edge.to, t, Math.min(low - result, edge.capacity));
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
}

class Edge {
  int from;
  int to;
  int capacity;

  Edge(int from, int to, int capacity) {
    this.from = from;
    this.to = to;
    this.capacity = capacity;
  }
}