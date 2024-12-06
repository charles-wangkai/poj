// https://www.hankcs.com/program/algorithm/poj-1740-a-new-stone-game.html
// http://poj.org/showmessage?message_id=96035

import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      if (n == 0) {
        break;
      }

      int[] piles = new int[n];
      for (int i = 0; i < piles.length; ++i) {
        piles[i] = sc.nextInt();
      }

      System.out.println(solve(piles));
    }

    sc.close();
  }

  static int solve(int[] piles) {
    if (piles.length % 2 == 1) {
      return 1;
    }

    Arrays.sort(piles);
    for (int i = 0; i < piles.length; i += 2) {
      if (piles[i] != piles[i + 1]) {
        return 1;
      }
    }

    return 0;
  }
}