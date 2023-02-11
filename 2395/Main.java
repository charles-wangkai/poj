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
    int[] L = new int[M];
    for (int i = 0; i < M; ++i) {
      A[i] = sc.nextInt() - 1;
      B[i] = sc.nextInt() - 1;
      L[i] = sc.nextInt();
    }

    System.out.println(solve(N, A, B, L));

    sc.close();
  }

  static int solve(int N, int[] A, int[] B, final int[] L) {
    Integer[] sortedIndices = new Integer[A.length];
    for (int i = 0; i < sortedIndices.length; ++i) {
      sortedIndices[i] = i;
    }
    Arrays.sort(
        sortedIndices,
        new Comparator<Integer>() {
          public int compare(Integer i1, Integer i2) {
            return L[i1] - L[i2];
          }
        });

    int result = -1;
    int[] parents = new int[N];
    Arrays.fill(parents, -1);
    for (int index : sortedIndices) {
      int root1 = findRoot(parents, A[index]);
      int root2 = findRoot(parents, B[index]);
      if (root1 != root2) {
        parents[root2] = root1;
        result = L[index];
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
