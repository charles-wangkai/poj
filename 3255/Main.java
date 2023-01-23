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
    int R = sc.nextInt();
    int[] A = new int[R];
    int[] B = new int[R];
    int[] D = new int[R];
    for (int i = 0; i < R; ++i) {
      A[i] = sc.nextInt() - 1;
      B[i] = sc.nextInt() - 1;
      D[i] = sc.nextInt();
    }

    System.out.println(solve(N, A, B, D));

    sc.close();
  }

  static int solve(int N, int[] A, int[] B, int[] D) {
    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[N];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < A.length; ++i) {
      edgeLists[A[i]].add(i);
      edgeLists[B[i]].add(i);
    }

    int[] distances = new int[N];
    Arrays.fill(distances, Integer.MAX_VALUE);
    int[] counts = new int[N];
    PriorityQueue<Element> pq =
        new PriorityQueue<Element>(
            1,
            new Comparator<Element>() {
              public int compare(Element e1, Element e2) {
                return e1.distance - e2.distance;
              }
            });
    pq.offer(new Element(0, 0));
    while (!pq.isEmpty()) {
      Element head = pq.poll();
      if (counts[head.node] != 2 && head.distance != distances[head.node]) {
        ++counts[head.node];
        distances[head.node] = head.distance;

        for (int edge : edgeLists[head.node]) {
          int other = (head.node == A[edge]) ? B[edge] : A[edge];
          if (counts[other] != 2) {
            pq.offer(new Element(other, distances[head.node] + D[edge]));
          }
        }
      }
    }

    return distances[distances.length - 1];
  }
}

class Element {
  int node;
  int distance;

  Element(int node, int distance) {
    this.node = node;
    this.distance = distance;
  }
}
