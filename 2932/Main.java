import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int N = Integer.parseInt(st.nextToken());
    double[] R = new double[N];
    double[] x = new double[N];
    double[] y = new double[N];
    for (int i = 0; i < N; ++i) {
      st = new StringTokenizer(br.readLine());
      R[i] = Double.parseDouble(st.nextToken());
      x[i] = Double.parseDouble(st.nextToken());
      y[i] = Double.parseDouble(st.nextToken());
    }

    System.out.println(solve(R, x, y));
  }

  static String solve(double[] R, double[] x, final double[] y) {
    List<Event> events = new ArrayList<Event>();
    for (int i = 0; i < R.length; ++i) {
      events.add(new Event(x[i] - R[i], i, true));
      events.add(new Event(x[i] + R[i], i, false));
    }
    Collections.sort(
        events,
        new Comparator<Event>() {
          public int compare(Event e1, Event e2) {
            return Double.compare(e1.x, e2.x);
          }
        });

    List<Integer> worshipIndices = new ArrayList<Integer>();
    NavigableSet<Integer> outerIndices =
        new TreeSet<Integer>(
            new Comparator<Integer>() {
              public int compare(Integer index1, Integer index2) {
                return (y[index1] != y[index2])
                    ? Double.compare(y[index1], y[index2])
                    : (index1 - index2);
              }
            });
    for (Event event : events) {
      if (event.leftOrRight) {
        Integer floorIndex = outerIndices.floor(event.index);
        Integer ceilingIndex = outerIndices.ceiling(event.index);
        if ((floorIndex == null || !isInside(R, x, y, event.index, floorIndex))
            && (ceilingIndex == null || !isInside(R, x, y, event.index, ceilingIndex))) {
          worshipIndices.add(event.index);
          outerIndices.add(event.index);
        }
      } else {
        outerIndices.remove(event.index);
      }
    }
    Collections.sort(worshipIndices);

    StringBuilder result = new StringBuilder().append(worshipIndices.size()).append("\n");
    for (int i = 0; i < worshipIndices.size(); ++i) {
      if (i != 0) {
        result.append(" ");
      }

      result.append(worshipIndices.get(i) + 1);
    }

    return result.toString();
  }

  static boolean isInside(double[] R, double[] x, double[] y, int index1, int index2) {
    return (x[index1] - x[index2]) * (x[index1] - x[index2])
            + (y[index1] - y[index2]) * (y[index1] - y[index2])
        <= R[index2] * R[index2];
  }
}

class Event {
  double x;
  int index;
  boolean leftOrRight;

  Event(double x, int index, boolean leftOrRight) {
    this.x = x;
    this.index = index;
    this.leftOrRight = leftOrRight;
  }
}