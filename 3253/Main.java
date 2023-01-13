import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int[] L = new int[N];
    for (int i = 0; i < L.length; ++i) {
      L[i] = sc.nextInt();
    }

    System.out.println(solve(L));

    sc.close();
  }

  static long solve(int[] L) {
    PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
    for (int Li : L) {
      pq.offer(Li);
    }

    long result = 0;
    while (pq.size() != 1) {
      int sum = pq.poll() + pq.poll();
      result += sum;
      pq.offer(sum);
    }

    return result;
  }
}
