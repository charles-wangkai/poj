import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    char[] faces = new char[N];
    for (int i = 0; i < faces.length; ++i) {
      faces[i] = sc.next().charAt(0);
    }

    System.out.println(solve(faces));

    sc.close();
  }

  static String solve(char[] faces) {
    int M = Integer.MAX_VALUE;
    int K = -1;
    for (int length = 1; length <= faces.length; ++length) {
      int operationNum = computeOperationNum(faces, length);
      if (operationNum < M) {
        M = operationNum;
        K = length;
      }
    }

    return String.format("%d %d", K, M);
  }

  static int computeOperationNum(char[] faces, int length) {
    int result = 0;
    boolean[] delta = new boolean[faces.length + 1];
    boolean flipped = false;
    for (int i = 0; i < faces.length; ++i) {
      flipped ^= delta[i];
      boolean backward = (faces[i] == 'B') ^ flipped;
      if (backward) {
        if (i + length >= delta.length) {
          return Integer.MAX_VALUE;
        }

        flipped ^= true;
        delta[i + length] ^= true;
        ++result;
      }
    }

    return result;
  }
}
