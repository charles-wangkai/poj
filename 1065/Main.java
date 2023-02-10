import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int n = sc.nextInt();
      int[] l = new int[n];
      int[] w = new int[n];
      for (int i = 0; i < n; ++i) {
        l[i] = sc.nextInt();
        w[i] = sc.nextInt();
      }

      System.out.println(solve(l, w));
    }

    sc.close();
  }

  static int solve(final int[] l, final int[] w) {
    Integer[] sortedIndices = new Integer[l.length];
    for (int i = 0; i < sortedIndices.length; ++i) {
      sortedIndices[i] = i;
    }
    Arrays.sort(
        sortedIndices,
        new Comparator<Integer>() {
          public int compare(Integer i1, Integer i2) {
            return (l[i1] != l[i2]) ? (l[i1] - l[i2]) : (w[i1] - w[i2]);
          }
        });

    int[] sortedW = new int[sortedIndices.length];
    for (int i = 0; i < sortedW.length; ++i) {
      sortedW[i] = w[sortedIndices[i]];
    }

    return computeLongestDecreasingLength(sortedW);
  }

  static int computeLongestDecreasingLength(int[] a) {
    List<Integer> sequence = new ArrayList<Integer>();
    for (int ai : a) {
      int index = Collections.binarySearch(sequence, ai, Collections.reverseOrder());
      if (index < 0) {
        index = -1 - index;
      }

      if (index == sequence.size()) {
        sequence.add(ai);
      } else {
        sequence.set(index, ai);
      }
    }

    return sequence.size();
  }
}
