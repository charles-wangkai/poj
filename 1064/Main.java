import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int K = sc.nextInt();
    double[] cables = new double[N];
    for (int i = 0; i < cables.length; ++i) {
      cables[i] = sc.nextDouble();
    }

    System.out.println(solve(cables, K));

    sc.close();
  }

  static String solve(double[] cables, int K) {
    int[] lengths = new int[cables.length];
    for (int i = 0; i < lengths.length; ++i) {
      lengths[i] = (int) Math.round(cables[i] * 100);
    }

    int result = 0;
    int lower = 1;
    int upper = -1;
    for (int length : lengths) {
      upper = Math.max(upper, length);
    }
    while (lower <= upper) {
      int middle = (lower + upper) / 2;
      if (check(lengths, K, middle)) {
        result = middle;
        lower = middle + 1;
      } else {
        upper = middle - 1;
      }
    }

    return String.format("%d.%02d", result / 100, result % 100);
  }

  static boolean check(int[] lengths, int K, int unit) {
    long pieceNum = 0;
    for (int length : lengths) {
      pieceNum += length / unit;
    }

    return pieceNum >= K;
  }
}
