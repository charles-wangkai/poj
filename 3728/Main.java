// https://www.hankcs.com/program/algorithm/poj-3728-the-merchant.html

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int[] w = new int[N];
    for (int i = 0; i < w.length; ++i) {
      w[i] = sc.nextInt();
    }
    int[] u = new int[N - 1];
    int[] v = new int[N - 1];
    for (int i = 0; i < N - 1; ++i) {
      u[i] = sc.nextInt();
      v[i] = sc.nextInt();
    }
    int Q = sc.nextInt();
    int[] a = new int[Q];
    int[] b = new int[Q];
    for (int i = 0; i < Q; ++i) {
      a[i] = sc.nextInt();
      b[i] = sc.nextInt();
    }

    System.out.println(solve(w, u, v, a, b));

    sc.close();
  }

  static String solve(int[] w, int[] u, int[] v, int[] a, int[] b) {
    int N = w.length;

    @SuppressWarnings("unchecked")
    List<Integer>[] adjLists = new List[N];
    for (int i = 0; i < adjLists.length; ++i) {
      adjLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < u.length; ++i) {
      adjLists[u[i] - 1].add(v[i] - 1);
      adjLists[v[i] - 1].add(u[i] - 1);
    }

    int depthLogSize = Integer.toBinaryString(N).length();

    int[] depths = new int[N];
    int[][] parents = new int[N][depthLogSize];
    search(adjLists, depths, parents, 0, -1, 0);

    int[][] minPrices = new int[N][depthLogSize];
    for (int i = 0; i < minPrices.length; ++i) {
      minPrices[i][0] = w[i];
    }

    int[][] maxPrices = new int[N][depthLogSize];
    for (int i = 0; i < maxPrices.length; ++i) {
      maxPrices[i][0] = w[i];
    }

    int[][] upProfits = new int[N][depthLogSize];
    int[][] downProfits = new int[N][depthLogSize];

    for (int j = 1; j < depthLogSize; ++j) {
      for (int i = 0; i < N; ++i) {
        int relay = parents[i][j - 1];

        parents[i][j] = (relay == -1) ? -1 : parents[relay][j - 1];

        if ((1 << j) <= depths[i] + 1) {
          minPrices[i][j] = Math.min(minPrices[i][j - 1], minPrices[relay][j - 1]);
          maxPrices[i][j] = Math.max(maxPrices[i][j - 1], maxPrices[relay][j - 1]);

          upProfits[i][j] =
              Math.max(
                  Math.max(upProfits[i][j - 1], upProfits[relay][j - 1]),
                  maxPrices[relay][j - 1] - minPrices[i][j - 1]);
          downProfits[i][j] =
              Math.max(
                  Math.max(downProfits[i][j - 1], downProfits[relay][j - 1]),
                  maxPrices[i][j - 1] - minPrices[relay][j - 1]);
        }
      }
    }

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < a.length; ++i) {
      if (result.length() != 0) {
        result.append("\n");
      }

      int lca = findLca(depths, parents, a[i] - 1, b[i] - 1);
      int maxProfit =
          Math.max(
              Math.max(
                  computeUpProfit(depths, parents, upProfits, minPrices, maxPrices, a[i] - 1, lca),
                  computeDownProfit(
                      depths, parents, downProfits, minPrices, maxPrices, lca, b[i] - 1)),
              computeMaxPrice(depths, parents, maxPrices, b[i] - 1, lca)
                  - computeMinPrice(depths, parents, minPrices, a[i] - 1, lca));

      result.append(maxProfit);
    }

    return result.toString();
  }

  static int computeDownProfit(
      int[] depths,
      int[][] parents,
      int[][] downProfits,
      int[][] minPrices,
      int[][] maxPrices,
      int fromNode,
      int toNode) {
    int nodeNum = depths[toNode] - depths[fromNode] + 1;
    if (nodeNum == 0) {
      return 0;
    }

    int log = Integer.toBinaryString(nodeNum).length() - 1;

    int result = downProfits[toNode][log];
    int relay = parents[toNode][log];
    if (relay != -1) {
      result =
          Math.max(
              result,
              computeDownProfit(
                  depths, parents, downProfits, minPrices, maxPrices, fromNode, relay));

      if ((1 << log) != nodeNum) {
        result =
            Math.max(
                result,
                maxPrices[toNode][log]
                    - computeMinPrice(depths, parents, minPrices, relay, fromNode));
      }
    }

    return result;
  }

  static int computeUpProfit(
      int[] depths,
      int[][] parents,
      int[][] upProfits,
      int[][] minPrices,
      int[][] maxPrices,
      int fromNode,
      int toNode) {
    int nodeNum = depths[fromNode] - depths[toNode] + 1;
    if (nodeNum == 0) {
      return 0;
    }

    int log = Integer.toBinaryString(nodeNum).length() - 1;

    int result = upProfits[fromNode][log];
    int relay = parents[fromNode][log];
    if (relay != -1) {
      result =
          Math.max(
              result,
              computeUpProfit(depths, parents, upProfits, minPrices, maxPrices, relay, toNode));

      if ((1 << log) != nodeNum) {
        result =
            Math.max(
                result,
                computeMaxPrice(depths, parents, maxPrices, relay, toNode)
                    - minPrices[fromNode][log]);
      }
    }

    return result;
  }

  static int computeMinPrice(
      int[] depths, int[][] parents, int[][] minPrices, int node1, int node2) {
    int result = Integer.MAX_VALUE;
    int nodeNum = depths[node1] - depths[node2] + 1;
    for (int i = 0; nodeNum != 0; ++i) {
      if (((nodeNum >> i) & 1) == 1) {
        result = Math.min(result, minPrices[node1][i]);
        node1 = parents[node1][i];

        nodeNum -= 1 << i;
      }
    }

    return result;
  }

  static int computeMaxPrice(
      int[] depths, int[][] parents, int[][] maxPrices, int node1, int node2) {
    int result = Integer.MIN_VALUE;
    int nodeNum = depths[node1] - depths[node2] + 1;
    for (int i = 0; nodeNum != 0; ++i) {
      if (((nodeNum >> i) & 1) == 1) {
        result = Math.max(result, maxPrices[node1][i]);
        node1 = parents[node1][i];

        nodeNum -= 1 << i;
      }
    }

    return result;
  }

  static int findLca(int[] depths, int[][] parents, int node1, int node2) {
    if (depths[node1] < depths[node2]) {
      return findLca(depths, parents, node2, node1);
    }

    int depthDiff = depths[node1] - depths[node2];
    for (int i = 0; depthDiff != 0; ++i) {
      if (((depthDiff >> i) & 1) == 1) {
        node1 = parents[node1][i];

        depthDiff -= 1 << i;
      }
    }
    if (node1 == node2) {
      return node1;
    }

    for (int i = parents[0].length - 1; i >= 0; --i) {
      if (parents[node1][i] != parents[node2][i]) {
        node1 = parents[node1][i];
        node2 = parents[node2][i];
      }
    }

    return parents[node1][0];
  }

  static void search(
      List<Integer>[] adjLists, int[] depths, int[][] parents, int depth, int parent, int node) {
    depths[node] = depth;
    parents[node][0] = parent;

    for (int adj : adjLists[node]) {
      if (adj != parent) {
        search(adjLists, depths, parents, depth + 1, node, adj);
      }
    }
  }
}