import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int C = sc.nextInt();
    int F = sc.nextInt();
    int[] scores = new int[C];
    int[] aids = new int[C];
    for (int i = 0; i < C; ++i) {
      scores[i] = sc.nextInt();
      aids[i] = sc.nextInt();
    }

    System.out.println(solve(N, scores, aids, F));

    sc.close();
  }

  static int solve(int N, final int[] scores, int[] aids, int F) {
    int C = scores.length;

    Integer[] sortedIndices = new Integer[C];
    for (int i = 0; i < sortedIndices.length; ++i) {
      sortedIndices[i] = i;
    }
    Arrays.sort(
        sortedIndices,
        new Comparator<Integer>() {
          public int compare(Integer i1, Integer i2) {
            return scores[i1] - scores[i2];
          }
        });

    int half = (N - 1) / 2;

    int[] leftAidSums = new int[C];
    PriorityQueue<Integer> leftAids = new PriorityQueue<Integer>(1, Collections.reverseOrder());
    int leftAidSum = 0;
    for (int i = 0; i < leftAidSums.length; ++i) {
      if (leftAids.size() == half) {
        leftAidSums[i] = leftAidSum;
      }

      if (leftAids.size() != half
          || leftAids.isEmpty()
          || aids[sortedIndices[i]] < leftAids.peek()) {
        leftAids.offer(aids[sortedIndices[i]]);
        leftAidSum += aids[sortedIndices[i]];
      }

      if (leftAids.size() == half + 1) {
        leftAidSum -= leftAids.poll();
      }
    }

    PriorityQueue<Integer> rightAids = new PriorityQueue<Integer>(1, Collections.reverseOrder());
    int rightAidSum = 0;
    for (int i = C - 1; i >= half; --i) {
      if (rightAids.size() == half && aids[sortedIndices[i]] + leftAidSums[i] + rightAidSum <= F) {
        return scores[sortedIndices[i]];
      }

      rightAids.offer(aids[sortedIndices[i]]);
      rightAidSum += aids[sortedIndices[i]];
      if (rightAids.size() == half + 1) {
        rightAidSum -= rightAids.poll();
      }
    }

    return -1;
  }
}
