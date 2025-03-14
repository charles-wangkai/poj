// https://en.wikipedia.org/wiki/Primitive_root_modulo_n#Finding_primitive_roots
// https://en.wikipedia.org/wiki/Euler%27s_totient_function

import java.util.Scanner;

public class Main {
  static final int LIMIT = 65536;

  static int[] totients;

  public static void main(String[] args) {
    precompute();

    Scanner sc = new Scanner(System.in);

    while (sc.hasNextInt()) {
      int p = sc.nextInt();

      System.out.println(solve(p));
    }

    sc.close();
  }

  static void precompute() {
    totients = new int[LIMIT];
    for (int i = 1; i < totients.length; ++i) {
      totients[i] = computeTotient(i);
    }
  }

  static int computeTotient(int x) {
    int result = x;
    for (int i = 2; i * i <= x; ++i) {
      if (x % i == 0) {
        result = result / i * (i - 1);

        while (x % i == 0) {
          x /= i;
        }
      }
    }
    if (x != 1) {
      result = result / x * (x - 1);
    }

    return result;
  }

  static int solve(int p) {
    return totients[totients[p]];
  }
}