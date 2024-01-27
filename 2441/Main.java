import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int M = sc.nextInt();
    int[][] likes = new int[N][];
    for (int i = 0; i < likes.length; ++i) {
      int P = sc.nextInt();
      likes[i] = new int[P];
      for (int j = 0; j < likes[i].length; ++j) {
        likes[i][j] = sc.nextInt();
      }
    }

    System.out.println(solve(M, likes));

    sc.close();
  }

  static int solve(int M, int[][] likes) {
    Map<Integer, Integer> maskToWayNum = Collections.singletonMap(0, 1);
    for (int[] l : likes) {
      Map<Integer, Integer> nextMaskToWayNum = new HashMap<Integer, Integer>();
      for (int mask : maskToWayNum.keySet()) {
        for (int like : l) {
          if (((mask >> (like - 1)) & 1) == 0) {
            int nextMask = mask + (1 << (like - 1));

            nextMaskToWayNum.put(
                nextMask,
                (nextMaskToWayNum.containsKey(nextMask) ? nextMaskToWayNum.get(nextMask) : 0)
                    + maskToWayNum.get(mask));
          }
        }
      }

      maskToWayNum = nextMaskToWayNum;
    }

    int result = 0;
    for (int wayNum : maskToWayNum.values()) {
      result += wayNum;
    }

    return result;
  }
}