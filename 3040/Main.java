import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int C = sc.nextInt();
    int[] V = new int[N];
    int[] B = new int[N];
    for (int i = 0; i < N; ++i) {
      V[i] = sc.nextInt();
      B[i] = sc.nextInt();
    }

    System.out.println(solve(V, B, C));

    sc.close();
  }

  static long solve(final int[] V, int[] B, int C) {
    Integer[] sortedIndices = new Integer[V.length];
    for (int i = 0; i < sortedIndices.length; ++i) {
      sortedIndices[i] = i;
    }
    Arrays.sort(
        sortedIndices,
        new Comparator<Integer>() {
          public int compare(Integer i1, Integer i2) {
            return V[i2] - V[i1];
          }
        });

    long result = 0;
    while (true) {
      int[] needed = new int[V.length];
      int rest = C;
      for (int i = 0; i < needed.length; ++i) {
        needed[i] = Math.min(B[sortedIndices[i]], rest / V[sortedIndices[i]]);
        rest -= needed[i] * V[sortedIndices[i]];
      }

      if (rest != 0) {
        int index = needed.length - 1;
        while (index != -1 && needed[index] == B[sortedIndices[index]]) {
          --index;
        }
        if (index == -1) {
          break;
        }

        ++needed[index];
      }

      int weekNum = Integer.MAX_VALUE;
      for (int i = 0; i < needed.length; ++i) {
        if (needed[i] != 0) {
          weekNum = Math.min(weekNum, B[sortedIndices[i]] / needed[i]);
        }
      }
      for (int i = 0; i < needed.length; ++i) {
        B[sortedIndices[i]] -= needed[i] * weekNum;
      }
      result += weekNum;
    }

    return result;
  }
}
