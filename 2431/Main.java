import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int[] distances = new int[N];
    int[] fuels = new int[N];
    for (int i = 0; i < N; ++i) {
      distances[i] = sc.nextInt();
      fuels[i] = sc.nextInt();
    }
    int L = sc.nextInt();
    int P = sc.nextInt();

    System.out.println(solve(distances, fuels, L, P));

    sc.close();
  }

  static int solve(int[] distances, int[] fuels, int L, int P) {
    int result = 0;
    int rest = P;
    int prevPos = 0;
    PriorityQueue<Integer> pq = new PriorityQueue<Integer>(1, Collections.reverseOrder());

    List<Stop> stops = new ArrayList<Stop>();
    for (int i = 0; i < distances.length; ++i) {
      stops.add(new Stop(L - distances[i], fuels[i]));
    }
    stops.add(new Stop(L, 0));
    Collections.sort(
        stops,
        new Comparator<Stop>() {
          public int compare(Stop stop1, Stop stop2) {
            return stop1.pos - stop2.pos;
          }
        });

    for (Stop stop : stops) {
      while (rest < stop.pos - prevPos) {
        if (pq.isEmpty()) {
          return -1;
        }

        rest += pq.poll();
        ++result;
      }

      pq.offer(stop.fuel);
      rest -= stop.pos - prevPos;
      prevPos = stop.pos;
    }

    return result;
  }
}

class Stop {
  int pos;
  int fuel;

  Stop(int pos, int fuel) {
    this.pos = pos;
    this.fuel = fuel;
  }
}
