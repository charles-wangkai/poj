// https://www.hankcs.com/program/algorithm/poj-3415-common-substrings.html

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int K = sc.nextInt();
      if (K == 0) {
        break;
      }

      String A = sc.next();
      String B = sc.next();

      System.out.println(solve(K, A, B));
    }

    sc.close();
  }

  static long solve(int K, String A, String B) {
    String combined = A + "$" + B;

    int[] suffixArray = buildSuffixArray(combined);
    int[] longestCommonPrefixes = buildLongestCommonPrefixes(combined, suffixArray);

    return computeWayNum(K, suffixArray, longestCommonPrefixes, A.length(), true)
        + computeWayNum(K, suffixArray, longestCommonPrefixes, A.length(), false);
  }

  static long computeWayNum(
      int K, int[] suffixArray, int[] longestCommonPrefixes, int lengthA, boolean firstA) {
    long result = 0;
    Deque<Element> stack = new ArrayDeque<Element>();
    long contribution = 0;
    for (int i = 0; i < longestCommonPrefixes.length; ++i) {
      if (longestCommonPrefixes[i] < K) {
        stack.clear();
        contribution = 0;
      } else {
        int size = 0;
        if ((firstA && suffixArray[i] < lengthA) || (!firstA && suffixArray[i] > lengthA)) {
          ++size;
          contribution += longestCommonPrefixes[i] - K + 1;
        }

        while (!stack.isEmpty() && stack.peek().longestCommonPrefix >= longestCommonPrefixes[i]) {
          Element top = stack.pop();

          contribution -= (long) top.size * (top.longestCommonPrefix - longestCommonPrefixes[i]);
          size += top.size;
        }
        if (size != 0) {
          stack.push(new Element(longestCommonPrefixes[i], size));
        }

        if ((firstA && suffixArray[i + 1] > lengthA) || (!firstA && suffixArray[i + 1] < lengthA)) {
          result += contribution;
        }
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

class Element {
  int longestCommonPrefix;
  int size;

  Element(int longestCommonPrefix, int size) {
    this.longestCommonPrefix = longestCommonPrefix;
    this.size = size;
  }
}
