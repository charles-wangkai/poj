// TLE on POJ. AC on Bailian: http://bailian.openjudge.cn/practice/2482

// https://tool.4xseo.com/a/35105.html

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (sc.hasNextInt()) {
      int n = sc.nextInt();
      int W = sc.nextInt();
      int H = sc.nextInt();
      int[] xs = new int[n];
      int[] ys = new int[n];
      int[] cs = new int[n];
      for (int i = 0; i < n; ++i) {
        xs[i] = sc.nextInt();
        ys[i] = sc.nextInt();
        cs[i] = sc.nextInt();
      }

      System.out.println(solve(xs, ys, cs, W, H));
    }

    sc.close();
  }

  static long solve(int[] xs, int[] ys, int[] cs, int W, int H) {
    SortedSet<Integer> sortedYs = new TreeSet<Integer>();
    for (int y : ys) {
      sortedYs.add(y - H + 1);
      sortedYs.add(y);
    }
    Map<Integer, Integer> yToCompressed = new HashMap<Integer, Integer>();
    for (int y : sortedYs) {
      yToCompressed.put(y, yToCompressed.size());
    }

    List<Event> events = new ArrayList<Event>();
    for (int i = 0; i < xs.length; ++i) {
      events.add(
          new Event(xs[i] - W, yToCompressed.get(ys[i] - H + 1), yToCompressed.get(ys[i]), cs[i]));
      events.add(
          new Event(xs[i], yToCompressed.get(ys[i] - H + 1), yToCompressed.get(ys[i]), -cs[i]));
    }
    Collections.sort(
        events,
        new Comparator<Event>() {
          public int compare(Event e1, Event e2) {
            if (e1.x != e2.x) {
              return e1.x - e2.x;
            }

            return e1.delta - e2.delta;
          }
        });

    long result = 0;
    long[] sums = new long[yToCompressed.size()];
    for (Event event : events) {
      for (int i = event.minY; i <= event.maxY; ++i) {
        sums[i] += event.delta;
      }

      result = Math.max(result, computeMax(sums));
    }

    return result;
  }

  static long computeMax(long[] a) {
    long result = Long.MIN_VALUE;
    for (long ai : a) {
      result = Math.max(result, ai);
    }

    return result;
  }
}

class Event {
  int x;
  int minY;
  int maxY;
  int delta;

  Event(int x, int minY, int maxY, int delta) {
    this.x = x;
    this.minY = minY;
    this.maxY = maxY;
    this.delta = delta;
  }
}