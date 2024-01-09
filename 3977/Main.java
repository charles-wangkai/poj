import java.util.NavigableMap;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int N = sc.nextInt();
      if (N == 0) {
        break;
      }

      long[] a = new long[N];
      for (int i = 0; i < a.length; ++i) {
        a[i] = sc.nextLong();
      }

      System.out.println(solve(a));
    }

    sc.close();
  }

  static String solve(long[] a) {
    int half = a.length / 2;

    NavigableMap<Long, Integer> sumToMinSize = new TreeMap<Long, Integer>();
    for (int mask = 1; mask < 1 << half; ++mask) {
      long sum = 0;
      for (int i = 0; i < half; ++i) {
        if (((mask >> i) & 1) == 1) {
          sum += a[i];
        }
      }

      sumToMinSize.put(
          sum,
          Math.min(
              sumToMinSize.containsKey(sum) ? sumToMinSize.get(sum) : Integer.MAX_VALUE,
              Integer.bitCount(mask)));
    }

    Outcome outcome = new Outcome(Long.MAX_VALUE, -1);
    for (int mask = 0; mask < 1 << (a.length - half); ++mask) {
      long sum = 0;
      for (int i = half; i < a.length; ++i) {
        if (((mask >> (i - half)) & 1) == 1) {
          sum += a[i];
        }
      }

      if (mask != 0) {
        outcome = merge(outcome, new Outcome(Math.abs(sum), Integer.bitCount(mask)));
      }

      Long floorSum = sumToMinSize.floorKey(-sum);
      if (floorSum != null) {
        outcome =
            merge(
                outcome,
                new Outcome(
                    Math.abs(sum + floorSum), Integer.bitCount(mask) + sumToMinSize.get(floorSum)));
      }

      Long ceilingSum = sumToMinSize.ceilingKey(-sum);
      if (ceilingSum != null) {
        outcome =
            merge(
                outcome,
                new Outcome(
                    Math.abs(sum + ceilingSum),
                    Integer.bitCount(mask) + sumToMinSize.get(ceilingSum)));
      }
    }

    return String.format("%d %d", outcome.minAbsSum, outcome.minSizeFoMinAbsSum);
  }

  static Outcome merge(Outcome o1, Outcome o2) {
    return (o1.minAbsSum < o2.minAbsSum
            || (o1.minAbsSum == o2.minAbsSum && o1.minSizeFoMinAbsSum < o2.minSizeFoMinAbsSum))
        ? o1
        : o2;
  }
}

class Outcome {
  long minAbsSum;
  int minSizeFoMinAbsSum;

  Outcome(long minAbsSum, int minSizeFoMinAbsSum) {
    this.minAbsSum = minAbsSum;
    this.minSizeFoMinAbsSum = minSizeFoMinAbsSum;
  }
}