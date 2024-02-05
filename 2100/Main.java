import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    long n = sc.nextLong();

    System.out.println(solve(n));

    sc.close();
  }

  static String solve(long n) {
    List<Solution> solutions = new ArrayList<Solution>();
    long sum = 0;
    int minSide = 1;
    for (int maxSide = 1; (long) maxSide * maxSide <= n; ++maxSide) {
      sum += (long) maxSide * maxSide;
      while (sum > n) {
        sum -= (long) minSide * minSide;
        ++minSide;
      }

      if (sum == n) {
        solutions.add(new Solution(maxSide - minSide + 1, minSide));
      }
    }

    Collections.sort(
        solutions,
        new Comparator<Solution>() {
          @Override
          public int compare(Solution solution1, Solution solution2) {
            return -(solution1.length - solution2.length);
          }
        });

    StringBuilder result = new StringBuilder().append(solutions.size()).append("\n");
    for (Solution solution : solutions) {
      result.append(solution.length);
      for (int i = 0; i < solution.length; ++i) {
        result.append(" ").append(solution.minSide + i);
      }
      result.append("\n");
    }

    return result.toString();
  }
}

class Solution {
  int length;
  int minSide;

  Solution(int length, int minSide) {
    this.length = length;
    this.minSide = minSide;
  }
}