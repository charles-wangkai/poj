// https://blog.csdn.net/neweryyy/article/details/109907238
// https://www.hankcs.com/program/algorithm/poj-1509-glass-beads.html

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    for (int tc = 0; tc < N; ++tc) {
      String s = sc.next();

      System.out.println(solve(s));
    }

    sc.close();
  }

  static int solve(String s) {
    String combined = s + s + (char) ('z' + 1);

    int[] suffixArray = buildSuffixArray(combined);

    for (int i = 0; ; ++i) {
      if (suffixArray[i] < s.length()) {
        return suffixArray[i] + 1;
      }
    }
  }

  static int[] buildSuffixArray(String s) {
    int n = s.length();

    Integer[] suffixArray = new Integer[n + 1];
    final int[] ranks = new int[n + 1];
    for (int i = 0; i <= n; ++i) {
      suffixArray[i] = i;
      ranks[i] = (i == n) ? -1 : s.charAt(i);
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
}