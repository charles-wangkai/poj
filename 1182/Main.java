import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int N = Integer.parseInt(st.nextToken());
    int K = Integer.parseInt(st.nextToken());
    int[] D = new int[K];
    int[] X = new int[K];
    int[] Y = new int[K];
    for (int i = 0; i < K; ++i) {
      st = new StringTokenizer(br.readLine());
      D[i] = Integer.parseInt(st.nextToken());
      X[i] = Integer.parseInt(st.nextToken()) - 1;
      Y[i] = Integer.parseInt(st.nextToken()) - 1;
    }

    System.out.println(solve(N, D, X, Y));
  }

  static int solve(int N, int[] D, int[] X, int[] Y) {
    int[] parents = new int[N];
    Arrays.fill(parents, -1);

    int[] relations = new int[N];

    int result = 0;
    for (int i = 0; i < D.length; ++i) {
      if (X[i] >= N || Y[i] >= N) {
        ++result;
      } else {
        int xRoot = findRoot(parents, relations, X[i]);
        int yRoot = findRoot(parents, relations, Y[i]);

        if (D[i] == 1) {
          if (xRoot == yRoot) {
            if (relations[X[i]] != relations[Y[i]]) {
              ++result;
            }
          } else {
            parents[yRoot] = xRoot;
            relations[yRoot] = (relations[X[i]] - relations[Y[i]] + 3) % 3;
          }
        } else {
          if (xRoot == yRoot) {
            if ((relations[X[i]] + 1) % 3 != relations[Y[i]]) {
              ++result;
            }
          } else {
            parents[yRoot] = xRoot;
            relations[yRoot] = (relations[X[i]] - relations[Y[i]] + 4) % 3;
          }
        }
      }
    }

    return result;
  }

  static int findRoot(int[] parents, int[] relations, int node) {
    if (parents[node] == -1) {
      return node;
    }

    int root = findRoot(parents, relations, parents[node]);
    relations[node] = (relations[node] + relations[parents[node]]) % 3;
    parents[node] = root;

    return root;
  }
}
