// https://en.wikipedia.org/wiki/Sprague%E2%80%93Grundy_theorem

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int n = sc.nextInt();

    System.out.println(solve(n));

    sc.close();
  }

  static int solve(int n) {
    return (computeNimber(new HashMap<Integer, Integer>(), n) == 0) ? 2 : 1;
  }

  static int computeNimber(Map<Integer, Integer> stateToNimber, int state) {
    if (!stateToNimber.containsKey(state)) {
      Set<Integer> nimbers = new HashSet<Integer>();
      for (int i = 0; i < state; ++i) {
        nimbers.add(
            computeNimber(stateToNimber, Math.max(0, i - 2))
                ^ computeNimber(stateToNimber, Math.max(0, state - i - 3)));
      }

      stateToNimber.put(state, mex(nimbers));
    }

    return stateToNimber.get(state);
  }

  static int mex(Set<Integer> nimbers) {
    for (int i = 0; ; ++i) {
      if (!nimbers.contains(i)) {
        return i;
      }
    }
  }
}