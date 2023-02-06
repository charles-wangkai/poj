import java.util.Scanner;

public class Main {
  static final int MODULUS = 1000000000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();

    System.out.println(solve(N));

    sc.close();
  }

  static int solve(int N) {
    int[] dp = new int[N + 1];
    dp[0] = 1;
    for (int i = 1; i < dp.length; ++i) {
      dp[i] = (i % 2 == 0) ? addMod(dp[i / 2], dp[i - 1]) : dp[i - 1];
    }

    return dp[dp.length - 1];
  }

  static int addMod(int x, int y) {
    return (x + y) % MODULUS;
  }
}
