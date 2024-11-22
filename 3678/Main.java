// https://www.hankcs.com/program/algorithm/poj-3678-katu-puzzle.html
// https://cp-algorithms.com/graph/2SAT.html

import java.util.ArrayList;
import java.util.Collections;
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
    String[] op = new String[M];
    for (int i = 0; i < M; ++i) {
      a[i] = sc.nextInt();
      b[i] = sc.nextInt();
      c[i] = sc.nextInt();
      op[i] = sc.next();
    }

    System.out.println(solve(N, a, b, c, op) ? "YES" : "NO");

    sc.close();
  }

  static boolean solve(int N, int[] a, int[] b, int[] c, String[] op) {
    @SuppressWarnings("unchecked")
    List<Integer>[] adjLists = new List[N * 2];
    for (int i = 0; i < adjLists.length; ++i) {
      adjLists[i] = new ArrayList<Integer>();
    }

    @SuppressWarnings("unchecked")
    List<Integer>[] reversedAdjLists = new List[N * 2];
    for (int i = 0; i < reversedAdjLists.length; ++i) {
      reversedAdjLists[i] = new ArrayList<Integer>();
    }

    for (int i = 0; i < a.length; ++i) {
      if (op[i].equals("AND")) {
        if (c[i] == 1) {
          addEdge(adjLists, reversedAdjLists, a[i] * 2 + 1, a[i] * 2);
          addEdge(adjLists, reversedAdjLists, b[i] * 2 + 1, b[i] * 2);
        } else {
          addEdge(adjLists, reversedAdjLists, a[i] * 2, b[i] * 2 + 1);
          addEdge(adjLists, reversedAdjLists, b[i] * 2, a[i] * 2 + 1);
        }
      } else if (op[i].equals("OR")) {
        if (c[i] == 1) {
          addEdge(adjLists, reversedAdjLists, a[i] * 2 + 1, b[i] * 2);
          addEdge(adjLists, reversedAdjLists, b[i] * 2 + 1, a[i] * 2);
        } else {
          addEdge(adjLists, reversedAdjLists, a[i] * 2, a[i] * 2 + 1);
          addEdge(adjLists, reversedAdjLists, b[i] * 2, b[i] * 2 + 1);
        }
      } else if (c[i] == 1) {
        addEdge(adjLists, reversedAdjLists, a[i] * 2, b[i] * 2 + 1);
        addEdge(adjLists, reversedAdjLists, b[i] * 2 + 1, a[i] * 2);
        addEdge(adjLists, reversedAdjLists, a[i] * 2 + 1, b[i] * 2);
        addEdge(adjLists, reversedAdjLists, b[i] * 2, a[i] * 2 + 1);
      } else {
        addEdge(adjLists, reversedAdjLists, a[i] * 2, b[i] * 2);
        addEdge(adjLists, reversedAdjLists, b[i] * 2, a[i] * 2);
        addEdge(adjLists, reversedAdjLists, a[i] * 2 + 1, b[i] * 2 + 1);
        addEdge(adjLists, reversedAdjLists, b[i] * 2 + 1, a[i] * 2 + 1);
      }
    }

    List<Integer> sortedNodes = new ArrayList<Integer>();
    boolean[] visited = new boolean[N * 2];
    for (int i = 0; i < visited.length; ++i) {
      if (!visited[i]) {
        search1(adjLists, sortedNodes, visited, i);
      }
    }
    Collections.reverse(sortedNodes);

    int k = 0;
    int[] components = new int[N * 2];
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
      List<Integer>[] adjLists, List<Integer>[] reversedAdjLists, int from, int to) {
    adjLists[from].add(to);
    reversedAdjLists[to].add(from);
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