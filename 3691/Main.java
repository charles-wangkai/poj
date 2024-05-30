import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
  static final char[] SYMBOLS = {'A', 'G', 'C', 'T'};

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    for (int tc = 1; ; ++tc) {
      int N = sc.nextInt();
      if (N == 0) {
        break;
      }

      String[] P = new String[N];
      for (int i = 0; i < P.length; ++i) {
        P[i] = sc.next();
      }
      String S = sc.next();

      System.out.println(String.format("Case %d: %d", tc, solve(P, S)));
    }

    sc.close();
  }

  static int solve(String[] P, String S) {
    List<String> prefixes = buildPrefixes(P);
    boolean[] valids = buildValids(P, prefixes);
    int[][] nexts = buildNexts(prefixes);

    int[][] dp = new int[S.length() + 1][prefixes.size()];
    for (int i = 0; i < dp.length; ++i) {
      Arrays.fill(dp[i], Integer.MAX_VALUE);
    }
    dp[0][0] = 0;

    for (int t = 0; t < dp.length - 1; ++t) {
      for (int i = 0; i < prefixes.size(); ++i) {
        if (valids[i] && dp[t][i] != Integer.MAX_VALUE) {
          for (int j = 0; j < SYMBOLS.length; ++j) {
            dp[t + 1][nexts[i][j]] =
                Math.min(dp[t + 1][nexts[i][j]], dp[t][i] + ((S.charAt(t) == SYMBOLS[j]) ? 0 : 1));
          }
        }
      }
    }

    int result = Integer.MAX_VALUE;
    for (int i = 0; i < prefixes.size(); ++i) {
      if (valids[i]) {
        result = Math.min(result, dp[S.length()][i]);
      }
    }

    return (result == Integer.MAX_VALUE) ? -1 : result;
  }

  static int[][] buildNexts(List<String> prefixes) {
    int[][] nexts = new int[prefixes.size()][SYMBOLS.length];
    for (int i = 0; i < prefixes.size(); ++i) {
      for (int j = 0; j < SYMBOLS.length; ++j) {
        String s = prefixes.get(i) + SYMBOLS[j];
        while (true) {
          nexts[i][j] = Collections.binarySearch(prefixes, s);
          if (nexts[i][j] >= 0) {
            break;
          }

          s = s.substring(1);
        }
      }
    }

    return nexts;
  }

  static boolean[] buildValids(String[] P, List<String> prefixes) {
    boolean[] result = new boolean[prefixes.size()];
    Arrays.fill(result, true);

    for (int i = 0; i < result.length; ++i) {
      for (int j = 0; j < P.length; ++j) {
        if (P[j].length() <= prefixes.get(i).length()
            && prefixes.get(i).substring(prefixes.get(i).length() - P[j].length()).equals(P[j])) {
          result[i] = false;
        }
      }
    }

    return result;
  }

  static List<String> buildPrefixes(String[] P) {
    Set<String> prefixes = new HashSet<String>();
    for (String p : P) {
      for (int i = 0; i <= p.length(); ++i) {
        prefixes.add(p.substring(0, i));
      }
    }

    List<String> result = new ArrayList<String>(prefixes);
    Collections.sort(result);

    return result;
  }
}