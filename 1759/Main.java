import java.util.Scanner;

public class Main {
  static final int ITERATION_NUM = 100;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    double A = sc.nextDouble();

    System.out.println(String.format("%.2f", solve(N, A)));

    sc.close();
  }

  static double solve(int N, double A) {
    double result = -1;
    double lower = 0;
    double upper = 1000000;
    for (int i = 0; i < ITERATION_NUM; ++i) {
      double middle = (lower + upper) / 2;
      if (check(N, A, middle)) {
        result = middle;
        upper = middle;
      } else {
        lower = middle;
      }
    }

    return result;
  }

  static boolean check(int N, double A, double B) {
    int prevS = 0;
    double prevT = A;
    int currS = 1;
    double currT = 0;
    for (int i = 0; i < N - 2; ++i) {
      int nextS = 2 * currS - prevS;
      double nextT = 2 * currT + 2 - prevT;

      prevS = currS;
      prevT = currT;
      currS = nextS;
      currT = nextT;
    }

    double prevHeight = A;
    double currHeight = (B - currT) / currS;
    for (int i = 1; i < N - 1; ++i) {
      if (currHeight < 0) {
        return false;
      }

      double nextHeight = 2 * currHeight + 2 - prevHeight;
      prevHeight = currHeight;
      currHeight = nextHeight;
    }

    return true;
  }
}