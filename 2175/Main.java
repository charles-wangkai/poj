import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int M = sc.nextInt();
    int[] X = new int[N];
    int[] Y = new int[N];
    int[] B = new int[N];
    for (int i = 0; i < N; ++i) {
      X[i] = sc.nextInt();
      Y[i] = sc.nextInt();
      B[i] = sc.nextInt();
    }
    int[] P = new int[M];
    int[] Q = new int[M];
    int[] C = new int[M];
    for (int i = 0; i < M; ++i) {
      P[i] = sc.nextInt();
      Q[i] = sc.nextInt();
      C[i] = sc.nextInt();
    }
    int[][] E = new int[N][M];
    for (int i = 0; i < N; ++i) {
      for (int j = 0; j < M; ++j) {
        E[i][j] = sc.nextInt();
      }
    }

    System.out.println(solve(X, Y, B, P, Q, C, E));

    sc.close();
  }

  static String solve(int[] X, int[] Y, int[] B, int[] P, int[] Q, int[] C, int[][] E) {
    int N = X.length;
    int M = P.length;

    // Indices:
    // [0,N-1] - buildings
    // [N,N+M-1] - shelters
    // N+M - sink

    int vertexNum = N + M + 1;
    int[][] distances = new int[vertexNum][vertexNum];
    for (int i = 0; i < distances.length; ++i) {
      Arrays.fill(distances[i], Integer.MAX_VALUE);
    }
    for (int j = 0; j < M; ++j) {
      int sum = 0;
      for (int i = 0; i < N; ++i) {
        int cost = Math.abs(X[i] - P[j]) + Math.abs(Y[i] - Q[j]) + 1;
        distances[i][N + j] = cost;
        if (E[i][j] != 0) {
          distances[N + j][i] = -cost;
        }

        sum += E[i][j];
      }

      if (sum != 0) {
        distances[N + M][N + j] = 0;
      }
      if (sum != C[j]) {
        distances[N + j][N + M] = 0;
      }
    }

    int[][] prevs = new int[vertexNum][vertexNum];
    for (int i = 0; i < prevs.length; ++i) {
      Arrays.fill(prevs[i], i);
    }
    for (int k = 0; k < vertexNum; ++k) {
      for (int i = 0; i < vertexNum; ++i) {
        for (int j = 0; j < vertexNum; ++j) {
          if (distances[i][k] != Integer.MAX_VALUE
              && distances[k][j] != Integer.MAX_VALUE
              && distances[i][k] + distances[k][j] < distances[i][j]) {
            distances[i][j] = distances[i][k] + distances[k][j];
            prevs[i][j] = prevs[k][j];

            if (j == i && distances[i][j] < 0) {
              boolean[] visited = new boolean[vertexNum];
              for (int v = i; !visited[v]; v = prevs[i][v]) {
                visited[v] = true;

                if (v != N + M && prevs[i][v] != N + M) {
                  if (v >= N) {
                    ++E[prevs[i][v]][v - N];
                  } else {
                    --E[v][prevs[i][v] - N];
                  }
                }
              }

              StringBuilder result = new StringBuilder("SUBOPTIMAL");
              for (int x = 0; x < N; ++x) {
                result.append("\n");
                for (int y = 0; y < M; ++y) {
                  if (y != 0) {
                    result.append(" ");
                  }
                  result.append(E[x][y]);
                }
              }

              return result.toString();
            }
          }
        }
      }
    }

    return "OPTIMAL";
  }
}