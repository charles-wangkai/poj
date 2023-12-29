import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (sc.hasNextInt()) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      int[][] preferences = new int[N][];
      for (int i = 0; i < preferences.length; ++i) {
        int Si = sc.nextInt();
        preferences[i] = new int[Si];
        for (int j = 0; j < preferences[i].length; ++j) {
          preferences[i][j] = sc.nextInt();
        }
      }

      System.out.println(solve(preferences, M));
    }

    sc.close();
  }

  static int solve(int[][] preferences, int M) {
    int N = preferences.length;

    Vertex[] leftVertices = new Vertex[N];
    for (int i = 0; i < leftVertices.length; ++i) {
      leftVertices[i] = new Vertex();
    }
    Vertex[] rightVertices = new Vertex[M];
    for (int i = 0; i < rightVertices.length; ++i) {
      rightVertices[i] = new Vertex();
    }
    for (int i = 0; i < preferences.length; ++i) {
      for (int j : preferences[i]) {
        leftVertices[i].adjs.add(j - 1);
        rightVertices[j - 1].adjs.add(i);
      }
    }

    int matchingCount = 0;
    for (int i = 0; i < leftVertices.length; ++i) {
      if (leftVertices[i].matching == -1
          && search(leftVertices, rightVertices, new boolean[rightVertices.length], i)) {
        ++matchingCount;
      }
    }

    return matchingCount;
  }

  static boolean search(
      Vertex[] leftVertices, Vertex[] rightVertices, boolean[] rightVisited, int leftIndex) {
    for (int rightIndex : leftVertices[leftIndex].adjs) {
      if (!rightVisited[rightIndex]) {
        rightVisited[rightIndex] = true;

        if (rightVertices[rightIndex].matching == -1
            || search(
                leftVertices, rightVertices, rightVisited, rightVertices[rightIndex].matching)) {
          leftVertices[leftIndex].matching = rightIndex;
          rightVertices[rightIndex].matching = leftIndex;

          return true;
        }
      }
    }

    return false;
  }
}

class Vertex {
  List<Integer> adjs = new ArrayList<Integer>();
  int matching = -1;
}