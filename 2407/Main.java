// https://en.wikipedia.org/wiki/Euler%27s_totient_function

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
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

  static int solve(int n) {
    int result = n;
    for (int i = 2; i * i <= n; ++i) {
      if (n % i == 0) {
        result = result / i * (i - 1);

        while (n % i == 0) {
          n /= i;
        }
      }
    }
    if (n != 1) {
      result = result / n * (n - 1);
    }

    return result;
  }
}