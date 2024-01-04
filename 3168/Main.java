// TLE on POJ. AC on Baekjoon OJ: https://www.acmicpc.net/source/71176961

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int N = Integer.parseInt(st.nextToken());
    int[] A = new int[N];
    int[] B = new int[N];
    int[] C = new int[N];
    int[] D = new int[N];
    for (int i = 0; i < N; ++i) {
      st = new StringTokenizer(br.readLine());
      A[i] = Integer.parseInt(st.nextToken());
      B[i] = Integer.parseInt(st.nextToken());
      C[i] = Integer.parseInt(st.nextToken());
      D[i] = Integer.parseInt(st.nextToken());
    }

    System.out.println(solve(A, B, C, D));
  }

  static int solve(int[] A, int[] B, int[] C, int[] D) {
    int N = A.length;

    Map<Integer, List<Event>> xToEvents = new HashMap<Integer, List<Event>>();
    Map<Integer, List<Event>> yToEvents = new HashMap<Integer, List<Event>>();
    for (int i = 0; i < N; ++i) {
      addMap(xToEvents, A[i], new Event(i, B[i], true));
      addMap(xToEvents, A[i], new Event(i, D[i], false));

      addMap(xToEvents, C[i], new Event(i, B[i], true));
      addMap(xToEvents, C[i], new Event(i, D[i], false));

      addMap(yToEvents, B[i], new Event(i, A[i], true));
      addMap(yToEvents, B[i], new Event(i, C[i], false));

      addMap(yToEvents, D[i], new Event(i, A[i], true));
      addMap(yToEvents, D[i], new Event(i, C[i], false));
    }

    Comparator<Event> eventComparator =
        new Comparator<Event>() {
          @Override
          public int compare(Event e1, Event e2) {
            if (e1.value != e2.value) {
              return e1.value - e2.value;
            }

            return -Boolean.valueOf(e1.inOrOut).compareTo(e2.inOrOut);
          }
        };

    boolean[] expandables = new boolean[N];
    Arrays.fill(expandables, true);
    for (List<Event> events : xToEvents.values()) {
      check(expandables, eventComparator, events);
    }
    for (List<Event> events : yToEvents.values()) {
      check(expandables, eventComparator, events);
    }

    int result = 0;
    for (boolean expandable : expandables) {
      if (expandable) {
        ++result;
      }
    }

    return result;
  }

  static void check(boolean[] expandables, Comparator<Event> eventComparator, List<Event> events) {
    Collections.sort(events, eventComparator);

    int depth = 0;
    boolean overlapping = false;
    for (Event event : events) {
      if (event.inOrOut) {
        ++depth;
      } else {
        if (depth != 1) {
          overlapping = true;
        }
        if (overlapping) {
          expandables[event.index] = false;
        }

        --depth;
        if (depth == 0) {
          overlapping = false;
        }
      }
    }
  }

  static void addMap(Map<Integer, List<Event>> keyToEvents, int key, Event event) {
    if (!keyToEvents.containsKey(key)) {
      keyToEvents.put(key, new ArrayList<Event>());
    }
    keyToEvents.get(key).add(event);
  }
}

class Event {
  int index;
  int value;
  boolean inOrOut;

  Event(int index, int value, boolean inOrOut) {
    this.index = index;
    this.value = value;
    this.inOrOut = inOrOut;
  }
}