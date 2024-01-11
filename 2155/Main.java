import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int X = sc.nextInt();
    for (int tc = 0; tc < X; ++tc) {
      int N = sc.nextInt();
      int T = sc.nextInt();
      String[] instructions = new String[T];
      sc.nextLine();
      for (int i = 0; i < instructions.length; ++i) {
        instructions[i] = sc.nextLine();
      }

      if (tc != 0) {
        System.out.println();
      }
      System.out.println(solve(N, instructions));
    }

    sc.close();
  }

  static String solve(int N, String[] instructions) {
    StringBuilder result = new StringBuilder();
    boolean[][] inverted = new boolean[N][N];
    for (String instruction : instructions) {
      String[] parts = instruction.split(" ");
      if (parts[0].equals("C")) {
        int x1 = Integer.parseInt(parts[1]);
        int y1 = Integer.parseInt(parts[2]);
        int x2 = Integer.parseInt(parts[3]);
        int y2 = Integer.parseInt(parts[4]);

        for (int r = x1 - 1; r <= x2 - 1; ++r) {
          inverted[r][y1 - 1] ^= true;
          if (y2 != N) {
            inverted[r][y2] ^= true;
          }
        }
      } else {
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);

        int value = 0;
        for (int c = 0; c <= y - 1; ++c) {
          if (inverted[x - 1][c]) {
            value = 1 - value;
          }
        }

        if (result.length() != 0) {
          result.append("\n");
        }
        result.append(value);
      }
    }

    return result.toString();
  }
}