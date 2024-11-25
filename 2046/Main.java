import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class Main {
  static final int SUIT_NUM = 4;
  static final int VALUE_NUM = 7;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int t = sc.nextInt();
    for (int tc = 0; tc < t; ++tc) {
      int[][] layout = new int[SUIT_NUM][VALUE_NUM + 1];
      for (int r = 0; r < layout.length; ++r) {
        for (int c = 1; c < layout[r].length; ++c) {
          layout[r][c] = sc.nextInt();
        }
      }

      System.out.println(solve(layout));
    }

    sc.close();
  }

  static int solve(int[][] layout) {
    for (int r = 0; r < SUIT_NUM; ++r) {
      swap(layout, new Point(r, 0), find(layout, r * 10 + 11).get(0));
    }

    Map<String, Integer> stateToDistance = new HashMap<String, Integer>();
    stateToDistance.put(toState(layout), 0);

    Queue<String> queue = new ArrayDeque<String>();
    queue.offer(toState(layout));

    while (!queue.isEmpty()) {
      String head = queue.poll();
      layout = toLayout(head);
      if (isTarget(layout)) {
        return stateToDistance.get(head);
      }

      List<Point> gaps = find(layout, 0);
      for (Point gap : gaps) {
        int successor = computeSuccessor(layout[gap.r][gap.c - 1]);
        if (successor != -1) {
          Point successorPoint = find(layout, successor).get(0);

          swap(layout, new Point(gap.r, gap.c), successorPoint);

          String state = toState(layout);
          if (!stateToDistance.containsKey(state)) {
            stateToDistance.put(state, stateToDistance.get(head) + 1);
            queue.offer(state);
          }

          swap(layout, new Point(gap.r, gap.c), successorPoint);
        }
      }
    }

    return -1;
  }

  static int computeSuccessor(int card) {
    return (card == 0 || card % 10 == VALUE_NUM) ? -1 : (card + 1);
  }

  static boolean isTarget(int[][] layout) {
    for (int r = 0; r < layout.length; ++r) {
      for (int c = 0; c < layout[r].length; ++c) {
        if (layout[r][c] != ((c == layout[r].length - 1) ? 0 : ((r + 1) * 10 + c + 1))) {
          return false;
        }
      }
    }

    return true;
  }

  static int[][] toLayout(String state) {
    int[][] result = new int[SUIT_NUM][VALUE_NUM + 1];
    String[] fields = state.split(",");
    for (int r = 0; r < result.length; ++r) {
      for (int c = 0; c < result[r].length; ++c) {
        result[r][c] = Integer.parseInt(fields[r * result[0].length + c]);
      }
    }

    return result;
  }

  static String toState(int[][] layout) {
    StringBuilder result = new StringBuilder();
    for (int[] line : layout) {
      for (int card : line) {
        if (result.length() != 0) {
          result.append(",");
        }
        result.append(card);
      }
    }

    return result.toString();
  }

  static List<Point> find(int[][] layout, int card) {
    List<Point> result = new ArrayList<Point>();
    for (int r = 0; r < layout.length; ++r) {
      for (int c = 0; c < layout[r].length; ++c) {
        if (layout[r][c] == card) {
          result.add(new Point(r, c));
        }
      }
    }

    return result;
  }

  static void swap(int[][] layout, Point p1, Point p2) {
    int temp = layout[p1.r][p1.c];
    layout[p1.r][p1.c] = layout[p2.r][p2.c];
    layout[p2.r][p2.c] = temp;
  }
}

class Point {
  int r;
  int c;

  Point(int r, int c) {
    this.r = r;
    this.c = c;
  }
}