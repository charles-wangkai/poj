import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      int k = sc.nextInt();
      if (n == 0 && k == 0) {
        break;
      }

      int[] u = new int[n - 1];
      int[] v = new int[n - 1];
      int[] l = new int[n - 1];
      for (int i = 0; i < n - 1; ++i) {
        u[i] = sc.nextInt();
        v[i] = sc.nextInt();
        l[i] = sc.nextInt();
      }

      System.out.println(solve(u, v, l, k));
    }

    sc.close();
  }

  static int solve(int[] u, int[] v, int[] l, int k) {
    int n = u.length + 1;

    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[n];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < u.length; ++i) {
      edgeLists[u[i] - 1].add(i);
      edgeLists[v[i] - 1].add(i);
    }

    return computePairNum(u, v, l, k, edgeLists, new boolean[n], new int[n], 0);
  }

  static int computePairNum(
      int[] u,
      int[] v,
      int[] l,
      int k,
      List<Integer>[] edgeLists,
      boolean[] centroids,
      int[] subtreeSizes,
      int node) {
    buildSubtreeSizes(subtreeSizes, u, v, edgeLists, centroids, -1, node);

    int s =
        findCentroid(u, v, edgeLists, centroids, subtreeSizes, subtreeSizes[node], -1, node).node;
    centroids[s] = true;

    int result = 0;
    for (int edge : edgeLists[s]) {
      int other = (s == u[edge] - 1) ? (v[edge] - 1) : (u[edge] - 1);
      if (!centroids[other]) {
        result += computePairNum(u, v, l, k, edgeLists, centroids, subtreeSizes, other);
      }
    }

    List<Integer> distances = new ArrayList<Integer>();
    distances.add(0);
    for (int edge : edgeLists[s]) {
      int other = (s == u[edge] - 1) ? (v[edge] - 1) : (u[edge] - 1);
      if (!centroids[other]) {
        List<Integer> subtreeDistances = new ArrayList<Integer>();
        searchPaths(subtreeDistances, u, v, l, edgeLists, centroids, l[edge], s, other);

        result -= countPairs(k, subtreeDistances);

        distances.addAll(subtreeDistances);
      }
    }

    result += countPairs(k, distances);

    centroids[s] = false;

    return result;
  }

  static int countPairs(int k, List<Integer> distances) {
    Collections.sort(distances);

    int result = 0;
    int endIndex = distances.size() - 1;
    for (int i = 0; i < distances.size(); ++i) {
      while (endIndex != -1 && distances.get(i) + distances.get(endIndex) > k) {
        --endIndex;
      }

      result += endIndex + 1 - ((i <= endIndex) ? 1 : 0);
    }
    result /= 2;

    return result;
  }

  static void searchPaths(
      List<Integer> subtreeDistances,
      int[] u,
      int[] v,
      int[] l,
      List<Integer>[] edgeLists,
      boolean[] centroids,
      int distance,
      int parent,
      int node) {
    subtreeDistances.add(distance);
    for (int edge : edgeLists[node]) {
      int other = (node == u[edge] - 1) ? (v[edge] - 1) : (u[edge] - 1);
      if (other != parent && !centroids[other]) {
        searchPaths(
            subtreeDistances, u, v, l, edgeLists, centroids, distance + l[edge], node, other);
      }
    }
  }

  static Outcome findCentroid(
      int[] u,
      int[] v,
      List<Integer>[] edgeLists,
      boolean[] centroids,
      int[] subtreeSizes,
      int totalSize,
      int parent,
      int node) {
    Outcome result = new Outcome(Integer.MAX_VALUE, -1);
    int restSize = totalSize - 1;
    int maxSubtreeSize = 0;
    for (int edge : edgeLists[node]) {
      int other = (node == u[edge] - 1) ? (v[edge] - 1) : (u[edge] - 1);
      if (other != parent && !centroids[other]) {
        result =
            min(
                result,
                findCentroid(u, v, edgeLists, centroids, subtreeSizes, totalSize, node, other));

        maxSubtreeSize = Math.max(maxSubtreeSize, subtreeSizes[other]);
        restSize -= subtreeSizes[other];
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
      int[] subtreeSizes,
      int[] u,
      int[] v,
      List<Integer>[] edgeLists,
      boolean[] centroids,
      int parent,
      int node) {
    subtreeSizes[node] = 1;

    for (int edge : edgeLists[node]) {
      int other = (node == u[edge] - 1) ? (v[edge] - 1) : (u[edge] - 1);
      if (other != parent && !centroids[other]) {
        buildSubtreeSizes(subtreeSizes, u, v, edgeLists, centroids, node, other);
        subtreeSizes[node] += subtreeSizes[other];
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