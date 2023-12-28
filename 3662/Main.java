import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int P = sc.nextInt();
    int K = sc.nextInt();
    int[] A = new int[P];
    int[] B = new int[P];
    int[] L = new int[P];
    for (int i = 0; i < P; ++i) {
      A[i] = sc.nextInt();
      B[i] = sc.nextInt();
      L[i] = sc.nextInt();
    }

    System.out.println(solve(N, A, B, L, K));

    sc.close();
  }

  static int solve(int N, int[] A, int[] B, int[] L, int K) {
    int result = -1;
    int lower = 0;
    int upper = Integer.MIN_VALUE;
    for (int Li : L) {
      upper = Math.max(upper, Li);
    }
    while (lower <= upper) {
      int middle = (lower + upper) / 2;
      if (check(N, A, B, L, K, middle)) {
        result = middle;
        upper = middle - 1;
      } else {
        lower = middle + 1;
      }
    }

    return result;
  }

  static boolean check(int N, int[] A, int[] B, int[] L, int K, int maxPaidLength) {
    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[N];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < A.length; ++i) {
      edgeLists[A[i] - 1].add(i);
      edgeLists[B[i] - 1].add(i);
    }

    int[] distances = new int[N];
    Arrays.fill(distances, Integer.MAX_VALUE);
    Deque<Element> deque = new ArrayDeque<Element>();
    deque.offerFirst(new Element(0, 0));
    while (!deque.isEmpty()) {
      Element head = deque.pollFirst();
      if (distances[head.node] == Integer.MAX_VALUE) {
        distances[head.node] = head.distance;

        for (int edge : edgeLists[head.node]) {
          int other = (A[edge] - 1 == head.node) ? (B[edge] - 1) : (A[edge] - 1);
          if (distances[other] == Integer.MAX_VALUE) {
            if (L[edge] <= maxPaidLength) {
              deque.offerFirst(new Element(other, distances[head.node]));
            } else {
              deque.offerLast(new Element(other, distances[head.node] + 1));
            }
          }
        }
      }
    }

    return distances[N - 1] <= K;
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
