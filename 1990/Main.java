import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class Main {

  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int N = Integer.parseInt(st.nextToken());
    int[] thresholds = new int[N];
    int[] xs = new int[N];
    for (int i = 0; i < N; ++i) {
      st = new StringTokenizer(br.readLine());
      thresholds[i] = Integer.parseInt(st.nextToken());
      xs[i] = Integer.parseInt(st.nextToken());
    }

    System.out.println(solve(thresholds, xs));
  }

  static long solve(final int[] thresholds, int[] xs) {
    int N = thresholds.length;

    BinaryIndexedTree counts = new BinaryIndexedTree();
    BinaryIndexedTree values = new BinaryIndexedTree();
    for (int i = 0; i < N; ++i) {
      counts.add(xs[i], 1);
      values.add(xs[i], xs[i]);
    }

    Integer[] sortedIndices = new Integer[N];
    for (int i = 0; i < sortedIndices.length; ++i) {
      sortedIndices[i] = i;
    }
    Arrays.sort(
        sortedIndices,
        new Comparator<Integer>() {
          @Override
          public int compare(Integer i1, Integer i2) {
            return -(thresholds[i1] - thresholds[i2]);
          }
        });

    int total = 0;
    for (int xi : xs) {
      total += xi;
    }

    long result = 0;
    for (int i = 0; i < sortedIndices.length; ++i) {
      int leftNum = counts.computeSum(xs[sortedIndices[i]]);
      int leftSum = values.computeSum(xs[sortedIndices[i]]);
      int rightNum = N - i - leftNum;
      int rightSum = total - leftSum;
      result +=
          (long)
                  ((xs[sortedIndices[i]] * leftNum - leftSum)
                      + (rightSum - xs[sortedIndices[i]] * rightNum))
              * thresholds[sortedIndices[i]];

      counts.add(xs[sortedIndices[i]], -1);
      values.add(xs[sortedIndices[i]], -xs[sortedIndices[i]]);
      total -= xs[sortedIndices[i]];
    }

    return result;
  }
}

class BinaryIndexedTree {
  static final int LIMIT = 20000;

  int[] a;

  BinaryIndexedTree() {
    a = new int[Integer.highestOneBit(LIMIT + 1) * 2 + 1];
  }

  void add(int i, int x) {
    while (i < a.length) {
      a[i] += x;
      i += i & -i;
    }
  }

  int computeSum(int i) {
    int result = 0;
    while (i != 0) {
      result += a[i];
      i -= i & -i;
    }

    return result;
  }
}