// https://www.hankcs.com/program/algorithm/poj-3692-kindergarten-i.html

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int tc = 1;
    while (true) {
      int G = sc.nextInt();
      int B = sc.nextInt();
      int M = sc.nextInt();
      if (G == 0 && B == 0 && M == 0) {
        break;
      }

      int[] X = new int[M];
      int[] Y = new int[M];
      for (int i = 0; i < X.length; ++i) {
        X[i] = sc.nextInt();
        Y[i] = sc.nextInt();
      }

      System.out.println(String.format("Case %d: %d", tc, solve(G, B, X, Y)));

      ++tc;
    }

    sc.close();
  }

  static int solve(int G, int B, int[] X, int[] Y) {
    boolean[][] relations = new boolean[G][B];
    for (int i = 0; i < X.length; ++i) {
      relations[X[i] - 1][Y[i] - 1] = true;
    }

    Vertex[] leftVertices = new Vertex[G];
    for (int i = 0; i < leftVertices.length; ++i) {
      leftVertices[i] = new Vertex();
    }
    Vertex[] rightVertices = new Vertex[B];
    for (int i = 0; i < rightVertices.length; ++i) {
      rightVertices[i] = new Vertex();
    }
    for (int i = 0; i < G; ++i) {
      for (int j = 0; j < B; ++j) {
        if (!relations[i][j]) {
          leftVertices[i].adjs.add(j);
          rightVertices[j].adjs.add(i);
        }
      }
    }

    int matchingCount = 0;
    for (int i = 0; i < leftVertices.length; ++i) {
      if (leftVertices[i].matching == -1
          && search(leftVertices, rightVertices, new boolean[rightVertices.length], i)) {
        ++matchingCount;
      }
    }

    return G + B - matchingCount;
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