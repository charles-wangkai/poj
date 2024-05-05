import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
  static int minRemovedCount;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int n = sc.nextInt();
      int k = sc.nextInt();
      int[] missings = new int[k];
      for (int i = 0; i < missings.length; ++i) {
        missings[i] = sc.nextInt();
      }

      System.out.println(solve(n, missings));
    }

    sc.close();
  }

  static int solve(int n, int[] missings) {
    List<Set<Integer>> matchIndicesInSquares = buildMatchIndicesInSquares(n);
    int[] maxMatchIndexInSquares = buildMaxMatchIndexInSquares(matchIndicesInSquares);

    boolean[] destroyed = new boolean[matchIndicesInSquares.size()];
    for (int missing : missings) {
      for (int i = 0; i < destroyed.length; ++i) {
        if (!destroyed[i] && matchIndicesInSquares.get(i).contains(missing - 1)) {
          destroyed[i] = true;
        }
      }
    }

    minRemovedCount = Integer.MAX_VALUE;
    search(n, matchIndicesInSquares, maxMatchIndexInSquares, destroyed, 0, 0);

    return minRemovedCount;
  }

  static void search(
      int n,
      List<Set<Integer>> matchIndicesInSquares,
      int[] maxMatchIndexInSquares,
      boolean[] destroyed,
      int removedCount,
      int matchIndex) {
    if (removedCount < minRemovedCount) {
      if (matchIndex == 2 * n * (n + 1)) {
        minRemovedCount = removedCount;
      } else {
        if (!mustRemove(maxMatchIndexInSquares, destroyed, matchIndex)) {
          search(
              n,
              matchIndicesInSquares,
              maxMatchIndexInSquares,
              destroyed,
              removedCount,
              matchIndex + 1);
        }

        List<Integer> destroyedSquares = new ArrayList<Integer>();
        for (int i = 0; i < destroyed.length; ++i) {
          if (!destroyed[i] && matchIndicesInSquares.get(i).contains(matchIndex)) {
            destroyed[i] = true;
            destroyedSquares.add(i);
          }
        }

        if (!destroyedSquares.isEmpty()) {
          search(
              n,
              matchIndicesInSquares,
              maxMatchIndexInSquares,
              destroyed,
              removedCount + 1,
              matchIndex + 1);
        }

        for (int square : destroyedSquares) {
          destroyed[square] = false;
        }
      }
    }
  }

  static boolean mustRemove(int[] maxMatchIndexInSquares, boolean[] destroyed, int matchIndex) {
    for (int i = 0; i < destroyed.length; ++i) {
      if (!destroyed[i] && matchIndex == maxMatchIndexInSquares[i]) {
        return true;
      }
    }

    return false;
  }

  static int[] buildMaxMatchIndexInSquares(List<Set<Integer>> matchIndicesInSquares) {
    int[] result = new int[matchIndicesInSquares.size()];
    for (int i = 0; i < result.length; ++i) {
      for (int matchIndex : matchIndicesInSquares.get(i)) {
        result[i] = Math.max(result[i], matchIndex);
      }
    }

    return result;
  }

  static List<Set<Integer>> buildMatchIndicesInSquares(int n) {
    List<Set<Integer>> result = new ArrayList<Set<Integer>>();
    for (int size = 1; size <= n; ++size) {
      for (int beginR = 0; beginR <= n - size; ++beginR) {
        for (int beginC = 0; beginC <= n - size; ++beginC) {
          Set<Integer> matchIndices = new HashSet<Integer>();
          for (int i = 0; i < size; ++i) {
            matchIndices.add(getHorizontalMatchIndex(n, beginR, beginC + i));
            matchIndices.add(getVerticalMatchIndex(n, beginR + i, beginC));
            matchIndices.add(getHorizontalMatchIndex(n, beginR + size, beginC + i));
            matchIndices.add(getVerticalMatchIndex(n, beginR + i, beginC + size));
          }

          result.add(matchIndices);
        }
      }
    }

    return result;
  }

  static int getHorizontalMatchIndex(int n, int r, int c) {
    return (2 * n + 1) * r + c;
  }

  static int getVerticalMatchIndex(int n, int r, int c) {
    return (2 * n + 1) * r + n + c;
  }
}