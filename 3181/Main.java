import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int K = sc.nextInt();

    System.out.println(solve(N, K));

    sc.close();
  }

  static BigInteger solve(int N, int K) {
    BigInteger[] dp = new BigInteger[N + 1];
    Arrays.fill(dp, BigInteger.ZERO);
    dp[0] = BigInteger.ONE;
    for (int i = 1; i <= K; ++i) {
      for (int j = i; j < dp.length; ++j) {
        dp[j] = dp[j].add(dp[j - i]);
      }
    }

    return dp[dp.length - 1];
  }
}
