import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int M = sc.nextInt();
    int[] money = new int[N];
    for (int i = 0; i < money.length; ++i) {
      money[i] = sc.nextInt();
    }

    System.out.println(solve(money, M));

    sc.close();
  }

  static int solve(int[] money, int M) {
    int lower = -1;
    int upper = 0;
    for (int m : money) {
      lower = Math.max(lower, m);
      upper += m;
    }

    int result = -1;
    while (lower <= upper) {
      int middle = (lower + upper) / 2;
      if (check(money, M, middle)) {
        result = middle;
        upper = middle - 1;
      } else {
        lower = middle + 1;
      }
    }

    return result;
  }

  static boolean check(int[] money, int M, int limit) {
    int count = 1;
    int rest = limit;
    for (int m : money) {
      if (m > rest) {
        ++count;
        rest = limit;
      }

      rest -= m;
    }

    return count <= M;
  }
}