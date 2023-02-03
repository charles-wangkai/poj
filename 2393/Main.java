import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int S = sc.nextInt();
    int[] C = new int[N];
    int[] Y = new int[N];
    for (int i = 0; i < N; ++i) {
      C[i] = sc.nextInt();
      Y[i] = sc.nextInt();
    }

    System.out.println(solve(C, Y, S));

    sc.close();
  }

  static long solve(int[] C, int[] Y, int S) {
    long result = 0;
    int minCost = Integer.MAX_VALUE;
    for (int i = 0; i < C.length; ++i) {
      minCost = Math.min(minCost, C[i] - i * S);
      result += (long) (minCost + i * S) * Y[i];
    }

    return result;
  }
}
