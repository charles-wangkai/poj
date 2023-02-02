import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    sc.nextInt();
    String s = sc.next();
    char[] letters = new char[N];
    int[] addCosts = new int[N];
    int[] deleteCosts = new int[N];
    for (int i = 0; i < N; ++i) {
      letters[i] = sc.next().charAt(0);
      addCosts[i] = sc.nextInt();
      deleteCosts[i] = sc.nextInt();
    }

    System.out.println(solve(letters, addCosts, deleteCosts, s));

    sc.close();
  }

  static int solve(char[] letters, int[] addCosts, int[] deleteCosts, String s) {
    Map<Character, Integer> letterToCost = new HashMap<Character, Integer>();
    for (int i = 0; i < letters.length; ++i) {
      letterToCost.put(letters[i], Math.min(addCosts[i], deleteCosts[i]));
    }

    int[][] dp = new int[s.length()][s.length()];
    for (int length = 1; length <= s.length(); ++length) {
      for (int beginIndex = 0; beginIndex + length - 1 < s.length(); ++beginIndex) {
        int endIndex = beginIndex + length - 1;
        if (s.charAt(beginIndex) == s.charAt(endIndex)) {
          dp[beginIndex][endIndex] = (length >= 3) ? dp[beginIndex + 1][endIndex - 1] : 0;
        } else {
          dp[beginIndex][endIndex] =
              Math.min(
                  letterToCost.get(s.charAt(beginIndex)) + dp[beginIndex + 1][endIndex],
                  letterToCost.get(s.charAt(endIndex)) + dp[beginIndex][endIndex - 1]);
        }
      }
    }

    return dp[0][s.length() - 1];
  }
}
