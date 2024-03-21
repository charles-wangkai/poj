import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
  static final int LIMIT = 200;

  static int[][] nimbers;

  public static void main(String[] args) {
    precompute();

    Scanner sc = new Scanner(System.in);

    while (sc.hasNextInt()) {
      int W = sc.nextInt();
      int H = sc.nextInt();

      System.out.println(solve(W, H));
    }

    sc.close();
  }

  static void precompute() {
    nimbers = new int[LIMIT + 1][LIMIT + 1];
    for (int w = 2; w <= LIMIT; ++w) {
      for (int h = 2; h <= LIMIT; ++h) {
        Set<Integer> seen = new HashSet<Integer>();
        for (int i = 2; w - i >= 2; ++i) {
          seen.add(nimbers[i][h] ^ nimbers[w - i][h]);
        }
        for (int i = 2; h - i >= 2; ++i) {
          seen.add(nimbers[w][i] ^ nimbers[w][h - i]);
        }

        while (seen.contains(nimbers[w][h])) {
          ++nimbers[w][h];
        }
      }
    }
  }

  static String solve(int W, int H) {
    return (nimbers[W][H] == 0) ? "LOSE" : "WIN";
  }
}