import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int C = sc.nextInt();
    int L = sc.nextInt();
    int[] minSpf = new int[C];
    int[] maxSpf = new int[C];
    for (int i = 0; i < C; ++i) {
      minSpf[i] = sc.nextInt();
      maxSpf[i] = sc.nextInt();
    }
    int[] spf = new int[L];
    int[] cover = new int[L];
    for (int i = 0; i < L; ++i) {
      spf[i] = sc.nextInt();
      cover[i] = sc.nextInt();
    }

    System.out.println(solve(minSpf, maxSpf, spf, cover));

    sc.close();
  }

  static int solve(int[] minSpf, final int[] maxSpf, final int[] spf, int[] cover) {
    Integer[] sortedCowIndices = new Integer[minSpf.length];
    for (int i = 0; i < sortedCowIndices.length; ++i) {
      sortedCowIndices[i] = i;
    }
    Arrays.sort(
        sortedCowIndices,
        new Comparator<Integer>() {
          public int compare(Integer i1, Integer i2) {
            return maxSpf[i1] - maxSpf[i2];
          }
        });

    Integer[] sortedLotionIndices = new Integer[spf.length];
    for (int i = 0; i < sortedLotionIndices.length; ++i) {
      sortedLotionIndices[i] = i;
    }
    Arrays.sort(
        sortedLotionIndices,
        new Comparator<Integer>() {
          public int compare(Integer i1, Integer i2) {
            return spf[i1] - spf[i2];
          }
        });

    int result = 0;
    boolean[] covered = new boolean[minSpf.length];
    for (int lotionIndex : sortedLotionIndices) {
      for (int cowIndex : sortedCowIndices) {
        if (!covered[cowIndex]
            && cover[lotionIndex] != 0
            && spf[lotionIndex] >= minSpf[cowIndex]
            && spf[lotionIndex] <= maxSpf[cowIndex]) {
          ++result;
          covered[cowIndex] = true;
          --cover[lotionIndex];
        }
      }
    }

    return result;
  }
}
