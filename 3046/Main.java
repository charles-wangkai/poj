import java.util.Scanner;

public class Main {
  static final int MODULUS = 1000000;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    int A = sc.nextInt();
    int S = sc.nextInt();
    int B = sc.nextInt();
    int[] types = new int[A];
    for (int i = 0; i < types.length; ++i) {
      types[i] = sc.nextInt() - 1;
    }

    System.out.println(solve(T, types, S, B));

    sc.close();
  }

  static int solve(int T, int[] types, int S, int B) {
    int[] counts = new int[T];
    for (int type : types) {
      ++counts[type];
    }

    int[] dp = new int[types.length + 1];
    dp[0] = 1;
    for (int count : counts) {
      int sum = 0;
      for (int i = dp.length - 1; i >= dp.length - 1 - count && i >= 0; --i) {
        sum = addMod(sum, dp[i]);
      }

      for (int i = dp.length - 1; i >= 0; --i) {
        sum = addMod(sum, -dp[i]);

        dp[i] = addMod(dp[i], sum);

        if (i >= count + 1) {
          sum = addMod(sum, dp[i - count - 1]);
        }
      }
    }

    int result = 0;
    for (int i = S; i <= B; ++i) {
      result = addMod(result, dp[i]);
    }

    return result;
  }

  static int addMod(int x, int y) {
    return (x + y + MODULUS) % MODULUS;
  }
}
