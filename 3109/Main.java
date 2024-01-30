import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int n = Integer.parseInt(st.nextToken());
    int[] xs = new int[n];
    int[] ys = new int[n];
    for (int i = 0; i < n; ++i) {
      st = new StringTokenizer(br.readLine());
      xs[i] = Integer.parseInt(st.nextToken());
      ys[i] = Integer.parseInt(st.nextToken());
    }

    System.out.println(solve(xs, ys));
  }

  static int solve(int[] xs, int[] ys) {
    xs = compress(xs);

    Map<Integer, Integer> yToMinX = new TreeMap<Integer, Integer>();
    Map<Integer, Integer> yToMaxX = new HashMap<Integer, Integer>();
    Map<Integer, Integer> xToMinY = new HashMap<Integer, Integer>();
    Map<Integer, Integer> xToMaxY = new HashMap<Integer, Integer>();
    for (int i = 0; i < xs.length; ++i) {
      if (!yToMinX.containsKey(ys[i])) {
        yToMinX.put(ys[i], Integer.MAX_VALUE);
        yToMaxX.put(ys[i], Integer.MIN_VALUE);
      }
      yToMinX.put(ys[i], Math.min(yToMinX.get(ys[i]), xs[i]));
      yToMaxX.put(ys[i], Math.max(yToMaxX.get(ys[i]), xs[i]));

      if (!xToMinY.containsKey(xs[i])) {
        xToMinY.put(xs[i], Integer.MAX_VALUE);
        xToMaxY.put(xs[i], Integer.MIN_VALUE);
      }
      xToMinY.put(xs[i], Math.min(xToMinY.get(xs[i]), ys[i]));
      xToMaxY.put(xs[i], Math.max(xToMaxY.get(xs[i]), ys[i]));
    }

    List<Line> lines = new ArrayList<Line>();
    for (int x : xToMinY.keySet()) {
      lines.add(new Line(x, xToMinY.get(x), xToMaxY.get(x)));
    }
    Collections.sort(
        lines,
        new Comparator<Line>() {
          @Override
          public int compare(Line l1, Line l2) {
            return l1.minY - l2.minY;
          }
        });

    int result = 0;
    int lineIndex = 0;
    PriorityQueue<Line> pq =
        new PriorityQueue<Line>(
            1,
            new Comparator<Line>() {
              @Override
              public int compare(Line l1, Line l2) {
                return l1.maxY - l2.maxY;
              }
            });
    int[] binaryIndexedTree = new int[Integer.highestOneBit(xToMinY.size()) * 2 + 1];
    for (int y : yToMinX.keySet()) {
      int minX = yToMinX.get(y);
      int maxX = yToMaxX.get(y);

      result += (minX == maxX) ? 1 : 2;

      while (lineIndex != lines.size() && lines.get(lineIndex).minY <= y) {
        pq.offer(lines.get(lineIndex));
        add(binaryIndexedTree, lines.get(lineIndex).x, 1);

        ++lineIndex;
      }

      while (!pq.isEmpty() && pq.peek().maxY < y) {
        Line head = pq.poll();
        add(binaryIndexedTree, head.x, -1);
      }

      if (minX + 2 <= maxX) {
        result += computeSum(binaryIndexedTree, maxX - 1) - computeSum(binaryIndexedTree, minX);
      }
    }

    return result;
  }

  static void add(int[] binaryIndexedTree, int i, int x) {
    while (i < binaryIndexedTree.length) {
      binaryIndexedTree[i] += x;
      i += i & -i;
    }
  }

  static int computeSum(int[] binaryIndexedTree, int i) {
    int result = 0;
    while (i != 0) {
      result += binaryIndexedTree[i];
      i -= i & -i;
    }

    return result;
  }

  static int[] compress(int[] xs) {
    SortedSet<Integer> values = new TreeSet<Integer>();
    for (int x : xs) {
      values.add(x);
    }

    Map<Integer, Integer> valueToMapped = new HashMap<Integer, Integer>();
    for (int value : values) {
      valueToMapped.put(value, valueToMapped.size() + 1);
    }

    int[] result = new int[xs.length];
    for (int i = 0; i < result.length; ++i) {
      result[i] = valueToMapped.get(xs[i]);
    }

    return result;
  }
}

class Line {
  int x;
  int minY;
  int maxY;

  Line(int x, int minY, int maxY) {
    this.x = x;
    this.minY = minY;
    this.maxY = maxY;
  }
}