import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int[] T = new int[N];
    int[] D = new int[N];
    for (int i = 0; i < N; ++i) {
      T[i] = sc.nextInt();
      D[i] = sc.nextInt();
    }

    System.out.println(solve(T, D));

    sc.close();
  }

  static long solve(final int[] T, final int[] D) {
    Integer[] sortedIndices = new Integer[T.length];
    for (int i = 0; i < sortedIndices.length; ++i) {
      sortedIndices[i] = i;
    }
    Arrays.sort(
        sortedIndices,
        new Comparator<Integer>() {
          public int compare(Integer i1, Integer i2) {
            return T[i1] * D[i2] - T[i2] * D[i1];
          }
        });

    long result = 0;
    int rest = 0;
    for (int d : D) {
      rest += d;
    }
    for (int index : sortedIndices) {
      rest -= D[index];
      result += 2L * T[index] * rest;
    }

    return result;
  }
}
