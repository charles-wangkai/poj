import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (sc.hasNextInt()) {
      int N = sc.nextInt();
      int[][] distances = new int[N][N];
      for (int i = 0; i < N; ++i) {
        for (int j = 0; j < N; ++j) {
          distances[i][j] = sc.nextInt();
        }
      }

      System.out.println(solve(distances));
    }

    sc.close();
  }

  static int solve(final int[][] distances) {
    int N = distances.length;

    List<Pair> pairs = new ArrayList<Pair>();
    for (int i = 0; i < N; ++i) {
      for (int j = i + 1; j < N; ++j) {
        pairs.add(new Pair(i, j));
      }
    }
    Collections.sort(
        pairs,
        new Comparator<Pair>() {
          public int compare(Pair p1, Pair p2) {
            return distances[p1.node1][p1.node2] - distances[p2.node1][p2.node2];
          }
        });

    int result = 0;
    int[] parents = new int[N];
    Arrays.fill(parents, -1);
    for (Pair pair : pairs) {
      int root1 = findRoot(parents, pair.node1);
      int root2 = findRoot(parents, pair.node2);
      if (root1 != root2) {
        result += distances[pair.node1][pair.node2];
        parents[root2] = root1;
      }
    }

    return result;
  }

  static int findRoot(int[] parents, int node) {
    if (parents[node] == -1) {
      return node;
    }

    parents[node] = findRoot(parents, parents[node]);

    return parents[node];
  }
}

class Pair {
  int node1;
  int node2;

  Pair(int node1, int node2) {
    this.node1 = node1;
    this.node2 = node2;
  }
}
