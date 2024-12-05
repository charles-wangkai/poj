// https://www.hankcs.com/program/algorithm/poj-2720-last-digits.html
// https://zhuanlan.zhihu.com/p/452303582

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
  static final int N_LIMIT = 7;

  static Map<State, Integer> cache = new HashMap<State, Integer>();

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int b = sc.nextInt();
      if (b == 0) {
        break;
      }

      int iteration = sc.nextInt();
      int n = sc.nextInt();

      System.out.println(solve(b, iteration, n));
    }

    sc.close();
  }

  static String solve(int b, int iteration, int n) {
    State state = new State(b, iteration);
    if (!cache.containsKey(state)) {
      cache.put(state, fMod(b, iteration, tenPow(N_LIMIT)));
    }

    return String.format("%0" + n + "d", cache.get(state) % tenPow(n));
  }

  static int tenPow(int exponent) {
    int result = 1;
    for (int i = 0; i < exponent; ++i) {
      result *= 10;
    }

    return result;
  }

  static int fMod(int b, int iteration, int modulus) {
    if (iteration == 0) {
      return 1;
    }
    if (modulus == 1) {
      return 0;
    }

    int phi = computePhi(modulus);
    int fValue = f(b, iteration, phi);

    return (fValue == -1)
        ? powMod(b, fMod(b, iteration - 1, phi) + phi, modulus)
        : (fValue % modulus);
  }

  static int multiplyMod(int x, int y, int modulus) {
    return (int) ((long) x * y % modulus);
  }

  static int powMod(int base, int exponent, int modulus) {
    if (exponent == 0) {
      return 1;
    }

    return multiplyMod(
        powMod(multiplyMod(base, base, modulus), exponent / 2, modulus),
        (exponent % 2 == 0) ? 1 : base,
        modulus);
  }

  static int computePhi(int x) {
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

  static int f(int b, int iteration, int limit) {
    if (iteration == 0) {
      return 1;
    }

    int exponent = f(b, iteration - 1, limit);
    if (exponent == -1) {
      return -1;
    }

    int result = 1;
    for (int i = 0; i < exponent; ++i) {
      result *= b;
      if (result >= limit) {
        return -1;
      }
    }

    return result;
  }
}

class State {
  int b;
  int iteration;

  State(int b, int iteration) {
    this.b = b;
    this.iteration = iteration;
  }

  @Override
  public int hashCode() {
    return b * iteration;
  }

  @Override
  public boolean equals(Object obj) {
    State other = (State) obj;

    return b == other.b && iteration == other.iteration;
  }
}