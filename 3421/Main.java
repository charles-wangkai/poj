import java.util.Arrays;
import java.util.Scanner;

public class Main {
  static final int LIMIT = 1 << 20;

  static int[] maxLengths;
  static int[] counts;

  public static void main(String[] args) {
    precompute();

    Scanner sc = new Scanner(System.in);

    while (sc.hasNextInt()) {
      int X = sc.nextInt();

      System.out.println(solve(X));
    }

    sc.close();
  }

  static void precompute() {
    maxLengths = new int[LIMIT + 1];
    Arrays.fill(maxLengths, 1);
    counts = new int[LIMIT + 1];
    Arrays.fill(counts, 1);

    for (int i = 2; i <= LIMIT; ++i) {
      for (int j = i + i; j <= LIMIT; j += i) {
        if (maxLengths[i] + 1 > maxLengths[j]) {
          maxLengths[j] = maxLengths[i] + 1;
          counts[j] = counts[i];
        } else if (maxLengths[i] + 1 == maxLengths[j]) {
          counts[j] += counts[i];
        }
      }
    }
  }

  static String solve(int X) {
    return String.format("%d %d", maxLengths[X], counts[X]);
  }
}
