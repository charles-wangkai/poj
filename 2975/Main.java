import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int n = sc.nextInt();
      if (n == 0) {
        break;
      }

      int[] k = new int[n];
      for (int i = 0; i < k.length; ++i) {
        k[i] = sc.nextInt();
      }

      System.out.println(solve(k));
    }

    sc.close();
  }

  static int solve(int[] k) {
    int xor = 0;
    for (int ki : k) {
      xor ^= ki;
    }
    if (xor == 0) {
      return 0;
    }

    int result = 0;
    for (int ki : k) {
      if ((ki & Integer.highestOneBit(xor)) != 0) {
        ++result;
      }
    }

    return result;
  }
}