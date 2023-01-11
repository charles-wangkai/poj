import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int t = sc.nextInt();
    for (int tc = 0; tc < t; ++tc) {
      int l = sc.nextInt();
      int n = sc.nextInt();
      int[] positions = new int[n];
      for (int i = 0; i < positions.length; ++i) {
        positions[i] = sc.nextInt();
      }

      System.out.println(solve(l, positions));
    }

    sc.close();
  }

  static String solve(int l, int[] positions) {
    int minTime = -1;
    for (int position : positions) {
      minTime = Math.max(minTime, Math.min(position, l - position));
    }

    int maxTime = -1;
    for (int position : positions) {
      maxTime = Math.max(maxTime, Math.max(position, l - position));
    }

    return String.format("%d %d", minTime, maxTime);
  }
}
