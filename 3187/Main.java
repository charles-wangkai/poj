import java.util.Scanner;

public class Main {
  static int[] sequence;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int sum = sc.nextInt();

    System.out.println(solve(N, sum));

    sc.close();
  }

  static String solve(int N, int sum) {
    int[] coeffs = {1};
    while (coeffs.length != N) {
      int[] nextCoeffs = new int[coeffs.length + 1];
      nextCoeffs[0] = 1;
      nextCoeffs[nextCoeffs.length - 1] = 1;
      for (int i = 1; i < nextCoeffs.length - 1; ++i) {
        nextCoeffs[i] = coeffs[i - 1] + coeffs[i];
      }

      coeffs = nextCoeffs;
    }

    int[] current = new int[N];
    for (int i = 0; i < current.length; ++i) {
      current[i] = i + 1;
    }

    search(sum, coeffs, current, 0);

    StringBuilder result = new StringBuilder();
    for (int x : sequence) {
      if (result.length() != 0) {
        result.append(' ');
      }
      result.append(x);
    }

    return result.toString();
  }

  static void search(int sum, int[] coeffs, int[] current, int index) {
    if (index == current.length) {
      int total = 0;
      for (int i = 0; i < coeffs.length; ++i) {
        total += coeffs[i] * current[i];
      }

      if (total == sum && (sequence == null || isLess(current, sequence))) {
        sequence = current.clone();
      }

      return;
    }

    for (int i = index; i < current.length; ++i) {
      swap(current, i, index);
      search(sum, coeffs, current, index + 1);
      swap(current, i, index);
    }
  }

  static boolean isLess(int[] a1, int[] a2) {
    for (int i = 0; i < a1.length; ++i) {
      if (a1[i] < a2[i]) {
        return true;
      }
      if (a1[i] > a2[i]) {
        return false;
      }
    }

    return false;
  }

  static void swap(int[] a, int index1, int index2) {
    int temp = a[index1];
    a[index1] = a[index2];
    a[index2] = temp;
  }
}
