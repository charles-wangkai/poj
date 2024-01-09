import java.util.Scanner;

public class Main {
  static final int SIZE = 20;

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int[] states = new int[SIZE];
    for (int i = 0; i < states.length; ++i) {
      states[i] = sc.nextInt();
    }

    System.out.println(solve(states));

    sc.close();
  }

  static int solve(int[] states) {
    return Math.min(computeFlipNum(states.clone(), true), computeFlipNum(states.clone(), false));
  }

  static int computeFlipNum(int[] states, boolean flipFirst) {
    int result = 0;
    if (flipFirst) {
      flip(states, 0);
      ++result;
    }
    for (int i = 0; i < states.length - 1; ++i) {
      if (states[i] == 1) {
        flip(states, i + 1);
        ++result;
      }
    }

    return (states[states.length - 1] == 0) ? result : Integer.MAX_VALUE;
  }

  static void flip(int[] states, int index) {
    if (index != 0) {
      states[index - 1] ^= 1;
    }

    states[index] ^= 1;

    if (index != states.length - 1) {
      states[index + 1] ^= 1;
    }
  }
}