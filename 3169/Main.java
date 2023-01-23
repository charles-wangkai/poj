import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int ML = sc.nextInt();
    int MD = sc.nextInt();
    int[] AL = new int[ML];
    int[] BL = new int[ML];
    int[] DL = new int[ML];
    for (int i = 0; i < ML; ++i) {
      AL[i] = sc.nextInt() - 1;
      BL[i] = sc.nextInt() - 1;
      DL[i] = sc.nextInt();
    }
    int[] AD = new int[MD];
    int[] BD = new int[MD];
    int[] DD = new int[MD];
    for (int i = 0; i < MD; ++i) {
      AD[i] = sc.nextInt() - 1;
      BD[i] = sc.nextInt() - 1;
      DD[i] = sc.nextInt();
    }

    System.out.println(solve(N, AL, BL, DL, AD, BD, DD));

    sc.close();
  }

  static int solve(int N, int[] AL, int[] BL, int[] DL, int[] AD, int[] BD, int[] DD) {
    if (!bellmanFord(AL, BL, DL, AD, BD, DD, new int[N])) {
      return -1;
    }

    int[] distances = new int[N];
    Arrays.fill(distances, Integer.MAX_VALUE);
    distances[0] = 0;
    bellmanFord(AL, BL, DL, AD, BD, DD, distances);

    return (distances[distances.length - 1] == Integer.MAX_VALUE)
        ? -2
        : distances[distances.length - 1];
  }

  static boolean bellmanFord(
      int[] AL, int[] BL, int[] DL, int[] AD, int[] BD, int[] DD, int[] distances) {
    int N = distances.length;

    int count = 0;
    while (count < N) {
      boolean updated = false;
      for (int i = 0; i + 1 < N; ++i) {
        if (distances[i + 1] != Integer.MAX_VALUE && distances[i + 1] < distances[i]) {
          distances[i] = distances[i + 1];
          updated = true;
        }
      }
      for (int i = 0; i < AL.length; ++i) {
        if (distances[AL[i]] != Integer.MAX_VALUE && distances[AL[i]] + DL[i] < distances[BL[i]]) {
          distances[BL[i]] = distances[AL[i]] + DL[i];
          updated = true;
        }
      }
      for (int i = 0; i < AD.length; ++i) {
        if (distances[BD[i]] != Integer.MAX_VALUE && distances[BD[i]] - DD[i] < distances[AD[i]]) {
          distances[AD[i]] = distances[BD[i]] - DD[i];
          updated = true;
        }
      }

      if (!updated) {
        break;
      }

      ++count;
    }

    return count != N;
  }
}
