import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
  static final char SEPARATOR = '\n';

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    sc.nextLine();
    for (int tc = 0; tc < N; ++tc) {
      String S = sc.nextLine();
      String T = sc.nextLine();

      System.out.println(String.format("Nejdelsi spolecny retezec ma delku %d.", solve(S, T)));
    }

    sc.close();
  }

  static int solve(String S, String T) {
    String combined = S + SEPARATOR + T;

    int[] suffixArray = buildSuffixArray(combined);
    int[] longestCommonPrefixes = buildLongestCommonPrefixes(combined, suffixArray);

    int result = 0;
    for (int i = 0; i < suffixArray.length - 1; ++i) {
      if ((suffixArray[i] < S.length()) != (suffixArray[i + 1] < S.length())) {
        result = Math.max(result, longestCommonPrefixes[i]);
      }
    }

    return result;
  }

  static int[] buildLongestCommonPrefixes(String s, int[] suffixArray) {
    int n = s.length();

    int[] ranks = new int[n + 1];
    for (int i = 0; i <= n; ++i) {
      ranks[suffixArray[i]] = i;
    }

    int[] result = new int[n];
    int h = 0;
    for (int i = 0; i < n; ++i) {
      if (h != 0) {
        --h;
      }

      int j = suffixArray[ranks[i] - 1];
      while (j + h < n && i + h < n && s.charAt(j + h) == s.charAt(i + h)) {
        ++h;
      }

      result[ranks[i] - 1] = h;
    }

    return result;
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