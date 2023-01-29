// https://introcs.cs.princeton.edu/java/99crypto/PollardRho.java.html

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Main {
  static final Random RANDOM = new Random();

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (sc.hasNextLong()) {
      long g = sc.nextLong();
      long l = sc.nextLong();

      System.out.println(solve(g, l));
    }

    sc.close();
  }

  static String solve(long g, long l) {
    Map<Long, Integer> primeToExponent = factorize(l / g);
    long[] parts = new long[primeToExponent.size()];
    int index = 0;
    for (long prime : primeToExponent.keySet()) {
      parts[index] = pow(prime, primeToExponent.get(prime));
      ++index;
    }

    long lower = 0;
    long upper = Long.MAX_VALUE;
    for (int mask = 0; mask < 1 << parts.length; ++mask) {
      long divisor1 = 1;
      for (int i = 0; i < parts.length; ++i) {
        if (((mask >> i) & 1) == 1) {
          divisor1 *= parts[i];
        }
      }
      long divisor2 = l / g / divisor1;

      if (Math.abs(divisor1 - divisor2) < upper - lower) {
        lower = Math.min(divisor1, divisor2);
        upper = Math.max(divisor1, divisor2);
      }
    }

    return String.format("%d %d", lower * g, upper * g);
  }

  static long pow(long base, int exponent) {
    long result = 1;
    for (int i = 0; i < exponent; ++i) {
      result *= base;
    }

    return result;
  }

  static Map<Long, Integer> factorize(long x) {
    Map<Long, Integer> primeToExponent = new HashMap<Long, Integer>();
    factor(primeToExponent, x);

    return primeToExponent;
  }

  static void factor(Map<Long, Integer> primeToExponent, long x) {
    if (x == 1) {
      return;
    }
    if (BigInteger.valueOf(x).isProbablePrime(20)) {
      if (!primeToExponent.containsKey(x)) {
        primeToExponent.put(x, 0);
      }
      primeToExponent.put(x, primeToExponent.get(x) + 1);

      return;
    }

    long divisor = rho(x);
    factor(primeToExponent, divisor);
    factor(primeToExponent, x / divisor);
  }

  static long rho(long n) {
    if (n % 2 == 0) {
      return 2;
    }

    BigInteger N = BigInteger.valueOf(n);

    BigInteger divisor;
    BigInteger c = new BigInteger(N.bitLength(), RANDOM);
    BigInteger x = new BigInteger(N.bitLength(), RANDOM);
    BigInteger xx = x;

    do {
      x = x.multiply(x).mod(N).add(c).mod(N);
      xx = xx.multiply(xx).mod(N).add(c).mod(N);
      xx = xx.multiply(xx).mod(N).add(c).mod(N);
      divisor = x.subtract(xx).gcd(N);
    } while ((divisor.compareTo(BigInteger.ONE)) == 0);

    return divisor.longValue();
  }
}
