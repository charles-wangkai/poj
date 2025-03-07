// https://www.hankcs.com/program/algorithm/poj-3134-power-calculus.html

import java.util.Scanner;

public class Main {
  static final int LIMIT = 1024;
  static final int MAX_PATH_LENGTH = 21;

  static int[] solutions;

  public static void main(String[] args) {
    precompute();

    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      if (n == 0) {
        break;
      }

      System.out.println(solve(n));
    }

    sc.close();
  }

  static void precompute() {
    solutions = new int[LIMIT + 1];
    for (int i = 1; i < solutions.length; ++i) {
      solutions[i] = Integer.toBinaryString(i).length() - 1 + (i & (~1));
    }

    int[] path = new int[MAX_PATH_LENGTH];
    path[0] = 1;

    search(path, 1);
  }

  static void search(int[] path, int index) {
    if (index != path.length) {
      for (int i = 0; i < index; ++i) {
        for (int next : new int[] {path[i] + path[index - 1], path[index - 1] - path[i]}) {
          if (next >= 1 && next < solutions.length && index <= solutions[next]) {
            solutions[next] = index;

            path[index] = next;
            search(path, index + 1);
          }
        }
      }
    }
  }

  static int solve(int n) {
    return solutions[n];
  }
}