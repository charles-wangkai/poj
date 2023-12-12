// https://blog.csdn.net/Richard__Luan/article/details/81002097

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int F = sc.nextInt();
    int D = sc.nextInt();
    int[][] foods = new int[N][];
    int[][] drinks = new int[N][];
    for (int i = 0; i < N; ++i) {
      int Fi = sc.nextInt();
      int Di = sc.nextInt();
      foods[i] = new int[Fi];
      for (int j = 0; j < foods[i].length; ++j) {
        foods[i][j] = sc.nextInt();
      }
      drinks[i] = new int[Di];
      for (int j = 0; j < drinks[i].length; ++j) {
        drinks[i][j] = sc.nextInt();
      }
    }

    System.out.println(solve(foods, drinks, F, D));

    sc.close();
  }

  static int solve(int[][] foods, int[][] drinks, int F, int D) {
    int N = foods.length;

    // Indices:
    // 0 - source
    // [1,N] - cows1
    // [N+1,2N] - cows2
    // [2N+1,2N+F] - foods
    // [2N+F+1,2N+F+D] - drinks
    // 2N+F+D+1 - sink

    List<Edge> edges = new ArrayList<Edge>();

    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[2 * N + F + D + 2];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < foods.length; ++i) {
      for (int food : foods[i]) {
        addEdges(edges, edgeLists, 2 * N + food, i + 1, 1);
      }
    }
    for (int i = 0; i < drinks.length; ++i) {
      for (int drink : drinks[i]) {
        addEdges(edges, edgeLists, N + i + 1, 2 * N + F + drink, 1);
      }
    }
    for (int i = 0; i < N; ++i) {
      addEdges(edges, edgeLists, i + 1, N + i + 1, 1);
    }
    for (int i = 0; i < F; ++i) {
      addEdges(edges, edgeLists, 0, 2 * N + i + 1, 1);
    }
    for (int i = 0; i < D; ++i) {
      addEdges(edges, edgeLists, 2 * N + F + i + 1, 2 * N + F + D + 1, 1);
    }

    return dinic(edges, edgeLists, 0, 2 * N + F + D + 1);
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