import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int[] A = new int[N];
    int[] B = new int[N];
    for (int i = 0; i < N; ++i) {
      A[i] = sc.nextInt();
      B[i] = sc.nextInt();
    }

    System.out.println(solve(A, B));

    sc.close();
  }

  static String solve(final int[] A, int[] B) {
    Integer[] sortedIndices = new Integer[A.length];
    for (int i = 0; i < sortedIndices.length; ++i) {
      sortedIndices[i] = i;
    }
    Arrays.sort(
        sortedIndices,
        new Comparator<Integer>() {
          public int compare(Integer i1, Integer i2) {
            return A[i1] - A[i2];
          }
        });

    int stallCount = 0;
    int[] assignments = new int[A.length];
    PriorityQueue<Element> occupied =
        new PriorityQueue<Element>(
            1,
            new Comparator<Element>() {
              public int compare(Element e1, Element e2) {
                return e1.endTime - e2.endTime;
              }
            });
    Stack<Integer> availables = new Stack<Integer>();
    for (int index : sortedIndices) {
      while (!occupied.isEmpty() && occupied.peek().endTime < A[index]) {
        availables.push(occupied.poll().stall);
      }

      if (availables.empty()) {
        ++stallCount;
        availables.push(stallCount);
      }

      assignments[index] = availables.pop();
      occupied.offer(new Element(assignments[index], B[index]));
    }

    StringBuilder result = new StringBuilder().append(stallCount);
    for (int assignment : assignments) {
      result.append("\n").append(assignment);
    }

    return result.toString();
  }
}

class Element {
  int stall;
  int endTime;

  Element(int stall, int endTime) {
    this.stall = stall;
    this.endTime = endTime;
  }
}
