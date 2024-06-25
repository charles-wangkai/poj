import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int n = sc.nextInt();
    int[] A = new int[n];
    for (int i = 0; i < A.length; ++i) {
      A[i] = sc.nextInt();
    }

    System.out.println(solve(A));

    sc.close();
  }

  static String solve(int[] A) {
    int length1 = computeLength1(A);

    int[] a = new int[A.length - length1];
    for (int i = 0; i < a.length; ++i) {
      a[i] = A[i + length1];
    }
    int length2 = computeLength2(a);

    reverse(A, 0, length1 - 1);
    reverse(A, length1, length1 + length2 - 1);
    reverse(A, length1 + length2, A.length - 1);

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < A.length; ++i) {
      if (i != 0) {
        result.append("\n");
      }
      result.append(A[i]);
    }

    return result.toString();
  }

  static int computeLength1(int[] A) {
    int[] reversed = A.clone();
    reverse(reversed, 0, reversed.length - 1);

    int[] suffixArray = buildSuffixArray(reversed);

    for (int i = 0; ; ++i) {
      int length1 = A.length - suffixArray[i];
      if (length1 >= 1 && A.length - length1 >= 2) {
        return length1;
      }
    }
  }

  static int computeLength2(int[] a) {
    int[] doubleReversed = new int[a.length * 2];
    for (int i = 0; i < doubleReversed.length; ++i) {
      doubleReversed[i] = a[i % a.length];
    }
    reverse(doubleReversed, 0, doubleReversed.length - 1);

    int[] suffixArray = buildSuffixArray(doubleReversed);

    for (int i = 0; ; ++i) {
      int length2 = a.length - suffixArray[i];
      if (length2 >= 1 && length2 < a.length) {
        return length2;
      }
    }
  }

  static int[] buildSuffixArray(int[] values) {
    int n = values.length;

    Integer[] suffixArray = new Integer[n + 1];
    final int[] ranks = new int[n + 1];
    for (int i = 0; i <= n; ++i) {
      suffixArray[i] = i;
      ranks[i] = (i == n) ? -1 : values[i];
    }

    for (int k = 1; k <= n; k *= 2) {
      final int k_ = k;
      Comparator<Integer> comparator =
          new Comparator<Integer>() {
            public int compare(Integer i1, Integer i2) {
              if (ranks[i1] != ranks[i2]) {
                return ranks[i1] - ranks[i2];
              }

              return ((i1 + k_ < ranks.length) ? ranks[i1 + k_] : -1)
                  - ((i2 + k_ < ranks.length) ? ranks[i2 + k_] : -1);
            }
          };
      Arrays.sort(suffixArray, comparator);

      int[] nextRanks = new int[ranks.length];
      for (int i = 1; i <= n; ++i) {
        nextRanks[suffixArray[i]] =
            nextRanks[suffixArray[i - 1]]
                + ((comparator.compare(suffixArray[i - 1], suffixArray[i]) == 0) ? 0 : 1);
      }

      for (int i = 0; i < ranks.length; ++i) {
        ranks[i] = nextRanks[i];
      }
    }

    int[] result = new int[suffixArray.length];
    for (int i = 0; i < result.length; ++i) {
      result[i] = suffixArray[i];
    }

    return result;
  }

  static void reverse(int[] A, int beginIndex, int endIndex) {
    for (int i = beginIndex, j = endIndex; i < j; ++i, --j) {
      int temp = A[i];
      A[i] = A[j];
      A[j] = temp;
    }
  }
}