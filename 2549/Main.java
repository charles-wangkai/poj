import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      int n = Integer.parseInt(st.nextToken());
      if (n == 0) {
        break;
      }

      int[] S = new int[n];
      for (int i = 0; i < S.length; ++i) {
        st = new StringTokenizer(br.readLine());
        S[i] = Integer.parseInt(st.nextToken());
      }

      System.out.println(solve(S));
    }
  }

  static String solve(int[] S) {
    int result = Integer.MIN_VALUE;

    Set<Integer> leftSums = new HashSet<Integer>();
    for (int i = 0; i < S.length; ++i) {
      for (int j = 0; j < i - 1; ++j) {
        leftSums.add(S[i - 1] + S[j]);
      }

      for (int j = i + 1; j < S.length; ++j) {
        if (leftSums.contains(S[i] - S[j])) {
          result = Math.max(result, S[i]);
        }
        if (leftSums.contains(S[j] - S[i])) {
          result = Math.max(result, S[j]);
        }
      }
    }

    Set<Integer> rightSums = new HashSet<Integer>();
    for (int i = S.length - 1; i >= 0; --i) {
      for (int j = i + 2; j < S.length; ++j) {
        rightSums.add(S[i + 1] + S[j]);
      }

      for (int j = 0; j < i; ++j) {
        if (rightSums.contains(S[i] - S[j])) {
          result = Math.max(result, S[i]);
        }
        if (rightSums.contains(S[j] - S[i])) {
          result = Math.max(result, S[j]);
        }
      }
    }

    return (result == Integer.MIN_VALUE) ? "no solution" : String.valueOf(result);
  }
}