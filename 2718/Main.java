import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int t = sc.nextInt();
    sc.nextLine();
    for (int tc = 0; tc < t; ++tc) {
      String[] parts = sc.nextLine().split(" ");
      int[] d = new int[parts.length];
      for (int i = 0; i < d.length; ++i) {
        d[i] = Integer.parseInt(parts[i]);
      }

      System.out.println(solve(d));
    }

    sc.close();
  }

  static int solve(int[] d) {
    int result = Integer.MAX_VALUE;
    for (int mask = 0; mask < 1 << d.length; ++mask) {
      List<Integer> digits1 = new ArrayList<Integer>();
      List<Integer> digits2 = new ArrayList<Integer>();
      for (int i = 0; i < d.length; ++i) {
        if (((mask >> i) & 1) == 1) {
          digits1.add(d[i]);
        } else {
          digits2.add(d[i]);
        }
      }

      if (!digits1.isEmpty()
          && !digits2.isEmpty()
          && Math.abs(digits1.size() - digits2.size()) <= 2) {
        List<Integer> values1 = buildValues(digits1);
        List<Integer> values2 = buildValues(digits2);

        for (int value2 : values2) {
          int index = Collections.binarySearch(values1, value2);
          if (index < 0) {
            index = -2 - index;
          }

          if (index >= 0) {
            result = Math.min(result, Math.abs(value2 - values1.get(index)));
          }
          if (index + 1 < values1.size()) {
            result = Math.min(result, Math.abs(value2 - values1.get(index + 1)));
          }
        }
      }
    }

    return result;
  }

  static List<Integer> buildValues(List<Integer> digits) {
    List<Integer> values = new ArrayList<Integer>();
    search(values, digits, 0);
    Collections.sort(values);

    return values;
  }

  static void search(List<Integer> values, List<Integer> digits, int index) {
    if (index == digits.size()) {
      int value = 0;
      for (int digit : digits) {
        value = value * 10 + digit;
      }

      values.add(value);

      return;
    }

    for (int i = index; i < digits.size(); ++i) {
      if (!(digits.size() != 1 && index == 0 && digits.get(i) == 0)) {
        swap(digits, i, index);
        search(values, digits, index + 1);
        swap(digits, i, index);
      }
    }
  }

  static void swap(List<Integer> a, int index1, int index2) {
    int temp = a.get(index1);
    a.set(index1, a.get(index2));
    a.set(index2, temp);
  }
}
