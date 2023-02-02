import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int M = sc.nextInt();
    int[][] movies = new int[M][];
    for (int i = 0; i < movies.length; ++i) {
      int Mi = sc.nextInt();
      movies[i] = new int[Mi];
      for (int j = 0; j < movies[i].length; ++j) {
        movies[i][j] = sc.nextInt() - 1;
      }
    }

    System.out.println(solve(N, movies));

    sc.close();
  }

  static int solve(int N, int[][] movies) {
    boolean[][] edges = new boolean[N][N];
    for (int[] movie : movies) {
      for (int i = 0; i < movie.length; ++i) {
        for (int j = i + 1; j < movie.length; ++j) {
          edges[movie[i]][movie[j]] = true;
          edges[movie[j]][movie[i]] = true;
        }
      }
    }

    int minDistanceSum = Integer.MAX_VALUE;
    for (int begin = 0; begin < N; ++begin) {
      int[] distances = new int[N];
      Arrays.fill(distances, -1);
      distances[begin] = 0;
      Queue<Integer> pq = new LinkedList<Integer>();
      pq.offer(begin);
      while (!pq.isEmpty()) {
        int head = pq.poll();
        for (int i = 0; i < N; ++i) {
          if (edges[head][i] && distances[i] == -1) {
            distances[i] = distances[head] + 1;
            pq.offer(i);
          }
        }
      }

      int distanceSum = 0;
      for (int distance : distances) {
        distanceSum += distance;
      }

      minDistanceSum = Math.min(minDistanceSum, distanceSum);
    }

    return minDistanceSum * 100 / (N - 1);
  }
}
