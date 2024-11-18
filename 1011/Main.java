import java.util.Scanner;

public class Main {
  static final int LIMIT = 50;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      if (n == 0) {
        break;
      }

      int[] a = new int[n];
      for (int i = 0; i < a.length; ++i) {
        a[i] = sc.nextInt();
      }

      System.out.println(solve(a));
    }

    sc.close();
  }

  static int solve(int[] a) {
    int[] counts = new int[LIMIT + 1];
    int max = -1;
    int sum = 0;
    for (int ai : a) {
      ++counts[ai];
      max = Math.max(max, ai);
      sum += ai;
    }

    for (int i = max; ; ++i) {
      if (sum % i == 0 && search(counts, a.length, i, 0)) {
        return i;
      }
    }
  }

  static boolean search(int[] counts, int stickRest, int length, int unitRest) {
    if (stickRest == 0) {
      return true;
    }
    if (unitRest == 0) {
      return search(counts, stickRest, length, length);
    }

    for (int i = Math.min(unitRest, counts.length - 1); i >= 1; --i) {
      if (counts[i] != 0) {
        --counts[i];

        if (search(counts, stickRest - 1, length, unitRest - i)) {
          return true;
        }

        ++counts[i];
        if (unitRest == length || i == unitRest) {
          break;
        }
      }
    }

    return false;
  }
}