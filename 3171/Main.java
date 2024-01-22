import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int M = sc.nextInt();
    int E = sc.nextInt();
    int[] T1 = new int[N];
    int[] T2 = new int[N];
    int[] S = new int[N];
    for (int i = 0; i < N; ++i) {
      T1[i] = sc.nextInt();
      T2[i] = sc.nextInt();
      S[i] = sc.nextInt();
    }

    System.out.println(solve(T1, T2, S, M, E));

    sc.close();
  }

  static long solve(int[] T1, int[] T2, int[] S, int M, int E) {
    Map<Integer, List<Integer>> leftToEdges = new HashMap<Integer, List<Integer>>();
    for (int i = 0; i < T1.length; ++i) {
      if (!leftToEdges.containsKey(T1[i])) {
        leftToEdges.put(T1[i], new ArrayList<Integer>());
      }
      leftToEdges.get(T1[i]).add(i);
    }

    PriorityQueue<Element> pq =
        new PriorityQueue<Element>(
            1,
            new Comparator<Element>() {
              public int compare(Element e1, Element e2) {
                return Long.signum(e1.minSalary - e2.minSalary);
              }
            });
    pq.offer(new Element(M - 1, 0));

    for (int i = M; i <= E; ++i) {
      long prevMinSalary = getMinSalary(pq, i - 1);
      if (prevMinSalary == -1) {
        return -1;
      }

      if (leftToEdges.containsKey(i)) {
        for (int edge : leftToEdges.get(i)) {
          pq.offer(new Element(T2[edge], prevMinSalary + S[edge]));
        }
      }
    }

    return getMinSalary(pq, E);
  }

  static long getMinSalary(PriorityQueue<Element> pq, int right) {
    while (!pq.isEmpty() && pq.peek().right < right) {
      pq.poll();
    }

    return pq.isEmpty() ? -1 : pq.peek().minSalary;
  }
}

class Element {
  int right;
  long minSalary;

  Element(int right, long minSalary) {
    this.right = right;
    this.minSalary = minSalary;
  }
}