// TLE on POJ. AC on AOJ: https://judge.u-aizu.ac.jp/onlinejudge/review.jsp?rid=9960068

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Main {
  static final int[] R_OFFSETS = {0, -1, 0, 1, 0};
  static final int[] C_OFFSETS = {0, 0, 1, 0, -1};
  static final int BASE = 16;

  static short[] distances = new short[pow(BASE, 6)];

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int w = sc.nextInt();
      int h = sc.nextInt();
      int n = sc.nextInt();
      if (w == 0 && h == 0 && n == 0) {
        break;
      }

      char[][] house = new char[h][w];
      sc.nextLine();
      for (int r = 0; r < h; ++r) {
        String line = sc.nextLine();
        for (int c = 0; c < w; ++c) {
          house[r][c] = line.charAt(c);
        }
      }

      System.out.println(solve(house, n));
    }

    sc.close();
  }

  static int solve(char[][] house, int n) {
    int h = house.length;
    int w = house[0].length;

    int[] initialPoints = new int[n];
    int[] targetPoints = new int[n];
    for (int r = 0; r < h; ++r) {
      for (int c = 0; c < w; ++c) {
        if (house[r][c] != '#' && house[r][c] != ' ') {
          (Character.isLowerCase(house[r][c]) ? initialPoints : targetPoints)
                  [Character.toLowerCase(house[r][c]) - 'a'] =
              toPoint(r, c);
        }
      }
    }
    int initialState = toState(initialPoints);
    int targetState = toState(targetPoints);

    Arrays.fill(distances, (short) -1);
    distances[initialState] = 0;

    Queue<Integer> queue = new ArrayDeque<Integer>();
    queue.offer(initialState);

    while (true) {
      int state = queue.poll();
      if (state == targetState) {
        return distances[targetState];
      }

      int[] points = toPoints(n, state);

      for (int nextState : buildNextStates(house, points)) {
        if (distances[nextState] == -1) {
          distances[nextState] = (short) (distances[state] + 1);
          queue.offer(nextState);
        }
      }
    }
  }

  static int pow(int base, int exponent) {
    int result = 1;
    for (int i = 0; i < exponent; ++i) {
      result *= base;
    }

    return result;
  }

  static List<Integer> buildNextStates(char[][] house, int[] points) {
    List<Integer> result = new ArrayList<Integer>();
    search(result, house, points, new int[points.length], 0);

    return result;
  }

  static void search(
      List<Integer> nextStates, char[][] house, int[] points, int[] nextPoints, int index) {
    int h = house.length;
    int w = house[0].length;

    if (index == nextPoints.length) {
      nextStates.add(toState(nextPoints));
    } else {
      for (int i = 0; i < R_OFFSETS.length; ++i) {
        int adjR = getR(points[index]) + R_OFFSETS[i];
        int adjC = getC(points[index]) + C_OFFSETS[i];
        nextPoints[index] = toPoint(adjR, adjC);
        if (adjR >= 0
            && adjR < h
            && adjC >= 0
            && adjC < w
            && house[adjR][adjC] != '#'
            && check(points, nextPoints, index)) {
          search(nextStates, house, points, nextPoints, index + 1);
        }
      }
    }
  }

  static boolean check(int[] points, int[] nextPoints, int index) {
    for (int i = 0; i < index; ++i) {
      if (nextPoints[index] == nextPoints[i]
          || (nextPoints[i] == points[index] && nextPoints[index] == points[i])) {
        return false;
      }
    }

    return true;
  }

  static int toState(int[] points) {
    int result = 0;
    for (int point : points) {
      result = result * BASE + getR(point);
      result = result * BASE + getC(point);
    }

    return result;
  }

  static int[] toPoints(int n, int state) {
    int[] result = new int[n];
    for (int i = result.length - 1; i >= 0; --i) {
      int c = state % BASE;
      state /= BASE;

      int r = state % BASE;
      state /= BASE;

      result[i] = toPoint(r, c);
    }

    return result;
  }

  static int toPoint(int r, int c) {
    return r * BASE + c;
  }

  static int getR(int point) {
    return point / BASE;
  }

  static int getC(int point) {
    return point % BASE;
  }
}
