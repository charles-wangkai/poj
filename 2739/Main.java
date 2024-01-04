import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  static final int LIMIT = 10000;

  static int[] additionNums;

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
    List<Integer> primes = buildPrimes();

    additionNums = new int[LIMIT + 1];
    for (int i = 0; i < primes.size(); ++i) {
      int sum = 0;
      for (int j = i; j < primes.size(); ++j) {
        sum += primes.get(j);
        if (sum > additionNums.length) {
          break;
        }

        ++additionNums[sum];
      }
    }
  }

  static List<Integer> buildPrimes() {
    boolean[] isPrimes = new boolean[LIMIT + 1];
    Arrays.fill(isPrimes, true);
    for (int i = 2; i < isPrimes.length; ++i) {
      if (isPrimes[i]) {
        for (int j = i + i; j < isPrimes.length; j += i) {
          isPrimes[j] = false;
        }
      }
    }

    List<Integer> result = new ArrayList<Integer>();
    for (int i = 2; i < isPrimes.length; ++i) {
      if (isPrimes[i]) {
        result.add(i);
      }
    }

    return result;
  }

  static int solve(int n) {
    return additionNums[n];
  }
}