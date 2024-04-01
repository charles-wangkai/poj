// https://cp-algorithms.com/graph/2SAT.html

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    String[] S = new String[N];
    String[] T = new String[N];
    int[] D = new int[N];
    for (int i = 0; i < N; ++i) {
      S[i] = sc.next();
      T[i] = sc.next();
      D[i] = sc.nextInt();
    }

    System.out.println(solve(S, T, D));

    sc.close();
  }

  static String solve(String[] S, String[] T, int[] D) {
    int N = S.length;

    int[] begins = new int[N];
    for (int i = 0; i < begins.length; ++i) {
      begins[i] = toMinutes(S[i]);
    }

    int[] ends = new int[N];
    for (int i = 0; i < ends.length; ++i) {
      ends[i] = toMinutes(T[i]);
    }

    boolean[][] adjs = new boolean[2 * N][2 * N];
    for (int i = 0; i < N; ++i) {
      for (int j = i + 1; j < N; ++j) {
        if (hasConflict(begins[i], begins[i] + D[i], begins[j], begins[j] + D[j])) {
          addEdges(adjs, 2 * i + 1, 2 * j + 1);
        }
        if (hasConflict(begins[i], begins[i] + D[i], ends[j] - D[j], ends[j])) {
          addEdges(adjs, 2 * i + 1, 2 * j);
        }
        if (hasConflict(ends[i] - D[i], ends[i], begins[j], begins[j] + D[j])) {
          addEdges(adjs, 2 * i, 2 * j + 1);
        }
        if (hasConflict(ends[i] - D[i], ends[i], ends[j] - D[j], ends[j])) {
          addEdges(adjs, 2 * i, 2 * j);
        }
      }
    }

    List<Integer> sortedNodes = new ArrayList<Integer>();
    boolean[] visited = new boolean[2 * N];
    for (int i = 0; i < visited.length; ++i) {
      if (!visited[i]) {
        search1(adjs, sortedNodes, visited, i);
      }
    }
    Collections.reverse(sortedNodes);

    boolean[][] reversedAdjs = new boolean[2 * N][2 * N];
    for (int i = 0; i < 2 * N; ++i) {
      for (int j = 0; j < 2 * N; ++j) {
        if (adjs[i][j]) {
          reversedAdjs[j][i] = true;
        }
      }
    }

    int[] components = new int[2 * N];
    int component = 0;
    for (int node : sortedNodes) {
      if (components[node] == 0) {
        ++component;
        search2(reversedAdjs, components, node, component);
      }
    }

    StringBuilder result = new StringBuilder("YES");
    for (int i = 0; i < N; ++i) {
      if (components[2 * i] == components[2 * i + 1]) {
        return "NO";
      }

      String beginTime;
      String endTime;
      if (components[2 * i] > components[2 * i + 1]) {
        beginTime = toTime(begins[i]);
        endTime = toTime(begins[i] + D[i]);
      } else {
        beginTime = toTime(ends[i] - D[i]);
        endTime = toTime(ends[i]);
      }

      result.append(String.format("\n%s %s", beginTime, endTime));
    }

    return result.toString();
  }

  static void search2(boolean[][] reversedAdjs, int[] components, int node, int component) {
    components[node] = component;

    for (int i = 0; i < reversedAdjs[node].length; ++i) {
      if (reversedAdjs[node][i] && components[i] == 0) {
        search2(reversedAdjs, components, i, component);
      }
    }
  }

  static void search1(boolean[][] adjs, List<Integer> sortedNodes, boolean[] visited, int node) {
    visited[node] = true;

    for (int i = 0; i < adjs[node].length; ++i) {
      if (adjs[node][i] && !visited[i]) {
        search1(adjs, sortedNodes, visited, i);
      }
    }

    sortedNodes.add(node);
  }

  static void addEdges(boolean[][] adjs, int index1, int index2) {
    adjs[index1 ^ 1][index2] = true;
    adjs[index2 ^ 1][index1] = true;
  }

  static boolean hasConflict(int begin1, int end1, int begin2, int end2) {
    return !(begin2 >= end1 || begin1 >= end2);
  }

  static int toMinutes(String s) {
    return Integer.parseInt(s.substring(0, 2)) * 60 + Integer.parseInt(s.substring(3));
  }

  static String toTime(int minutes) {
    return String.format("%02d:%02d", minutes / 60, minutes % 60);
  }
}