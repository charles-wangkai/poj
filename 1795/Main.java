// MLE on POJ. AC on Bailian: http://bailian.openjudge.cn/practice/1795

// https://www.hankcs.com/program/algorithm/poj-1795-dna-laboratory.html

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  static final String INF = "Z";

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int t = sc.nextInt();
    for (int tc = 1; tc <= t; ++tc) {
      int n = sc.nextInt();
      String[] words = new String[n];
      for (int i = 0; i < words.length; ++i) {
        words[i] = sc.next();
      }

      System.out.println(String.format("Scenario #%d:\n%s\n", tc, solve(words)));
    }

    sc.close();
  }

  static String solve(String[] words) {
    List<String> parts = removeContained(words);

    int[][] costs = new int[parts.size()][parts.size()];
    for (int i = 0; i < parts.size(); ++i) {
      for (int j = 0; j < parts.size(); ++j) {
        if (j != i) {
          costs[i][j] = computeCost(parts.get(i), parts.get(j));
        }
      }
    }

    int[][] lengths = new int[1 << parts.size()][parts.size()];
    for (int i = 0; i < lengths.length; ++i) {
      Arrays.fill(lengths[i], Integer.MAX_VALUE);
    }

    for (int mask = 1; mask < 1 << parts.size(); ++mask) {
      for (int i = 0; i < parts.size(); ++i) {
        if (((mask >> i) & 1) == 1) {
          int prevMask = mask - (1 << i);
          if (prevMask == 0) {
            lengths[mask][i] = parts.get(i).length();
          } else {
            for (int j = 0; j < parts.size(); ++j) {
              if (((prevMask >> j) & 1) == 1) {
                lengths[mask][i] = Math.min(lengths[mask][i], lengths[prevMask][j] + costs[j][i]);
              }
            }
          }
        }
      }
    }

    boolean[][] reachables = new boolean[1 << parts.size()][parts.size()];

    int minLength = Integer.MAX_VALUE;
    for (int i = 0; i < parts.size(); ++i) {
      minLength = Math.min(minLength, lengths[(1 << parts.size()) - 1][i]);
    }
    for (int i = 0; i < parts.size(); ++i) {
      if (lengths[(1 << parts.size()) - 1][i] == minLength) {
        reachables[(1 << parts.size()) - 1][i] = true;
      }
    }

    for (int mask = (1 << parts.size()) - 1; mask > 0; --mask) {
      for (int i = 0; i < parts.size(); ++i) {
        if (reachables[mask][i]) {
          int prevMask = mask - (1 << i);
          for (int j = 0; j < parts.size(); ++j) {
            if (((prevMask >> j) & 1) == 1
                && lengths[prevMask][j] + costs[j][i] == lengths[mask][i]) {
              reachables[prevMask][j] = true;
            }
          }
        }
      }
    }

    String result = INF;
    int index = -1;
    for (int i = 0; i < parts.size(); ++i) {
      if (reachables[1 << i][i] && parts.get(i).compareTo(result) < 0) {
        result = parts.get(i);
        index = i;
      }
    }

    int appendedMask = 1 << index;
    while (appendedMask != (1 << parts.size()) - 1) {
      String tail = INF;
      int nextIndex = -1;
      for (int i = 0; i < parts.size(); ++i) {
        if (((appendedMask >> i) & 1) == 0
            && reachables[appendedMask + (1 << i)][i]
            && lengths[appendedMask][index] + costs[index][i] == lengths[appendedMask + (1 << i)][i]
            && parts.get(i).substring(parts.get(i).length() - costs[index][i]).compareTo(tail)
                < 0) {
          tail = parts.get(i).substring(parts.get(i).length() - costs[index][i]);
          nextIndex = i;
        }
      }

      index = nextIndex;
      appendedMask += 1 << nextIndex;
      result += tail;
    }

    return result;
  }

  static int computeCost(String s1, String s2) {
    for (int i = s2.length() - 1; ; --i) {
      if (s1.endsWith(s2.substring(0, i))) {
        return s2.length() - i;
      }
    }
  }

  static List<String> removeContained(String[] words) {
    List<String> result = new ArrayList<String>();
    for (int i = 0; i < words.length; ++i) {
      boolean contained = false;
      for (int j = 0; j < words.length; ++j) {
        if (words[j].length() > words[i].length() && words[j].contains(words[i])) {
          contained = true;
        }
      }

      if (!contained && !result.contains(words[i])) {
        result.add(words[i]);
      }
    }

    return result;
  }
}