// https://blog.csdn.net/salmonwilliam/article/details/106736498

import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (sc.hasNext()) {
      char[][] grid = new char[16][16];
      for (int r = 0; r < 16; ++r) {
        String line = sc.next();
        for (int c = 0; c < 16; ++c) {
          grid[r][c] = line.charAt(c);
        }
      }

      System.out.println(solve(grid));
    }

    sc.close();
  }

  static String solve(char[][] grid) {
    int filled = 0;

    int[][] values = new int[16][16];
    for (int r = 0; r < values.length; ++r) {
      Arrays.fill(values[r], -1);
    }

    int[][] masks = new int[16][16];

    for (int r = 0; r < 16; ++r) {
      for (int c = 0; c < 16; ++c) {
        if (grid[r][c] != '-') {
          fill(values, masks, r, c, grid[r][c] - 'A');
          ++filled;
        }
      }
    }

    values = search(filled, values, masks);

    StringBuilder result = new StringBuilder();
    for (int r = 0; r < 16; ++r) {
      for (int c = 0; c < 16; ++c) {
        result.append((char) (values[r][c] + 'A'));
      }
      result.append("\n");
    }

    return result.toString();
  }

  static int[][] search(int filled, int[][] values, int[][] masks) {
    for (int r = 0; r < 16; ++r) {
      for (int c = 0; c < 16; ++c) {
        if (values[r][c] == -1) {
          int onlyCandidateValue = findOnlyCandidateValue(masks[r][c]);
          if (onlyCandidateValue != -1) {
            fill(values, masks, r, c, onlyCandidateValue);
            ++filled;
          }
        }
      }
    }

    for (int r = 0; r < 16; ++r) {
      for (int value = 0; value < 16; ++value) {
        int onlyCandidateCol = findOnlyCandidateCol(values, masks, r, value);
        if (onlyCandidateCol == -2) {
          return null;
        }
        if (onlyCandidateCol != -1) {
          fill(values, masks, r, onlyCandidateCol, value);
          ++filled;
        }
      }
    }

    for (int c = 0; c < 16; ++c) {
      for (int value = 0; value < 16; ++value) {
        int onlyCandidateRow = findOnlyCandidateRow(values, masks, c, value);
        if (onlyCandidateRow == -2) {
          return null;
        }
        if (onlyCandidateRow != -1) {
          fill(values, masks, onlyCandidateRow, c, value);
          ++filled;
        }
      }
    }

    for (int beginR = 0; beginR < 16; beginR += 4) {
      for (int beginC = 0; beginC < 16; beginC += 4) {
        for (int value = 0; value < 16; ++value) {
          Point point = findOnlyCandidatePointInBlock(values, masks, beginR, beginC, value);
          if (point.r == -2) {
            return null;
          }
          if (point.r != -1) {
            fill(values, masks, point.r, point.c, value);
            ++filled;
          }
        }
      }
    }

    if (filled == 256) {
      return values;
    }

    int minCandidateNum = Integer.MAX_VALUE;
    int nextR = -1;
    int nextC = -1;
    for (int r = 0; r < 16; ++r) {
      for (int c = 0; c < 16; ++c) {
        if (values[r][c] == -1) {
          int candidateNum = 16 - Integer.bitCount(masks[r][c]);
          if (candidateNum < minCandidateNum) {
            minCandidateNum = candidateNum;
            nextR = r;
            nextC = c;
          }
        }
      }
    }

    for (int value = 0; value < 16; ++value) {
      if (((masks[nextR][nextC] >> value) & 1) == 0) {
        int[][] nextValues = clone(values);
        int[][] nextMasks = clone(masks);
        fill(nextValues, nextMasks, nextR, nextC, value);

        int[][] solution = search(filled + 1, nextValues, nextMasks);
        if (solution != null) {
          return solution;
        }
      }
    }

    return null;
  }

  static int[][] clone(int[][] a) {
    int[][] result = new int[a.length][];
    for (int i = 0; i < result.length; ++i) {
      result[i] = a[i].clone();
    }

    return result;
  }

  static Point findOnlyCandidatePointInBlock(
      int[][] values, int[][] masks, int beginR, int beginC, int value) {
    int r = -2;
    int c = -2;
    for (int i = 0; i < 4; ++i) {
      for (int j = 0; j < 4; ++j) {
        if (values[beginR + i][beginC + j] == value) {
          return new Point(-1, -1);
        }
        if (values[beginR + i][beginC + j] == -1
            && ((masks[beginR + i][beginC + j] >> value) & 1) == 0) {
          if (r != -2) {
            return new Point(-1, -1);
          }

          r = beginR + i;
          c = beginC + j;
        }
      }
    }

    return new Point(r, c);
  }

  static int findOnlyCandidateRow(int[][] values, int[][] masks, int c, int value) {
    int result = -2;
    for (int r = 0; r < 16; ++r) {
      if (values[r][c] == value) {
        return -1;
      }
      if (values[r][c] == -1 && ((masks[r][c] >> value) & 1) == 0) {
        if (result != -2) {
          return -1;
        }

        result = r;
      }
    }

    return result;
  }

  static int findOnlyCandidateCol(int[][] values, int[][] masks, int r, int value) {
    int result = -2;
    for (int c = 0; c < 16; ++c) {
      if (values[r][c] == value) {
        return -1;
      }
      if (values[r][c] == -1 && ((masks[r][c] >> value) & 1) == 0) {
        if (result != -2) {
          return -1;
        }

        result = c;
      }
    }

    return result;
  }

  static int findOnlyCandidateValue(int mask) {
    return (Integer.bitCount(mask) == 1) ? Integer.numberOfTrailingZeros(mask) : -1;
  }

  static void fill(int[][] values, int[][] masks, int r, int c, int value) {
    values[r][c] = value;

    for (int i = 0; i < 16; ++i) {
      masks[i][c] |= 1 << value;
    }

    for (int j = 0; j < 16; ++j) {
      masks[r][j] |= 1 << value;
    }

    for (int i = 0; i < 4; ++i) {
      for (int j = 0; j < 4; ++j) {
        masks[r / 4 * 4 + i][c / 4 * 4 + j] |= 1 << value;
      }
    }
  }
}

class Point {
  int r;
  int c;

  Point(int r, int c) {
    this.r = r;
    this.c = c;
  }
}