import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      String line = br.readLine();
      if (line == null) {
        break;
      }

      StringTokenizer st = new StringTokenizer(line);
      int M = Integer.parseInt(st.nextToken());
      int N = Integer.parseInt(st.nextToken());
      int[][] A = new int[M][N];
      for (int i = 0; i < M; ++i) {
        st = new StringTokenizer(br.readLine());
        for (int j = 0; j < N; ++j) {
          A[i][j] = Integer.parseInt(st.nextToken());
        }
      }

      System.out.println(solve(A) ? "Yes, I found it" : "It is impossible");
    }
  }

  static boolean solve(int[][] A) {
    int M = A.length;
    int N = A[0].length;

    int[] values = new int[N];
    for (int j = 0; j < values.length; ++j) {
      for (int i = 0; i < M; ++i) {
        values[j] = values[j] * 2 + A[i][j];
      }
    }

    for (int mask = 0; mask < 1 << M; ++mask) {
      if (check(values, mask)) {
        return true;
      }
    }

    return false;
  }

  static boolean check(int[] values, int mask) {
    for (int value : values) {
      if (Integer.bitCount(value & mask) != 1) {
        return false;
      }
    }

    return true;
  }
}