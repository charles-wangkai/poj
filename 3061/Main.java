import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int N = sc.nextInt();
      int S = sc.nextInt();
      int[] a = new int[N];
      for (int i = 0; i < a.length; ++i) {
        a[i] = sc.nextInt();
      }

      System.out.println(solve(a, S));
    }

    sc.close();
  }

  static int solve(int[] a, int S) {
    int minLength = Integer.MAX_VALUE;
    int sum = 0;
    int beginIndex = 0;
    for (int endIndex = 0; endIndex < a.length; ++endIndex) {
      sum += a[endIndex];

      while (sum - a[beginIndex] >= S) {
        sum -= a[beginIndex];
        ++beginIndex;
      }

      if (sum >= S) {
        minLength = Math.min(minLength, endIndex - beginIndex + 1);
      }
    }

    return (minLength == Integer.MAX_VALUE) ? 0 : minLength;
  }
}
