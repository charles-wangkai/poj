import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int n = sc.nextInt();
    for (int tc = 0; tc < n; ++tc) {
      int p = sc.nextInt();
      int[] mapping = new int[p];
      for (int i = 0; i < mapping.length; ++i) {
        mapping[i] = sc.nextInt();
      }

      System.out.println(solve(mapping));
    }

    sc.close();
  }

  static int solve(int[] mapping) {
    List<Integer> lasts = new ArrayList<Integer>();
    for (int x : mapping) {
      int index = -1 - Collections.binarySearch(lasts, x);
      if (index == lasts.size()) {
        lasts.add(x);
      } else {
        lasts.set(index, x);
      }
    }

    return lasts.size();
  }
}
