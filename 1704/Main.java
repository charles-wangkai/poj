import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int N = sc.nextInt();
      int[] P = new int[N];
      for (int i = 0; i < P.length; ++i) {
        P[i] = sc.nextInt();
      }

      System.out.println(solve(P));
    }

    sc.close();
  }

  static String solve(int[] P) {
    List<Integer> sorted = new ArrayList<Integer>();
    for (int pi : P) {
      sorted.add(pi);
    }
    if (P.length % 2 == 1) {
      sorted.add(0);
    }
    Collections.sort(sorted);

    int xor = 0;
    for (int i = 0; i < sorted.size(); i += 2) {
      xor ^= sorted.get(i + 1) - sorted.get(i) - 1;
    }

    return (xor == 0) ? "Bob will win" : "Georgia will win";
  }
}