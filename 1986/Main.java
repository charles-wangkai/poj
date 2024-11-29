import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int M = sc.nextInt();
    int[] F1 = new int[M];
    int[] F2 = new int[M];
    int[] L = new int[M];
    char[] D = new char[M];
    for (int i = 0; i < M; ++i) {
      F1[i] = sc.nextInt();
      F2[i] = sc.nextInt();
      L[i] = sc.nextInt();
      D[i] = sc.next().charAt(0);
    }
    int K = sc.nextInt();
    int[] farm1s = new int[K];
    int[] farm2s = new int[K];
    for (int i = 0; i < K; ++i) {
      farm1s[i] = sc.nextInt();
      farm2s[i] = sc.nextInt();
    }

    System.out.println(solve(N, F1, F2, L, D, farm1s, farm2s));

    sc.close();
  }

  static String solve(int N, int[] F1, int[] F2, int[] L, char[] D, int[] farm1s, int[] farm2s) {
    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[N];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < F1.length; ++i) {
      edgeLists[F1[i] - 1].add(i);
      edgeLists[F2[i] - 1].add(i);
    }

    final int[] heights = new int[N];
    int[][] parents = new int[N][Integer.toBinaryString(N).length()];
    int[] distances = new int[N];
    search(F1, F2, L, edgeLists, heights, parents, distances, 0, -1, 0, 0);

    Integer[] sortedNodes = new Integer[N];
    for (int i = 0; i < sortedNodes.length; ++i) {
      sortedNodes[i] = i;
    }
    Arrays.sort(
        sortedNodes,
        new Comparator<Integer>() {
          public int compare(Integer i1, Integer i2) {
            return heights[i1] - heights[i2];
          }
        });

    for (int node : sortedNodes) {
      for (int i = 1; i < parents[node].length; ++i) {
        parents[node][i] = (parents[node][i - 1] == -1) ? -1 : parents[parents[node][i - 1]][i - 1];
      }
    }

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < farm1s.length; ++i) {
      if (result.length() != 0) {
        result.append("\n");
      }

      result.append(
          distances[farm1s[i] - 1]
              + distances[farm2s[i] - 1]
              - 2 * distances[findLca(heights, parents, farm1s[i] - 1, farm2s[i] - 1)]);
    }

    return result.toString();
  }

  static int findLca(int[] heights, int[][] parents, int node1, int node2) {
    if (heights[node1] < heights[node2]) {
      return findLca(heights, parents, node2, node1);
    }

    int diff = heights[node1] - heights[node2];
    for (int i = 0; diff != 0; ++i) {
      if (((diff >> i) & 1) == 1) {
        node1 = parents[node1][i];

        diff -= 1 << i;
      }
    }
    if (node1 == node2) {
      return node1;
    }

    while (true) {
      int index = -1;
      while (parents[node1][index + 1] != parents[node2][index + 1]) {
        ++index;
      }
      if (index == -1) {
        break;
      }

      node1 = parents[node1][index];
      node2 = parents[node2][index];
    }

    return parents[node1][0];
  }

  static void search(
      int[] F1,
      int[] F2,
      int[] L,
      List<Integer>[] edgeLists,
      int[] heights,
      int[][] parents,
      int[] distances,
      int height,
      int parent,
      int distance,
      int node) {
    heights[node] = height;
    parents[node][0] = parent;
    distances[node] = distance;

    for (int edge : edgeLists[node]) {
      int other = (F1[edge] - 1 == node) ? (F2[edge] - 1) : (F1[edge] - 1);
      if (other != parent) {
        search(
            F1,
            F2,
            L,
            edgeLists,
            heights,
            parents,
            distances,
            height + 1,
            node,
            distance + L[edge],
            other);
      }
    }
  }
}