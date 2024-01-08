// TLE on POJ. AC on BOJ: https://bzoj.net/p/1629

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
  static final int MIN_RISK = -500000000;
  static final int MAX_RISK = 500000000;

  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int N = Integer.parseInt(st.nextToken());
    int[] W = new int[N];
    int[] S = new int[N];
    for (int i = 0; i < N; ++i) {
      st = new StringTokenizer(br.readLine());
      W[i] = Integer.parseInt(st.nextToken());
      S[i] = Integer.parseInt(st.nextToken());
    }

    System.out.println(solve(W, S));
  }

  static int solve(final int[] W, final int[] S) {
    List<Integer> indices = new ArrayList<Integer>();
    for (int i = 0; i < W.length; ++i) {
      indices.add(i);
    }
    Collections.sort(
        indices,
        new Comparator<Integer>() {
          public int compare(Integer i1, Integer i2) {
            return -((W[i1] + S[i1]) - (W[i2] + S[i2]));
          }
        });

    int weightSum = 0;
    for (int weight : W) {
      weightSum += weight;
    }

    int result = -1;
    int lower = MIN_RISK;
    int upper = MAX_RISK;
    while (lower <= upper) {
      int middle = (lower + upper) / 2;
      if (check(W, S, indices, weightSum, middle)) {
        result = middle;
        upper = middle - 1;
      } else {
        lower = middle + 1;
      }
    }

    return result;
  }

  static boolean check(int[] W, int[] S, List<Integer> indices, int weightSum, int risk) {
    int index = 0;
    PriorityQueue<Integer> weights = new PriorityQueue<Integer>(1, Collections.reverseOrder());
    for (int i = 0; i < W.length; ++i) {
      while (index != indices.size()
          && W[indices.get(index)] + S[indices.get(index)] >= weightSum - risk) {
        weights.offer(W[indices.get(index)]);
        ++index;
      }
      if (weights.isEmpty()) {
        return false;
      }

      weightSum -= weights.poll();
    }

    return true;
  }
}