import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (sc.hasNextInt()) {
      int n = sc.nextInt();
      int[][] relations = new int[n][];
      for (int i = 0; i < n; ++i) {
        String s = sc.next();
        int id = Integer.parseInt(s.substring(0, s.length() - 1));

        s = sc.next();
        int size = Integer.parseInt(s.substring(1, s.length() - 1));

        relations[id] = new int[size];
        for (int j = 0; j < relations[id].length; ++j) {
          relations[id][j] = sc.nextInt();
        }
      }

      System.out.println(solve(relations));
    }

    sc.close();
  }

  static int solve(int[][] relations) {
    int n = relations.length;

    Vertex[] vertices = new Vertex[n];
    for (int i = 0; i < vertices.length; ++i) {
      vertices[i] = new Vertex();
    }
    for (int i = 0; i < relations.length; ++i) {
      for (int r : relations[i]) {
        vertices[i].adjs.add(r);
        vertices[r].adjs.add(i);
      }
    }

    for (int i = 0; i < vertices.length; ++i) {
      if (vertices[i].leftOrRight == null) {
        color(vertices, i, true);
      }
    }

    int matchingCount = 0;
    for (int i = 0; i < vertices.length; ++i) {
      if (vertices[i].leftOrRight
          && vertices[i].matching == -1
          && search(vertices, new boolean[n], i)) {
        ++matchingCount;
      }
    }

    return n - matchingCount;
  }

  static boolean search(Vertex[] vertices, boolean[] visited, int leftIndex) {
    for (int rightIndex : vertices[leftIndex].adjs) {
      if (!visited[rightIndex]) {
        visited[rightIndex] = true;

        if (vertices[rightIndex].matching == -1
            || search(vertices, visited, vertices[rightIndex].matching)) {
          vertices[leftIndex].matching = rightIndex;
          vertices[rightIndex].matching = leftIndex;

          return true;
        }
      }
    }

    return false;
  }

  static void color(Vertex[] vertices, int index, boolean leftOrRight) {
    if (vertices[index].leftOrRight == null) {
      vertices[index].leftOrRight = leftOrRight;

      for (int adj : vertices[index].adjs) {
        color(vertices, adj, !leftOrRight);
      }
    }
  }
}

class Vertex {
  Boolean leftOrRight;
  List<Integer> adjs = new ArrayList<Integer>();
  int matching = -1;
}