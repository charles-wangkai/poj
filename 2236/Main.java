import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int d = sc.nextInt();
    int[] x = new int[N];
    int[] y = new int[N];
    for (int i = 0; i < N; ++i) {
      x[i] = sc.nextInt();
      y[i] = sc.nextInt();
    }
    List<String> operations = new ArrayList<String>();
    sc.nextLine();
    while (sc.hasNextLine()) {
      operations.add(sc.nextLine());
    }

    System.out.println(solve(x, y, d, operations));

    sc.close();
  }

  static String solve(int[] x, int[] y, int d, List<String> operations) {
    int N = x.length;

    StringBuilder result = new StringBuilder();
    int[] parents = new int[N];
    Arrays.fill(parents, -1);
    boolean[] repaired = new boolean[N];
    for (String operation : operations) {
      String[] parts = operation.split(" ");
      if (parts[0].equals("O")) {
        int p = Integer.parseInt(parts[1]) - 1;
        for (int i = 0; i < N; ++i) {
          if (repaired[i] && isNear(x[i], y[i], x[p], y[p], d)) {
            int root1 = findRoot(parents, p);
            int root2 = findRoot(parents, i);
            if (root1 != root2) {
              parents[root2] = root1;
            }
          }
        }

        repaired[p] = true;
      } else {
        int p = Integer.parseInt(parts[1]) - 1;
        int q = Integer.parseInt(parts[2]) - 1;

        if (result.length() != 0) {
          result.append("\n");
        }
        result.append(
            (repaired[p] && repaired[q] && findRoot(parents, p) == findRoot(parents, q))
                ? "SUCCESS"
                : "FAIL");
      }
    }

    return result.toString();
  }

  static boolean isNear(int x1, int y1, int x2, int y2, int d) {
    return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) <= d * d;
  }

  static int findRoot(int[] parents, int node) {
    if (parents[node] == -1) {
      return node;
    }

    parents[node] = findRoot(parents, parents[node]);

    return parents[node];
  }
}
