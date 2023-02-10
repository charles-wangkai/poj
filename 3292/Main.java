import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  static final int LIMIT = 1000001;

  static int[] hSemiPrimeNums;

  public static void main(String[] args) {
    preprocess();

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

  static void preprocess() {
    boolean[] isHPrimes = new boolean[LIMIT + 1];
    for (int i = 0; i < isHPrimes.length; ++i) {
      isHPrimes[i] = i >= 5 && i % 4 == 1;
    }
    for (int i = 0; i < isHPrimes.length; ++i) {
      if (isHPrimes[i]) {
        for (int j = i * 5; j < isHPrimes.length; j += i * 4) {
          isHPrimes[j] = false;
        }
      }
    }

    List<Integer> hPrimes = new ArrayList<Integer>();
    for (int i = 0; i < isHPrimes.length; ++i) {
      if (isHPrimes[i]) {
        hPrimes.add(i);
      }
    }

    boolean[] isHSemiPrimes = new boolean[LIMIT + 1];
    for (int i = 0; i < hPrimes.size(); ++i) {
      for (int j = i; j < hPrimes.size(); ++j) {
        long hSemiPrime = (long) hPrimes.get(i) * hPrimes.get(j);
        if (hSemiPrime >= isHSemiPrimes.length) {
          break;
        }

        isHSemiPrimes[(int) hSemiPrime] = true;
      }
    }

    hSemiPrimeNums = new int[LIMIT + 1];
    for (int i = 0; i < hSemiPrimeNums.length; ++i) {
      hSemiPrimeNums[i] = ((i == 0) ? 0 : hSemiPrimeNums[i - 1]) + (isHSemiPrimes[i] ? 1 : 0);
    }
  }

  static String solve(int n) {
    return String.format("%d %d", n, hSemiPrimeNums[n]);
  }
}
