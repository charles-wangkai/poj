// https://www.hankcs.com/program/algorithm/poj-3293-rectilinear-polygon.html

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int t = Integer.parseInt(st.nextToken());
    for (int tc = 0; tc < t; ++tc) {
      st = new StringTokenizer(br.readLine());
      int n = Integer.parseInt(st.nextToken());
      int[] xs = new int[n];
      int[] ys = new int[n];
      for (int i = 0; i < n; ++i) {
        st = new StringTokenizer(br.readLine());
        xs[i] = Integer.parseInt(st.nextToken());
        ys[i] = Integer.parseInt(st.nextToken());
      }

      System.out.println(solve(xs, ys));
    }
  }

  static int solve(int[] xs, int[] ys) {
    int[] verticalAdjIndices = buildAdjIndices(xs, ys);
    if (verticalAdjIndices == null) {
      return -1;
    }

    int[] horizontalAdjIndices = buildAdjIndices(ys, xs);
    if (horizontalAdjIndices == null) {
      return -1;
    }

    if (hasIntersection(xs, ys, verticalAdjIndices, horizontalAdjIndices)) {
      return -1;
    }

    int index = 0;
    int count = 0;
    int lengthSum = 0;
    while (true) {
      if (count % 2 == 0) {
        lengthSum += Math.abs(ys[index] - ys[verticalAdjIndices[index]]);
        index = verticalAdjIndices[index];
      } else {
        lengthSum += Math.abs(xs[index] - xs[horizontalAdjIndices[index]]);
        index = horizontalAdjIndices[index];
      }

      ++count;

      if (index == 0) {
        break;
      }
    }

    return (count == xs.length) ? lengthSum : -1;
  }

  static boolean hasIntersection(
      int[] xs, int[] ys, int[] verticalAdjIndices, int[] horizontalAdjIndices) {
    int n = verticalAdjIndices.length;

    for (int i = 0; i < n; ++i) {
      if (i < verticalAdjIndices[i]) {
        for (int j = 0; j < n; ++j) {
          if (Integer.signum(xs[j] - xs[i]) == -Integer.signum(xs[horizontalAdjIndices[j]] - xs[i])
              && Integer.signum(ys[i] - ys[j])
                  == -Integer.signum(ys[verticalAdjIndices[i]] - ys[j])) {
            return true;
          }
        }
      }
    }

    return false;
  }

  static int[] buildAdjIndices(int[] keys, final int[] values) {
    int n = keys.length;

    Map<Integer, List<Integer>> keyToIndices = new HashMap<Integer, List<Integer>>();
    for (int i = 0; i < n; ++i) {
      if (!keyToIndices.containsKey(keys[i])) {
        keyToIndices.put(keys[i], new ArrayList<Integer>());
      }
      keyToIndices.get(keys[i]).add(i);
    }

    int[] result = new int[n];
    for (List<Integer> indices : keyToIndices.values()) {
      if (indices.size() % 2 == 1) {
        return null;
      }

      Collections.sort(
          indices,
          new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
              return values[i1] - values[i2];
            }
          });

      for (int i = 0; i < indices.size(); i += 2) {
        result[indices.get(i)] = indices.get(i + 1);
        result[indices.get(i + 1)] = indices.get(i);
      }
    }

    return result;
  }
}