// https://www.hankcs.com/program/algorithm/poj-2032-square-carpets.html
// https://github.com/charles-wangkai/aoj/blob/main/1128/Main.java

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int W = sc.nextInt();
      int H = sc.nextInt();
      if (W == 0 && H == 0) {
        break;
      }

      int[][] P = new int[H][W];
      for (int r = 0; r < H; ++r) {
        for (int c = 0; c < W; ++c) {
          P[r][c] = sc.nextInt();
        }
      }

      System.out.println(solve(P));
    }

    sc.close();
  }

  static int solve(int[][] P) {
    int H = P.length;
    int W = P[0].length;

    int[][] sizes = buildSizes(P);
    int[][] covered = buildCovered(sizes);

    int required = 0;
    int[][] counts = new int[H][W];
    for (int r = 0; r < H; ++r) {
      for (int c = 0; c < W; ++c) {
        int minR = r - sizes[r][c] + 1;
        int minC = c - sizes[r][c] + 1;
        if (hasRequired(covered, minR, r, minC, c)) {
          for (int i = minR; i <= r; ++i) {
            for (int j = minC; j <= c; ++j) {
              ++counts[i][j];
            }
          }

          ++required;
          sizes[r][c] = 0;
        }
      }
    }

    int maxSize = 0;
    List<Point> points = new ArrayList<Point>();
    for (int r = 0; r < H; ++r) {
      for (int c = 0; c < W; ++c) {
        if (sizes[r][c] != 0) {
          maxSize = Math.max(maxSize, sizes[r][c]);
          points.add(new Point(r, c));
        }
      }
    }
    Collections.reverse(points);

    return required + idaStar(P, maxSize, sizes, points, counts);
  }

  static boolean hasRequired(int[][] covered, int minR, int maxR, int minC, int maxC) {
    for (int r = minR; r <= maxR; ++r) {
      for (int c = minC; c <= maxC; ++c) {
        if (covered[r][c] == 1) {
          return true;
        }
      }
    }

    return false;
  }

  static int idaStar(int[][] P, int maxSize, int[][] sizes, List<Point> points, int[][] counts) {
    if (maxSize == 0) {
      return 0;
    }

    for (int result = h(P, maxSize, counts); ; ++result) {
      if (search(P, maxSize, sizes, points, counts, result, 0)) {
        return result;
      }
    }
  }

  static boolean search(
      int[][] P,
      int maxSize,
      int[][] sizes,
      List<Point> points,
      int[][] counts,
      int rest,
      int index) {
    if (countNotCovered(P, counts, 0) == 0) {
      return true;
    }
    if (h(P, maxSize, counts) > rest
        || index == points.size()
        || countNotCovered(P, counts, points.get(index).r + 1) != 0) {
      return false;
    }

    if (search(P, maxSize, sizes, points, counts, rest, index + 1)) {
      return true;
    }

    int maxR = points.get(index).r;
    int maxC = points.get(index).c;
    for (int r = maxR - sizes[maxR][maxC] + 1; r <= maxR; ++r) {
      for (int c = maxC - sizes[maxR][maxC] + 1; c <= maxC; ++c) {
        ++counts[r][c];
      }
    }

    if (search(P, maxSize, sizes, points, counts, rest - 1, index + 1)) {
      return true;
    }

    for (int r = maxR - sizes[maxR][maxC] + 1; r <= maxR; ++r) {
      for (int c = maxC - sizes[maxR][maxC] + 1; c <= maxC; ++c) {
        --counts[r][c];
      }
    }

    return false;
  }

  static int h(int[][] P, int maxSize, int[][] counts) {
    return (countNotCovered(P, counts, 0) + maxSize * maxSize - 1) / (maxSize * maxSize);
  }

  static int countNotCovered(int[][] P, int[][] counts, int beginR) {
    int H = P.length;
    int W = P[0].length;

    int result = 0;
    for (int r = beginR; r < H; ++r) {
      for (int c = 0; c < W; ++c) {
        if (P[r][c] == 1 && counts[r][c] == 0) {
          ++result;
        }
      }
    }

    return result;
  }

  static int[][] buildCovered(int[][] sizes) {
    int H = sizes.length;
    int W = sizes[0].length;

    int[][] result = new int[H][W];
    for (int r = 0; r < H; ++r) {
      for (int c = 0; c < W; ++c) {
        for (int i = r - sizes[r][c] + 1; i <= r; ++i) {
          for (int j = c - sizes[r][c] + 1; j <= c; ++j) {
            ++result[i][j];
          }
        }
      }
    }

    return result;
  }

  static int[][] buildSizes(int[][] P) {
    int H = P.length;
    int W = P[0].length;

    int[][] result = new int[H][W];
    for (int r = 0; r < H; ++r) {
      for (int c = 0; c < W; ++c) {
        if (P[r][c] == 1) {
          result[r][c] =
              (r == 0 || c == 0)
                  ? 1
                  : (Math.min(Math.min(result[r - 1][c], result[r][c - 1]), result[r - 1][c - 1])
                      + 1);
        }
      }
    }
    for (int r = 0; r < H; ++r) {
      for (int c = 0; c < W; ++c) {
        int minR = r - result[r][c] + 1;
        int minC = c - result[r][c] + 1;
        for (int i = minR; i <= r; ++i) {
          for (int j = minC; j <= c; ++j) {
            if (!(i == r && j == c)
                && i - result[i][j] + 1 >= minR
                && j - result[i][j] + 1 >= minC) {
              result[i][j] = 0;
            }
          }
        }
      }
    }

    return result;
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