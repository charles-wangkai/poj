import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    char[] initials = new char[N];
    for (int i = 0; i < initials.length; ++i) {
      initials[i] = sc.next().charAt(0);
    }

    System.out.println(solve(initials));

    sc.close();
  }

  static String solve(char[] initials) {
    StringBuilder result = new StringBuilder();
    int leftIndex = 0;
    int rightIndex = initials.length - 1;
    int count = 0;
    while (leftIndex <= rightIndex) {
      boolean leftOrRight = true;
      for (int i = leftIndex, j = rightIndex; i <= j; ++i, --j) {
        if (initials[i] < initials[j]) {
          leftOrRight = true;

          break;
        } else if (initials[i] > initials[j]) {
          leftOrRight = false;

          break;
        }
      }

      if (count != 0 && count % 80 == 0) {
        result.append("\n");
      }
      if (leftOrRight) {
        result.append(initials[leftIndex]);
        ++leftIndex;
      } else {
        result.append(initials[rightIndex]);
        --rightIndex;
      }
      ++count;
    }

    return result.toString();
  }
}
