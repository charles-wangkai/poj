import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int L = sc.nextInt();
    int N = sc.nextInt();
    int M = sc.nextInt();
    int[] rocks = new int[N];
    for (int i = 0; i < rocks.length; ++i) {
      rocks[i] = sc.nextInt();
    }

    System.out.println(solve(L, rocks, M));

    sc.close();
  }

  static int solve(int L, int[] rocks, int M) {
    Arrays.sort(rocks);

    int result = -1;
    int lower = 1;
    int upper = L;
    while (lower <= upper) {
      int middle = (lower + upper) / 2;
      if (check(L, rocks, M, middle)) {
        result = middle;
        lower = middle + 1;
      } else {
        upper = middle - 1;
      }
    }

    return result;
  }

  static boolean check(int L, int[] rocks, int M, int minDistance) {
    int removedCount = 0;
    int left = 0;
    for (int rock : rocks) {
      if (rock - left < minDistance || L - rock < minDistance) {
        ++removedCount;
      } else {
        left = rock;
      }
    }

    return removedCount <= M;
  }
}