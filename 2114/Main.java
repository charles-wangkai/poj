// TLE on POJ. AC on Bailian: http://bailian.openjudge.cn/practice/2114

// https://www.hankcs.com/program/algorithm/poj-2114-boatherds.html

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      int N = Integer.parseInt(st.nextToken());
      if (N == 0) {
        break;
      }

      @SuppressWarnings("unchecked")
      List<Integer>[] ds = new List[N];
      @SuppressWarnings("unchecked")
      List<Integer>[] cs = new List[N];
      for (int i = 0; i < N; ++i) {
        st = new StringTokenizer(br.readLine());

        ds[i] = new ArrayList<Integer>();
        cs[i] = new ArrayList<Integer>();

        while (true) {
          int d = Integer.parseInt(st.nextToken());
          if (d == 0) {
            break;
          }

          ds[i].add(d);
          cs[i].add(Integer.parseInt(st.nextToken()));
        }
      }

      while (true) {
        st = new StringTokenizer(br.readLine());
        int x = Integer.parseInt(st.nextToken());
        if (x == 0) {
          break;
        }

        System.out.println(solve(ds, cs, x) ? "AYE" : "NAY");
      }

      System.out.println(".");
    }
  }

  static boolean solve(List<Integer>[] ds, List<Integer>[] cs, int x) {
    int N = ds.length;

    @SuppressWarnings("unchecked")
    List<Edge>[] edgeLists = new List[N];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Edge>();
    }
    for (int i = 0; i < ds.length; ++i) {
      for (int j = 0; j < ds[i].size(); ++j) {
        edgeLists[i].add(new Edge(ds[i].get(j) - 1, cs[i].get(j)));
        edgeLists[ds[i].get(j) - 1].add(new Edge(i, cs[i].get(j)));
      }
    }

    return computePairNum(x, edgeLists, new boolean[N], new int[N], 0) != 0;
  }

  static int computePairNum(
      int x, List<Edge>[] edgeLists, boolean[] centroids, int[] subtreeSizes, int node) {
    buildSubtreeSizes(subtreeSizes, edgeLists, centroids, -1, node);

    int s = findCentroid(edgeLists, centroids, subtreeSizes, subtreeSizes[node], -1, node).node;
    centroids[s] = true;

    int result = 0;
    for (Edge edge : edgeLists[s]) {
      if (!centroids[edge.to]) {
        result += computePairNum(x, edgeLists, centroids, subtreeSizes, edge.to);
      }
    }

    List<Integer> distances = new ArrayList<Integer>();
    distances.add(0);
    for (Edge edge : edgeLists[s]) {
      if (!centroids[edge.to]) {
        List<Integer> subtreeDistances = new ArrayList<Integer>();
        searchPaths(subtreeDistances, edgeLists, centroids, edge.cost, s, edge.to);

        result -= countPairs(x, subtreeDistances);

        distances.addAll(subtreeDistances);
      }
    }

    result += countPairs(x, distances);

    centroids[s] = false;

    return result;
  }

  static int countPairs(int x, List<Integer> distances) {
    return countPairsLessThan(x, distances) - countPairsLessThan(x - 1, distances);
  }

  static int countPairsLessThan(int x, List<Integer> distances) {
    Collections.sort(distances);

    int result = 0;
    int endIndex = distances.size() - 1;
    for (int i = 0; i < distances.size(); ++i) {
      while (endIndex != -1 && distances.get(i) + distances.get(endIndex) > x) {
        --endIndex;
      }

      result += endIndex + 1 - ((i <= endIndex) ? 1 : 0);
    }
    result /= 2;

    return result;
  }

  static void searchPaths(
      List<Integer> subtreeDistances,
      List<Edge>[] edgeLists,
      boolean[] centroids,
      int distance,
      int parent,
      int node) {
    subtreeDistances.add(distance);
    for (Edge edge : edgeLists[node]) {
      if (edge.to != parent && !centroids[edge.to]) {
        searchPaths(subtreeDistances, edgeLists, centroids, distance + edge.cost, node, edge.to);
      }
    }
  }

  static Outcome findCentroid(
      List<Edge>[] edgeLists,
      boolean[] centroids,
      int[] subtreeSizes,
      int totalSize,
      int parent,
      int node) {
    Outcome result = new Outcome(Integer.MAX_VALUE, -1);
    int restSize = totalSize - 1;
    int maxSubtreeSize = 0;
    for (Edge edge : edgeLists[node]) {
      if (edge.to != parent && !centroids[edge.to]) {
        result =
            min(result, findCentroid(edgeLists, centroids, subtreeSizes, totalSize, node, edge.to));

        maxSubtreeSize = Math.max(maxSubtreeSize, subtreeSizes[edge.to]);
        restSize -= subtreeSizes[edge.to];
      }
    }

    maxSubtreeSize = Math.max(maxSubtreeSize, restSize);
    result = min(result, new Outcome(maxSubtreeSize, node));

    return result;
  }

  static Outcome min(Outcome outcome1, Outcome outcome2) {
    return (outcome1.maxSubtreeSize < outcome2.maxSubtreeSize) ? outcome1 : outcome2;
  }

  static void buildSubtreeSizes(
      int[] subtreeSizes, List<Edge>[] edgeLists, boolean[] centroids, int parent, int node) {
    subtreeSizes[node] = 1;

    for (Edge edge : edgeLists[node]) {
      if (edge.to != parent && !centroids[edge.to]) {
        buildSubtreeSizes(subtreeSizes, edgeLists, centroids, node, edge.to);
        subtreeSizes[node] += subtreeSizes[edge.to];
      }
    }
  }
}

class Outcome {
  int maxSubtreeSize;
  int node;

  Outcome(int maxSubtreeSize, int node) {
    this.maxSubtreeSize = maxSubtreeSize;
    this.node = node;
  }
}

class Edge {
  int to;
  int cost;

  Edge(int to, int cost) {
    this.to = to;
    this.cost = cost;
  }
}
