import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int a = sc.nextInt();
      int b = sc.nextInt();
      if (a == 0 && b == 0) {
        break;
      }

      System.out.println(solve(a, b));
    }

    sc.close();
  }

  static String solve(int a, int b) {
    return isFirstPlayerWin(a, b) ? "Stan wins" : "Ollie wins";
  }

  static boolean isFirstPlayerWin(int a, int b) {
    if (a < b) {
      return isFirstPlayerWin(b, a);
    }
    if (a % b == 0 || a - b > b) {
      return true;
    }

    return !isFirstPlayerWin(a - b, b);
  }
}