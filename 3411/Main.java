import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int m = sc.nextInt();
    int[] a = new int[m];
    int[] b = new int[m];
    int[] c = new int[m];
    int[] P = new int[m];
    int[] R = new int[m];
    for (int i = 0; i < m; ++i) {
      a[i] = sc.nextInt();
      b[i] = sc.nextInt();
      c[i] = sc.nextInt();
      P[i] = sc.nextInt();
      R[i] = sc.nextInt();
    }

    System.out.println(solve(N, a, b, c, P, R));

    sc.close();
  }

  static String solve(int N, int[] a, int[] b, int[] c, int[] P, int[] R) {
    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[N];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < a.length; ++i) {
      edgeLists[a[i] - 1].add(i);
    }

    int[][] distances = new int[N][1 << N];
    for (int i = 0; i < distances.length; ++i) {
      Arrays.fill(distances[i], Integer.MAX_VALUE);
    }

    PriorityQueue<Element> pq =
        new PriorityQueue<Element>(
            1,
            new Comparator<Element>() {
              public int compare(Element e1, Element e2) {
                return e1.distance - e2.distance;
              }
            });
    pq.offer(new Element(0, 1, 0));
    while (!pq.isEmpty()) {
      Element head = pq.poll();
      if (head.distance < distances[head.city][head.visitedMask]) {
        distances[head.city][head.visitedMask] = head.distance;

        for (int edge : edgeLists[head.city]) {
          int nextCity = b[edge] - 1;
          int nextVisitedMask = head.visitedMask | (1 << nextCity);

          if (distances[nextCity][nextVisitedMask] == Integer.MAX_VALUE) {
            pq.offer(
                new Element(
                    nextCity,
                    nextVisitedMask,
                    head.distance
                        + ((((head.visitedMask >> (c[edge] - 1)) & 1) == 1) ? P[edge] : R[edge])));
          }
        }
      }
    }

    int result = Integer.MAX_VALUE;
    for (int distance : distances[N - 1]) {
      result = Math.min(result, distance);
    }

    return (result == Integer.MAX_VALUE) ? "impossible" : String.valueOf(result);
  }
}

class Element {
  int city;
  int visitedMask;
  int distance;

  Element(int city, int visitedMask, int distance) {
    this.city = city;
    this.visitedMask = visitedMask;
    this.distance = distance;
  }
}