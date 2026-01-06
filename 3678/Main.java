// https://www.hankcs.com/program/algorithm/poj-3678-katu-puzzle.html
// https://cp-algorithms.com/graph/2SAT.html

import java.util.ArrayList;
import java.util.Arrays;
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
    TwoSat twoSat = new TwoSat(N);

    for (int i = 0; i < a.length; ++i) {
      if (op[i].equals("AND")) {
        if (c[i] == 1) {
          twoSat.addClause(a[i], true, a[i], true);
          twoSat.addClause(b[i], true, b[i], true);
        } else {
          twoSat.addClause(a[i], false, b[i], false);
        }
      } else if (op[i].equals("OR")) {
        if (c[i] == 1) {
          twoSat.addClause(a[i], true, b[i], true);
        } else {
          twoSat.addClause(a[i], false, a[i], false);
          twoSat.addClause(b[i], false, b[i], false);
        }
      } else if (c[i] == 1) {
        twoSat.addClause(a[i], false, b[i], false);
        twoSat.addClause(a[i], true, b[i], true);
      } else {
        twoSat.addClause(a[i], false, b[i], true);
        twoSat.addClause(a[i], true, b[i], false);
      }
    }

    return twoSat.findAssignment() != null;
  }
}

class TwoSat {
  int n;
  Scc scc;

  TwoSat(int n) {
    this.n = n;
    scc = new Scc(2 * n);
  }

  void addClause(int i, boolean f, int j, boolean g) {
    scc.addEdge(2 * i + (f ? 0 : 1), 2 * j + (g ? 1 : 0));
    scc.addEdge(2 * j + (g ? 0 : 1), 2 * i + (f ? 1 : 0));
  }

  boolean[] findAssignment() {
    int[] components = scc.buildComponents();

    boolean[] assignment = new boolean[n];
    for (int i = 0; i < n; ++i) {
      if (components[2 * i] == components[2 * i + 1]) {
        return null;
      }

      assignment[i] = components[2 * i] < components[2 * i + 1];
    }

    return assignment;
  }
}

class Scc {
  List<Integer>[] adjLists;
  List<Integer>[] reversedAdjLists;

  @SuppressWarnings("unchecked")
  Scc(int n) {
    adjLists = new List[n];
    for (int i = 0; i < adjLists.length; ++i) {
      adjLists[i] = new ArrayList<Integer>();
    }

    reversedAdjLists = new List[n];
    for (int i = 0; i < reversedAdjLists.length; ++i) {
      reversedAdjLists[i] = new ArrayList<Integer>();
    }
  }

  void addEdge(int from, int to) {
    adjLists[from].add(to);
    reversedAdjLists[to].add(from);
  }

  List<Integer> topologicalSort() {
    int n = adjLists.length;

    List<Integer> sorted = new ArrayList<Integer>();
    boolean[] visited = new boolean[n];
    for (int i = 0; i < n; ++i) {
      if (!visited[i]) {
        search1(sorted, visited, i);
      }
    }
    Collections.reverse(sorted);

    return sorted;
  }

  private void search1(List<Integer> sorted, boolean[] visited, int node) {
    visited[node] = true;

    for (int adj : adjLists[node]) {
      if (!visited[adj]) {
        search1(sorted, visited, adj);
      }
    }

    sorted.add(node);
  }

  int[] buildComponents() {
    int n = adjLists.length;

    List<Integer> sorted = topologicalSort();

    int[] components = new int[n];
    Arrays.fill(components, -1);
    int component = 0;
    for (int node : sorted) {
      if (components[node] == -1) {
        search2(components, node, component);
        ++component;
      }
    }

    return components;
  }

  private void search2(int[] components, int node, int component) {
    components[node] = component;

    for (int adj : reversedAdjLists[node]) {
      if (components[adj] == -1) {
        search2(components, adj, component);
      }
    }
  }
}
