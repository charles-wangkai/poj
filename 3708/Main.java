// https://www.hankcs.com/program/algorithm/poj-3708-recurrent-function.html
// https://cp-algorithms.com/algebra/extended-euclid-algorithm.html

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int d = sc.nextInt();
      if (d == -1) {
        break;
      }

      int[] a = new int[d];
      for (int i = 1; i < a.length; ++i) {
        a[i] = sc.nextInt();
      }
      int[] b = new int[d];
      for (int i = 0; i < b.length; ++i) {
        b[i] = sc.nextInt();
      }
      BigInteger m = sc.nextBigInteger();
      BigInteger k = sc.nextBigInteger();

      System.out.println(solve(a, b, m, k));
    }

    sc.close();
  }

  static String solve(int[] a, int[] b, BigInteger m, BigInteger k) {
    int d = b.length;

    List<Integer> mDigits = buildDigits(d, m);
    List<Integer> kDigits = buildDigits(d, k);
    if (mDigits.size() != kDigits.size()) {
      return "NO";
    }

    Recurrence[] recurrences = new Recurrence[mDigits.size()];
    for (int i = 0; i < recurrences.length; ++i) {
      recurrences[i] =
          computeRecurrence((i == recurrences.length - 1) ? a : b, mDigits.get(i), kDigits.get(i));
      if (recurrences[i] == null) {
        return "NO";
      }
    }

    long remainder = 0;
    long modulus = 1;
    for (Recurrence recurrence : recurrences) {
      Outcome outcome = gcd(modulus, recurrence.cycle);
      if ((remainder - recurrence.count) % outcome.g != 0) {
        return "NO";
      }

      modulus = modulus / outcome.g * recurrence.cycle;

      long z = outcome.y * ((remainder - recurrence.count) / outcome.g);
      remainder = ((z * recurrence.cycle + recurrence.count) % modulus + modulus) % modulus;
    }

    return String.valueOf(remainder);
  }

  static Outcome gcd(long a, long b) {
    if (b == 0) {
      return new Outcome(a, 1, 0);
    }

    Outcome outcome = gcd(b, a % b);

    return new Outcome(outcome.g, outcome.y, outcome.x - outcome.y * (a / b));
  }

  static Recurrence computeRecurrence(int[] nexts, int fromDigit, int toDigit) {
    int count = 0;
    int current = fromDigit;
    while (current != toDigit) {
      if (current == fromDigit && count != 0) {
        return null;
      }

      current = nexts[current];
      ++count;
    }

    int cycle = 0;
    while (current != toDigit || cycle == 0) {
      current = nexts[current];
      ++cycle;
    }

    return new Recurrence(count, cycle);
  }

  static List<Integer> buildDigits(int d, BigInteger x) {
    List<Integer> result = new ArrayList<Integer>();
    while (!x.equals(BigInteger.ZERO)) {
      result.add(x.mod(BigInteger.valueOf(d)).intValue());
      x = x.divide(BigInteger.valueOf(d));
    }

    return result;
  }
}

class Outcome {
  long g;
  long x;
  long y;

  Outcome(long g, long x, long y) {
    this.g = g;
    this.x = x;
    this.y = y;
  }
}

class Recurrence {
  int count;
  int cycle;

  Recurrence(int count, int cycle) {
    this.count = count;
    this.cycle = cycle;
  }
}