// https://blog.csdn.net/weixin_45840825/article/details/115313925

import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int[] weights = new int[N];
    for (int i = 0; i < weights.length; ++i) {
      weights[i] = sc.nextInt();
    }

    System.out.println(String.format("%.3f", solve(weights)));

    sc.close();
  }

  static double solve(int[] weights) {
    Arrays.sort(weights);

    double result = weights[weights.length - 1];
    for (int i = weights.length - 2; i >= 0; --i) {
      result = 2 * Math.sqrt(result * weights[i]);
    }

    return result;
  }
}
