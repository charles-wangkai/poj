import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int K = sc.nextInt();
    int[] R = new int[K];
    int[] C = new int[K];
    for (int i = 0; i < K; ++i) {
      R[i] = sc.nextInt() - 1;
      C[i] = sc.nextInt() - 1;
    }

    System.out.println(solve(N, R, C));

    sc.close();
  }

  static int solve(int N, int[] R, int[] C) {
    Vertex[] leftVertices = new Vertex[N];
    for (int i = 0; i < leftVertices.length; ++i) {
      leftVertices[i] = new Vertex();
    }
    Vertex[] rightVertices = new Vertex[N];
    for (int i = 0; i < rightVertices.length; ++i) {
      rightVertices[i] = new Vertex();
    }
    for (int i = 0; i < R.length; ++i) {
      leftVertices[R[i]].adjacents.add(C[i]);
      rightVertices[C[i]].adjacents.add(R[i]);
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
    for (int rightIndex : leftVertices[leftIndex].adjacents) {
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
  List<Integer> adjacents = new ArrayList<Integer>();
  int matching = -1;
}
