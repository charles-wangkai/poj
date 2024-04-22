// TLE on POJ. AC on Bailian: http://bailian.openjudge.cn/practice/2984

// https://www.cnblogs.com/wmq12138/p/10368693.html

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {
  static final int[][] BLOCK_INDICES = {
    {0, 0, 0, 1, 1, 1, 2, 2, 2},
    {0, 0, 0, 1, 1, 1, 2, 2, 2},
    {0, 0, 0, 1, 1, 1, 2, 2, 2},
    {3, 3, 3, 4, 4, 4, 5, 5, 5},
    {3, 3, 3, 4, 4, 4, 5, 5, 5},
    {3, 3, 3, 4, 4, 4, 5, 5, 5},
    {6, 6, 6, 7, 7, 7, 8, 8, 8},
    {6, 6, 6, 7, 7, 7, 8, 8, 8},
    {6, 6, 6, 7, 7, 7, 8, 8, 8}
  };

  static Map<Integer, Integer> valueToBitNum = new HashMap<Integer, Integer>();
  static Map<Integer, Integer> twoPowerToPos = new HashMap<Integer, Integer>();

  public static void main(String[] args) throws Throwable {
    precompute();

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      String s = br.readLine();
      if (s.equals("end")) {
        break;
      }

      System.out.println(solve(s));
    }
  }

  static void precompute() {
    for (int mask = 0; mask < 1 << 9; ++mask) {
      valueToBitNum.put(mask, Integer.bitCount(mask));
    }

    for (int pos = 0; pos < 9; ++pos) {
      twoPowerToPos.put(1 << pos, pos);
    }
  }

  static String solve(String s) {
    int[][] table = new int[9][9];

    int[] rowMasks = new int[9];
    Arrays.fill(rowMasks, (1 << 9) - 1);

    int[] colMasks = new int[9];
    Arrays.fill(colMasks, (1 << 9) - 1);

    int[] blockMasks = new int[9];
    Arrays.fill(blockMasks, (1 << 9) - 1);

    Set<Integer> rests = new HashSet<Integer>();
    for (int r = 0; r < 9; ++r) {
      for (int c = 0; c < 9; ++c) {
        char ch = s.charAt(r * 9 + c);
        if (ch == '.') {
          rests.add(r * 9 + c);
        } else {
          table[r][c] = ch - '0';
          flip(rowMasks, colMasks, blockMasks, r, c, table[r][c] - 1);
        }
      }
    }

    search(table, rowMasks, colMasks, blockMasks, rests);

    StringBuilder result = new StringBuilder();
    for (int r = 0; r < 9; ++r) {
      for (int c = 0; c < 9; ++c) {
        result.append(table[r][c]);
      }
    }

    return result.toString();
  }

  static void flip(int[] rowMasks, int[] colMasks, int[] blockMasks, int r, int c, int pos) {
    rowMasks[r] ^= 1 << pos;
    colMasks[c] ^= 1 << pos;
    blockMasks[BLOCK_INDICES[r][c]] ^= 1 << pos;
  }

  static boolean search(
      int[][] table, int[] rowMasks, int[] colMasks, int[] blockMasks, Set<Integer> rests) {
    if (rests.isEmpty()) {
      return true;
    }

    int mask = -1;
    int r = -1;
    int c = -1;
    for (int rest : rests) {
      int currR = rest / 9;
      int currC = rest % 9;
      int currentMask = rowMasks[currR] & colMasks[currC] & blockMasks[BLOCK_INDICES[currR][currC]];
      if (mask == -1 || valueToBitNum.get(currentMask) < valueToBitNum.get(mask)) {
        mask = currentMask;
        r = currR;
        c = currC;
      }
    }

    rests.remove(r * 9 + c);

    while (mask != 0) {
      int pos = twoPowerToPos.get(mask & -mask);

      table[r][c] = pos + 1;
      flip(rowMasks, colMasks, blockMasks, r, c, pos);

      if (search(table, rowMasks, colMasks, blockMasks, rests)) {
        return true;
      }

      table[r][c] = 0;
      flip(rowMasks, colMasks, blockMasks, r, c, pos);

      mask -= 1 << pos;
    }

    rests.add(r * 9 + c);

    return false;
  }
}