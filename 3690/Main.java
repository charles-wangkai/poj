import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class Main {
  static final int BASE_1 = 31;
  static final int BASE_2 = 37;

  static boolean[][] sky = new boolean[1000][1000];
  static boolean[][] constellation = new boolean[50][50];
  static int[][] rowHashes = new int[1000][1000];
  static Set<Integer> hashes = new HashSet<Integer>();

  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    int tc = 1;
    while (true) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      int N = Integer.parseInt(st.nextToken());
      int M = Integer.parseInt(st.nextToken());
      int T = Integer.parseInt(st.nextToken());
      int P = Integer.parseInt(st.nextToken());
      int Q = Integer.parseInt(st.nextToken());
      if (N == 0 && M == 0 && T == 0 && P == 0 && Q == 0) {
        break;
      }

      for (int r = 0; r < N; ++r) {
        String line = br.readLine();
        for (int c = 0; c < M; ++c) {
          sky[r][c] = line.charAt(c) == '*';
        }
      }

      Map<Integer, Integer> constellationHashToCount = new HashMap<Integer, Integer>();
      for (int i = 0; i < T; ++i) {
        br.readLine();

        for (int r = 0; r < P; ++r) {
          String line = br.readLine();
          for (int c = 0; c < Q; ++c) {
            constellation[r][c] = line.charAt(c) == '*';
          }
        }

        int constellationHash = fillHashes(null, null, constellation, P, Q, P, Q);
        constellationHashToCount.put(
            constellationHash,
            (constellationHashToCount.containsKey(constellationHash)
                    ? constellationHashToCount.get(constellationHash)
                    : 0)
                + 1);
      }

      hashes.clear();
      fillHashes(hashes, constellationHashToCount, sky, N, M, P, Q);

      int matchingNum = 0;
      for (int hash : hashes) {
        matchingNum += constellationHashToCount.get(hash);
      }

      System.out.println(String.format("Case %d: %d", tc, matchingNum));

      ++tc;
    }
  }

  static int fillHashes(
      Set<Integer> hashes,
      Map<Integer, Integer> constellationHashToCount,
      boolean[][] matrix,
      int height,
      int width,
      int row,
      int col) {
    int placeValue1 = 1;
    for (int i = 0; i < col - 1; ++i) {
      placeValue1 *= BASE_1;
    }

    int placeValue2 = 1;
    for (int i = 0; i < row - 1; ++i) {
      placeValue2 *= BASE_2;
    }

    for (int r = 0; r < height; ++r) {
      int rowHash = 0;
      for (int c = 0; c < width; ++c) {
        rowHash = rowHash * BASE_1 + (matrix[r][c] ? 1 : 0);

        if (c >= col - 1) {
          rowHashes[r][c - col + 1] = rowHash;

          rowHash -= (matrix[r][c - col + 1] ? 1 : 0) * placeValue1;
        }
      }
    }

    int result = -1;
    for (int c = 0; c < width - col + 1; ++c) {
      int hash = 0;
      for (int r = 0; r < height; ++r) {
        hash = hash * BASE_2 + rowHashes[r][c];

        if (r >= row - 1) {
          if (hashes != null && constellationHashToCount.containsKey(hash)) {
            hashes.add(hash);
          }
          result = hash;

          hash -= rowHashes[r - row + 1][c] * placeValue2;
        }
      }
    }

    return result;
  }
}