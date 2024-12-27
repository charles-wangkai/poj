// https://www.hankcs.com/program/algorithm/poj-3180-the-cow-prom.html

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int M = sc.nextInt();
    int[] A = new int[M];
    int[] B = new int[M];
    for (int i = 0; i < M; ++i) {
      A[i] = sc.nextInt();
      B[i] = sc.nextInt();
    }

    System.out.println(solve(N, A, B));

    sc.close();
  }

  static int solve(int N, int[] A, int[] B) {
    @SuppressWarnings("unchecked")
    List<Integer>[] adjLists = new List[N];
    for (int i = 0; i < adjLists.length; ++i) {
      adjLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < A.length; ++i) {
      adjLists[A[i] - 1].add(B[i] - 1);
    }

    List<Integer> sortedNodes = new ArrayList<Integer>();
    boolean[] visited = new boolean[N];
    for (int i = 0; i < visited.length; ++i) {
      if (!visited[i]) {
        search1(adjLists, sortedNodes, visited, i);
      }
    }
    Collections.reverse(sortedNodes);

    @SuppressWarnings("unchecked")
    List<Integer>[] reversedAdjLists = new List[N];
    for (int i = 0; i < reversedAdjLists.length; ++i) {
      reversedAdjLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < A.length; ++i) {
      reversedAdjLists[B[i] - 1].add(A[i] - 1);
    }

    int[] components = new int[N];
    int component = 0;
    for (int node : sortedNodes) {
      if (components[node] == 0) {
        ++component;
        search2(reversedAdjLists, components, node, component);
      }
    }

    Map<Integer, Integer> componentToCount = new HashMap<Integer, Integer>();
    for (int c : components) {
      if (!componentToCount.containsKey(c)) {
        componentToCount.put(c, 0);
      }
      componentToCount.put(c, componentToCount.get(c) + 1);
    }

    int result = 0;
    for (int count : componentToCount.values()) {
      if (count != 1) {
        ++result;
      }
    }

    return result;
  }

  static void search2(List<Integer>[] reversedAdjLists, int[] components, int node, int component) {
    components[node] = component;

    for (int adj : reversedAdjLists[node]) {
      if (components[adj] == 0) {
        search2(reversedAdjLists, components, adj, component);
      }
    }
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