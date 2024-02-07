import java.util.NavigableMap;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      int k = sc.nextInt();
      if (n == 0 && k == 0) {
        break;
      }

      int[] a = new int[n];
      for (int i = 0; i < a.length; ++i) {
        a[i] = sc.nextInt();
      }
      int[] t = new int[k];
      for (int i = 0; i < t.length; ++i) {
        t[i] = sc.nextInt();
      }

      System.out.println(solve(a, t));
    }

    sc.close();
  }

  static String solve(int[] a, int[] t) {
    StringBuilder result = new StringBuilder();
    for (int tj : t) {
      Outcome outcome = null;
      NavigableMap<Integer, Integer> sumToIndex = new TreeMap<Integer, Integer>();
      sumToIndex.put(0, -1);
      int sum = 0;
      for (int i = 0; i < a.length; ++i) {
        sum += a[i];
        for (int target : new int[] {sum - tj, sum + tj}) {
          for (Integer prevSum :
              new Integer[] {sumToIndex.floorKey(target), sumToIndex.ceilingKey(target)}) {
            if (prevSum != null) {
              outcome =
                  merge(
                      outcome,
                      new Outcome(
                          Math.abs(Math.abs(sum - prevSum) - tj),
                          Math.abs(sum - prevSum),
                          sumToIndex.get(prevSum) + 2,
                          i + 1));
            }
          }
        }

        sumToIndex.put(sum, i);
      }

      result.append(String.format("%d %d %d\n", outcome.absSum, outcome.lower, outcome.upper));
    }

    return result.toString();
  }

  static Outcome merge(Outcome o1, Outcome o2) {
    return (o1 == null || o2.diff < o1.diff) ? o2 : o1;
  }
}

class Outcome {
  int diff;
  int absSum;
  int lower;
  int upper;

  Outcome(int diff, int absSum, int lower, int upper) {
    this.diff = diff;
    this.absSum = absSum;
    this.lower = lower;
    this.upper = upper;
  }
}