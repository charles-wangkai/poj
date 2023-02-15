import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int n = sc.nextInt();
    int[] A = new int[n];
    int[] B = new int[n];
    int[] C = new int[n];
    int[] D = new int[n];
    for (int i = 0; i < n; ++i) {
      A[i] = sc.nextInt();
      B[i] = sc.nextInt();
      C[i] = sc.nextInt();
      D[i] = sc.nextInt();
    }

    System.out.println(solve(A, B, C, D));

    sc.close();
  }

  static long solve(int[] A, int[] B, int[] C, int[] D) {
    int[] cdSums = new int[C.length * D.length];
    for (int i = 0; i < cdSums.length; ++i) {
      cdSums[i] = C[i / D.length] + D[i % D.length];
    }
    Arrays.sort(cdSums);

    long result = 0;
    for (int a : A) {
      for (int b : B) {
        result += computeNum(cdSums, -(a + b));
      }
    }

    return result;
  }

  static int computeNum(int[] cdSums, int target) {
    return findLessEqualIndex(cdSums, target) - findGreaterEqualIndex(cdSums, target) + 1;
  }

  static int findLessEqualIndex(int[] a, int target) {
    int result = -1;
    int lower = 0;
    int upper = a.length - 1;
    while (lower <= upper) {
      int middle = (lower + upper) / 2;
      if (a[middle] <= target) {
        result = middle;
        lower = middle + 1;
      } else {
        upper = middle - 1;
      }
    }

    return result;
  }

  static int findGreaterEqualIndex(int[] a, int target) {
    int result = a.length;
    int lower = 0;
    int upper = a.length - 1;
    while (lower <= upper) {
      int middle = (lower + upper) / 2;
      if (a[middle] >= target) {
        result = middle;
        upper = middle - 1;
      } else {
        lower = middle + 1;
      }
    }

    return result;
  }
}
