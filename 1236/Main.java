import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    @SuppressWarnings("unchecked")
    List<Integer>[] receivers = new List[N];
    for (int i = 0; i < receivers.length; ++i) {
      receivers[i] = new ArrayList<Integer>();
      while (true) {
        int receiver = sc.nextInt();
        if (receiver == 0) {
          break;
        }

        receivers[i].add(receiver);
      }
    }

    System.out.println(solve(receivers));

    sc.close();
  }

  static String solve(List<Integer>[] receivers) {
    int N = receivers.length;

    @SuppressWarnings("unchecked")
    List<Integer>[] adjLists = new List[N];
    for (int i = 0; i < adjLists.length; ++i) {
      adjLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < receivers.length; ++i) {
      for (int receiver : receivers[i]) {
        adjLists[i].add(receiver - 1);
      }
    }

    int solutionA = solveTaskA(adjLists);
    int solutionB = solveTaskB(adjLists);

    return String.format("%d\n%d", solutionA, solutionB);
  }

  static int solveTaskA(List<Integer>[] adjLists) {
    int N = adjLists.length;

    List<Integer> sortedNodes = new ArrayList<Integer>();
    boolean[] visited = new boolean[N];
    for (int i = 0; i < visited.length; ++i) {
      if (!visited[i]) {
        search1(adjLists, sortedNodes, visited, i);
      }
    }
    Collections.reverse(sortedNodes);

    int result = 0;
    Arrays.fill(visited, false);
    for (int node : sortedNodes) {
      if (!visited[node]) {
        search3(adjLists, visited, node);
        ++result;
      }
    }

    return result;
  }

  static int solveTaskB(List<Integer>[] adjLists) {
    int N = adjLists.length;

    int result = 0;
    while (true) {
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
      for (int i = 0; i < adjLists.length; ++i) {
        for (int adj : adjLists[i]) {
          reversedAdjLists[adj].add(i);
        }
      }

      int[] components = new int[N];
      int first = -1;
      int last = -1;
      int component = 0;
      for (int node : sortedNodes) {
        if (components[node] == 0) {
          if (first == -1) {
            first = node;
          }
          last = node;

          ++component;
          search2(reversedAdjLists, components, node, component);
        }
      }

      if (component == 1) {
        break;
      }

      adjLists[last].add(first);
      ++result;
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

  static void search3(List<Integer>[] adjLists, boolean[] visited, int node) {
    visited[node] = true;

    for (int adj : adjLists[node]) {
      if (!visited[adj]) {
        search3(adjLists, visited, adj);
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