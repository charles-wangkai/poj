import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int M = sc.nextInt();
    int[] A = new int[M];
    int[] B = new int[M];
    int[] C = new int[M];
    for (int i = 0; i < M; ++i) {
      A[i] = sc.nextInt() - 1;
      B[i] = sc.nextInt() - 1;
      C[i] = sc.nextInt();
    }

    System.out.println(solve(N, A, B, C));

    sc.close();
  }

  static int solve(int N, int[] A, int[] B, final int[] C) {
    int result = 0;
    int usedCount = 0;
    int[] parents = new int[N];
    Arrays.fill(parents, -1);

    Integer[] sortedIndices = new Integer[A.length];
    for (int i = 0; i < sortedIndices.length; ++i) {
      sortedIndices[i] = i;
    }
    Arrays.sort(
        sortedIndices,
        new Comparator<Integer>() {
          public int compare(Integer i1, Integer i2) {
            return C[i2] - C[i1];
          }
        });

    for (int index : sortedIndices) {
      int aRoot = findRoot(parents, A[index]);
      int bRoot = findRoot(parents, B[index]);
      if (aRoot != bRoot) {
        result += C[index];
        ++usedCount;
        parents[bRoot] = aRoot;
      }
    }

    return (usedCount == N - 1) ? result : -1;
  }

  static int findRoot(int[] parents, int node) {
    int root = node;
    while (parents[root] != -1) {
      root = parents[root];
    }

    int p = node;
    while (p != root) {
      int next = parents[p];
      parents[p] = root;

      p = next;
    }

    return root;
  }
}
