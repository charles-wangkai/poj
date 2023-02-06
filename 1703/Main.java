import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  static final int LIMIT = 100000;

  static int[] parents = new int[LIMIT];
  static boolean[] inverses = new boolean[LIMIT];

  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int T = Integer.parseInt(st.nextToken());
    for (int tc = 0; tc < T; ++tc) {
      st = new StringTokenizer(br.readLine());
      st.nextToken();
      int M = Integer.parseInt(st.nextToken());
      char[] types = new char[M];
      int[] a = new int[M];
      int[] b = new int[M];
      for (int i = 0; i < M; ++i) {
        st = new StringTokenizer(br.readLine());
        types[i] = st.nextToken().charAt(0);
        a[i] = Integer.parseInt(st.nextToken()) - 1;
        b[i] = Integer.parseInt(st.nextToken()) - 1;
      }

      Arrays.fill(parents, -1);
      Arrays.fill(inverses, false);

      for (int i = 0; i < a.length; ++i) {
        int aRoot = findRoot(parents, inverses, a[i]);
        int bRoot = findRoot(parents, inverses, b[i]);

        if (types[i] == 'D') {
          if (aRoot != bRoot) {
            parents[bRoot] = aRoot;
            inverses[bRoot] = inverses[a[i]] == inverses[b[i]];
          }
        } else {
          String judgment;
          if (aRoot == bRoot) {
            judgment =
                (inverses[a[i]] == inverses[b[i]]) ? "In the same gang." : "In different gangs.";
          } else {
            judgment = "Not sure yet.";
          }

          System.out.println(judgment);
        }
      }
    }
  }

  static int findRoot(int[] parents, boolean[] inverses, int node) {
    if (parents[node] == -1) {
      return node;
    }

    int root = findRoot(parents, inverses, parents[node]);
    inverses[node] ^= inverses[parents[node]];
    parents[node] = root;

    return root;
  }
}
