import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class Main {
  static Map<Integer, List<Integer>> primeToAdjs;

  public static void main(String[] args) {
    precompute();

    Scanner sc = new Scanner(System.in);

    int t = sc.nextInt();
    for (int tc = 0; tc < t; ++tc) {
      int begin = sc.nextInt();
      int end = sc.nextInt();

      System.out.println(solve(begin, end));
    }

    sc.close();
  }

  static void precompute() {
    primeToAdjs = new HashMap<Integer, List<Integer>>();
    for (int i = 1000; i <= 9999; ++i) {
      if (isPrime(i)) {
        primeToAdjs.put(i, new ArrayList<Integer>());
      }
    }

    for (int prime : primeToAdjs.keySet()) {
      for (int placeValue = 1; placeValue <= 1000; placeValue *= 10) {
        for (int d = (placeValue == 1000) ? 1 : 0; d <= 9; ++d) {
          int adj =
              prime % placeValue + prime / (placeValue * 10) * (placeValue * 10) + d * placeValue;
          if (adj != prime && primeToAdjs.containsKey(adj)) {
            primeToAdjs.get(prime).add(adj);
          }
        }
      }
    }
  }

  static boolean isPrime(int x) {
    for (int i = 2; i * i <= x; ++i) {
      if (x % i == 0) {
        return false;
      }
    }

    return true;
  }

  static String solve(int begin, int end) {
    Map<Integer, Integer> primeToDistance = new HashMap<Integer, Integer>();
    primeToDistance.put(begin, 0);
    Queue<Integer> queue = new LinkedList<Integer>();
    queue.offer(begin);
    while (!queue.isEmpty()) {
      int head = queue.poll();
      for (int adj : primeToAdjs.get(head)) {
        if (!primeToDistance.containsKey(adj)) {
          primeToDistance.put(adj, primeToDistance.get(head) + 1);
          queue.offer(adj);
        }
      }
    }

    return primeToDistance.containsKey(end)
        ? String.valueOf(primeToDistance.get(end))
        : "Impossible";
  }
}
