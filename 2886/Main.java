// http://www.hankcs.com/program/algorithm/poj-2886-who-gets-the-most-candies.html

import java.util.Arrays;
import java.util.Scanner;

public class Main {
  static final int LIMIT = 500000;

  static int[] candyNums;

  public static void main(String[] args) {
    precompute();

    Scanner sc = new Scanner(System.in);

    while (sc.hasNextInt()) {
      int N = sc.nextInt();
      int K = sc.nextInt();
      String[] names = new String[N];
      int[] cards = new int[N];
      for (int i = 0; i < N; ++i) {
        names[i] = sc.next();
        cards[i] = sc.nextInt();
      }

      System.out.println(solve(names, cards, K));
    }

    sc.close();
  }

  static void precompute() {
    candyNums = new int[LIMIT + 1];
    Arrays.fill(candyNums, 1);
    for (int i = 2; i < candyNums.length; ++i) {
      if (candyNums[i] == 1) {
        for (int j = i; j < candyNums.length; j += i) {
          int exponent = 0;
          int rest = j;
          while (rest % i == 0) {
            ++exponent;
            rest /= i;
          }

          candyNums[j] *= exponent + 1;
        }
      }
    }
  }

  static String solve(String[] names, int[] cards, int K) {
    int N = names.length;

    int[] binaryIndexedTree = new int[Integer.highestOneBit(N) * 2 + 1];
    for (int i = 0; i < N; ++i) {
      add(binaryIndexedTree, i + 1, 1);
    }

    int maxCandyNum = -1;
    String nameWithMaxCandyNum = null;
    int index = K - 1;
    for (int i = 0; i < N; ++i) {
      if (candyNums[i + 1] > maxCandyNum) {
        maxCandyNum = candyNums[i + 1];
        nameWithMaxCandyNum = names[index];
      }

      add(binaryIndexedTree, index + 1, -1);

      if (i != N - 1) {
        int restNum = N - i - 1;
        int sequence =
            ((computeSum(binaryIndexedTree, index + 1)
                                + cards[index]
                                + ((cards[index] > 0) ? 0 : 1)
                                - 1)
                            % restNum
                        + restNum)
                    % restNum
                + 1;
        index = find(binaryIndexedTree, sequence) - 1;
      }
    }

    return String.format("%s %d", nameWithMaxCandyNum, maxCandyNum);
  }

  static void add(int[] binaryIndexedTree, int i, int x) {
    while (i < binaryIndexedTree.length) {
      binaryIndexedTree[i] += x;
      i += i & -i;
    }
  }

  static int computeSum(int[] binaryIndexedTree, int i) {
    int result = 0;
    while (i != 0) {
      result += binaryIndexedTree[i];
      i -= i & -i;
    }

    return result;
  }

  static int find(int[] binaryIndexedTree, int sequence) {
    int result = -1;
    int lower = 1;
    int upper = binaryIndexedTree.length - 1;
    while (lower <= upper) {
      int middle = (lower + upper) / 2;
      if (computeSum(binaryIndexedTree, middle) >= sequence) {
        result = middle;
        upper = middle - 1;
      } else {
        lower = middle + 1;
      }
    }

    return result;
  }
}