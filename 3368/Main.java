import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      if (n == 0) {
        break;
      }

      int q = sc.nextInt();
      int[] a = new int[n];
      for (int i = 0; i < a.length; ++i) {
        a[i] = sc.nextInt();
      }
      int[] lefts = new int[q];
      int[] rights = new int[q];
      for (int i = 0; i < q; ++i) {
        lefts[i] = sc.nextInt();
        rights[i] = sc.nextInt();
      }

      System.out.println(solve(a, lefts, rights));
    }

    sc.close();
  }

  static String solve(int[] a, int[] lefts, int[] rights) {
    Map<Integer, Integer> valueToRangeIndex = new HashMap<Integer, Integer>();
    List<Range> ranges = new ArrayList<Range>();
    int beginIndex = 0;
    for (int i = 1; i <= a.length; ++i) {
      if (i == a.length || a[i] != a[i - 1]) {
        valueToRangeIndex.put(a[i - 1], ranges.size());
        ranges.add(new Range(beginIndex, i - 1));

        beginIndex = i;
      }
    }

    int maxExponent = Integer.numberOfTrailingZeros(Integer.highestOneBit(ranges.size()));
    int[][] maxFreqs = new int[ranges.size()][maxExponent + 1];
    for (int i = 0; i < maxFreqs.length; ++i) {
      maxFreqs[i][0] = ranges.get(i).endIndex - ranges.get(i).beginIndex + 1;
    }
    for (int j = 1; j <= maxExponent; ++j) {
      for (int i = 0; i + (1 << j) <= ranges.size(); ++i) {
        maxFreqs[i][j] = Math.max(maxFreqs[i][j - 1], maxFreqs[i + (1 << (j - 1))][j - 1]);
      }
    }

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < lefts.length; ++i) {
      if (result.length() != 0) {
        result.append("\n");
      }

      int leftRangeIndex = valueToRangeIndex.get(a[lefts[i] - 1]);
      int rightRangeIndex = valueToRangeIndex.get(a[rights[i] - 1]);
      if (leftRangeIndex == rightRangeIndex) {
        result.append(rights[i] - lefts[i] + 1);
      } else {
        result.append(
            Math.max(
                computeMaxFreq(maxFreqs, leftRangeIndex + 1, rightRangeIndex - 1),
                Math.max(
                    ranges.get(leftRangeIndex).endIndex - (lefts[i] - 1) + 1,
                    (rights[i] - 1) - ranges.get(rightRangeIndex).beginIndex + 1)));
      }
    }

    return result.toString();
  }

  static int computeMaxFreq(int[][] maxFreqs, int fromIndex, int toIndex) {
    if (fromIndex > toIndex) {
      return -1;
    }

    int maxExponent = Integer.numberOfTrailingZeros(Integer.highestOneBit(toIndex - fromIndex + 1));

    return Math.max(
        maxFreqs[fromIndex][maxExponent], maxFreqs[toIndex - (1 << maxExponent) + 1][maxExponent]);
  }
}

class Range {
  int beginIndex;
  int endIndex;

  Range(int beginIndex, int endIndex) {
    this.beginIndex = beginIndex;
    this.endIndex = endIndex;
  }
}