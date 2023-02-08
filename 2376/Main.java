import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int T = sc.nextInt();
    int[] starts = new int[N];
    int[] ends = new int[N];
    for (int i = 0; i < N; ++i) {
      starts[i] = sc.nextInt();
      ends[i] = sc.nextInt();
    }

    System.out.println(solve(starts, ends, T));

    sc.close();
  }

  static int solve(final int[] starts, int[] ends, int T) {
    Integer[] sortedIndices = new Integer[starts.length];
    for (int i = 0; i < sortedIndices.length; ++i) {
      sortedIndices[i] = i;
    }
    Arrays.sort(
        sortedIndices,
        new Comparator<Integer>() {
          public int compare(Integer i1, Integer i2) {
            return starts[i1] - starts[i2];
          }
        });

    int result = 0;
    int maxEnd = 0;
    int nextMaxEnd = 0;
    for (int index : sortedIndices) {
      if (starts[index] > T) {
        break;
      }

      if (starts[index] > nextMaxEnd + 1) {
        return -1;
      }

      if (starts[index] > maxEnd + 1) {
        maxEnd = nextMaxEnd;
        ++result;
      }

      nextMaxEnd = Math.max(nextMaxEnd, ends[index]);
    }

    if (nextMaxEnd < T) {
      return -1;
    }

    if (maxEnd < T) {
      ++result;
    }

    return result;
  }
}
