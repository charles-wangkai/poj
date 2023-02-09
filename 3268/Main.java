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
    int M = sc.nextInt();
    int X = sc.nextInt() - 1;
    int[] A = new int[M];
    int[] B = new int[M];
    int[] T = new int[M];
    for (int i = 0; i < M; ++i) {
      A[i] = sc.nextInt() - 1;
      B[i] = sc.nextInt() - 1;
      T[i] = sc.nextInt();
    }

    System.out.println(solve(N, A, B, T, X));

    sc.close();
  }

  static int solve(int N, int[] A, int[] B, int[] T, int X) {
    @SuppressWarnings("unchecked")
    List<Integer>[] forwardEdgeLists = new List[N];
    for (int i = 0; i < forwardEdgeLists.length; ++i) {
      forwardEdgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < A.length; ++i) {
      forwardEdgeLists[A[i]].add(i);
    }

    int[] forwardDistances = computeDistances(A, B, T, X, forwardEdgeLists);

    @SuppressWarnings("unchecked")
    List<Integer>[] backwardEdgeLists = new List[N];
    for (int i = 0; i < backwardEdgeLists.length; ++i) {
      backwardEdgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < A.length; ++i) {
      backwardEdgeLists[B[i]].add(i);
    }

    int[] backwardDistances = computeDistances(A, B, T, X, backwardEdgeLists);

    int result = -1;
    for (int i = 0; i < forwardDistances.length; ++i) {
      result = Math.max(result, forwardDistances[i] + backwardDistances[i]);
    }

    return result;
  }

  static int[] computeDistances(int[] A, int[] B, int[] T, int X, List<Integer>[] edgeLists) {
    int[] distances = new int[edgeLists.length];
    Arrays.fill(distances, -1);

    PriorityQueue<Element> pq =
        new PriorityQueue<Element>(
            1,
            new Comparator<Element>() {
              public int compare(Element e1, Element e2) {
                return e1.distance - e2.distance;
              }
            });
    pq.offer(new Element(X, 0));

    while (!pq.isEmpty()) {
      Element head = pq.poll();
      if (distances[head.node] == -1) {
        distances[head.node] = head.distance;

        for (int edge : edgeLists[head.node]) {
          int other = (head.node == A[edge]) ? B[edge] : A[edge];
          if (distances[other] == -1) {
            pq.offer(new Element(other, head.distance + T[edge]));
          }
        }
      }
    }

    return distances;
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
