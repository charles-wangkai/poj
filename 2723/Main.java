// https://cp-algorithms.com/graph/2SAT.html

import java.util.ArrayList;
import java.util.Collections;
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

      int[][] keys = new int[N][2];
      for (int i = 0; i < keys.length; ++i) {
        for (int j = 0; j < keys[i].length; ++j) {
          keys[i][j] = sc.nextInt();
        }
      }
      int[][] locks = new int[M][2];
      for (int i = 0; i < locks.length; ++i) {
        for (int j = 0; j < locks[i].length; ++j) {
          locks[i][j] = sc.nextInt();
        }
      }

      System.out.println(solve(keys, locks));
    }

    sc.close();
  }

  static int solve(int[][] keys, int[][] locks) {
    int result = -1;
    int lower = 0;
    int upper = locks.length;
    while (lower <= upper) {
      int middle = (lower + upper) / 2;
      if (check(keys, locks, middle)) {
        result = middle;
        lower = middle + 1;
      } else {
        upper = middle - 1;
      }
    }

    return result;
  }

  static boolean check(int[][] keys, int[][] locks, int doorNum) {
    int N = keys.length;

    @SuppressWarnings("unchecked")
    List<Integer>[] adjLists = new List[N * 4];
    for (int i = 0; i < adjLists.length; ++i) {
      adjLists[i] = new ArrayList<Integer>();
    }

    @SuppressWarnings("unchecked")
    List<Integer>[] reversedAdjLists = new List[N * 4];
    for (int i = 0; i < reversedAdjLists.length; ++i) {
      reversedAdjLists[i] = new ArrayList<Integer>();
    }

    for (int[] key : keys) {
      addEdge(adjLists, reversedAdjLists, key[0] * 2 + 1, key[1] * 2 + 1);
    }
    for (int i = 0; i < doorNum; ++i) {
      addEdge(adjLists, reversedAdjLists, locks[i][0] * 2, locks[i][1] * 2);
    }

    List<Integer> sortedNodes = new ArrayList<Integer>();
    boolean[] visited = new boolean[N * 4];
    for (int i = 0; i < visited.length; ++i) {
      if (!visited[i]) {
        search1(adjLists, sortedNodes, visited, i);
      }
    }
    Collections.reverse(sortedNodes);

    int k = 0;
    int[] components = new int[N * 4];
    for (int node : sortedNodes) {
      if (components[node] == 0) {
        ++k;
        search2(reversedAdjLists, components, node, k);
      }
    }

    for (int i = 0; i < N; ++i) {
      if (components[i * 2] == components[i * 2 + 1]) {
        return false;
      }
    }

    return true;
  }

  static void search2(List<Integer>[] reversedAdjLists, int[] components, int node, int k) {
    components[node] = k;

    for (int adj : reversedAdjLists[node]) {
      if (components[adj] == 0) {
        search2(reversedAdjLists, components, adj, k);
      }
    }
  }

  static void addEdge(
      List<Integer>[] adjLists, List<Integer>[] reversedAdjLists, int index1, int index2) {
    adjLists[index1 ^ 1].add(index2);
    reversedAdjLists[index2].add(index1 ^ 1);

    adjLists[index2 ^ 1].add(index1);
    reversedAdjLists[index1].add(index2 ^ 1);
  }

  static void search1(
      List<Integer>[] adjLists, List<Integer> sortedNodes, boolean[] visited, int node) {
    visited[node] = true;

    for (int adj : adjLists[node]) {
      if (!visited[adj]) {
        search1(adjLists, sortedNodes, visited, adj);
      }
    }

    sortedNodes.add(node);
  }
}