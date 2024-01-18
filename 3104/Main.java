import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int n = Integer.parseInt(st.nextToken());
    int[] a = new int[n];
    st = new StringTokenizer(br.readLine());
    for (int i = 0; i < a.length; ++i) {
      a[i] = Integer.parseInt(st.nextToken());
    }
    st = new StringTokenizer(br.readLine());
    int k = Integer.parseInt(st.nextToken());

    System.out.println(solve(a, k));
  }

  static int solve(int[] a, int k) {
    int max = -1;
    for (int ai : a) {
      max = Math.max(max, ai);
    }

    if (k == 1) {
      return max;
    }

    int result = -1;
    int lower = 1;
    int upper = max;
    while (lower <= upper) {
      int middle = (lower + upper) / 2;
      if (check(a, k, middle)) {
        result = middle;
        upper = middle - 1;
      } else {
        lower = middle + 1;
      }
    }

    return result;
  }

  static boolean check(int[] a, int k, int time) {
    long needed = 0;
    for (int ai : a) {
      needed += (Math.max(0, ai - time) + (k - 2)) / (k - 1);
    }

    return needed <= time;
  }
}