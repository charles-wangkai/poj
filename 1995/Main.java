import java.math.BigInteger;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int Z = sc.nextInt();
    for (int tc = 0; tc < Z; ++tc) {
      int M = sc.nextInt();
      int H = sc.nextInt();
      BigInteger[] A = new BigInteger[H];
      BigInteger[] B = new BigInteger[H];
      for (int i = 0; i < H; ++i) {
        A[i] = sc.nextBigInteger();
        B[i] = sc.nextBigInteger();
      }

      System.out.println(solve(M, A, B));
    }

    sc.close();
  }

  static int solve(int M, BigInteger[] A, BigInteger[] B) {
    int result = 0;
    for (int i = 0; i < A.length; ++i) {
      result = addMod(result, A[i].modPow(B[i], BigInteger.valueOf(M)).intValue(), M);
    }

    return result;
  }

  static int addMod(int x, int y, int m) {
    return (x + y) % m;
  }
}
