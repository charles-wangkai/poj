import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
  static final int BASE1 = 10007;
  static final int MODULUS1 = 1000000007;
  static final int BASE2 = 10009;
  static final int MODULUS2 = 1000000009;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (sc.hasNextInt()) {
      int n = sc.nextInt();
      int m = sc.nextInt();
      int k = sc.nextInt();
      int[] S1 = new int[n];
      for (int i = 0; i < S1.length; ++i) {
        S1[i] = sc.nextInt();
      }
      int[] S2 = new int[m];
      for (int i = 0; i < S2.length; ++i) {
        S2[i] = sc.nextInt();
      }

      System.out.println(solve(S1, S2, k));
    }

    sc.close();
  }

  static int solve(int[] S1, int[] S2, int k) {
    Set<Hash> hashes2 = buildHashes(S2, k);
    Set<Hash> extendedHashes2 = buildHashes(S2, k + 1);

    int highestPower1 = powMod(BASE1, k - 1, MODULUS1);
    int highestPower2 = powMod(BASE2, k - 1, MODULUS2);

    int result = 0;
    int hash1 = 0;
    int hash2 = 0;
    for (int i = 0; i < S1.length; ++i) {
      hash1 = addMod(multiplyMod(hash1, BASE1, MODULUS1), S1[i] + 1, MODULUS1);
      hash2 = addMod(multiplyMod(hash2, BASE2, MODULUS2), S1[i] + 1, MODULUS2);

      if (i >= k - 1) {
        if (hashes2.contains(new Hash(hash1, hash2))
            && ((i == S1.length - 1)
                || !extendedHashes2.contains(
                    new Hash(
                        addMod(multiplyMod(hash1, BASE1, MODULUS1), S1[i + 1] + 1, MODULUS1),
                        addMod(multiplyMod(hash2, BASE2, MODULUS2), S1[i + 1] + 1, MODULUS2))))) {
          ++result;
        }

        hash1 = addMod(hash1, -multiplyMod(S1[i - k + 1] + 1, highestPower1, MODULUS1), MODULUS1);
        hash2 = addMod(hash2, -multiplyMod(S1[i - k + 1] + 1, highestPower2, MODULUS2), MODULUS2);
      }
    }

    return result;
  }

  static Set<Hash> buildHashes(int[] S2, int length) {
    if (length > S2.length) {
      return Collections.emptySet();
    }

    int highestPower1 = powMod(BASE1, length - 1, MODULUS1);
    int highestPower2 = powMod(BASE2, length - 1, MODULUS2);

    Set<Hash> result = new HashSet<Hash>();
    int hash1 = 0;
    int hash2 = 0;
    for (int i = 0; i < S2.length; ++i) {
      hash1 = addMod(multiplyMod(hash1, BASE1, MODULUS1), S2[i] + 1, MODULUS1);
      hash2 = addMod(multiplyMod(hash2, BASE2, MODULUS2), S2[i] + 1, MODULUS2);

      if (i >= length - 1) {
        result.add(new Hash(hash1, hash2));

        hash1 =
            addMod(hash1, -multiplyMod(S2[i - length + 1] + 1, highestPower1, MODULUS1), MODULUS1);
        hash2 =
            addMod(hash2, -multiplyMod(S2[i - length + 1] + 1, highestPower2, MODULUS2), MODULUS2);
      }
    }

    return result;
  }

  static int addMod(int x, int y, int m) {
    return ((x + y) % m + m) % m;
  }

  static int multiplyMod(int x, int y, int m) {
    return (int) ((long) x * y % m);
  }

  static int powMod(int base, int exponent, int m) {
    int result = 1;
    for (int i = 0; i < exponent; ++i) {
      result = multiplyMod(result, base, m);
    }

    return result;
  }
}

class Hash {
  int hash1;
  int hash2;

  Hash(int hash1, int hash2) {
    this.hash1 = hash1;
    this.hash2 = hash2;
  }

  @Override
  public int hashCode() {
    return hash1 * hash2;
  }

  @Override
  public boolean equals(Object obj) {
    Hash other = (Hash) obj;

    return hash1 == other.hash1 && hash2 == other.hash2;
  }
}