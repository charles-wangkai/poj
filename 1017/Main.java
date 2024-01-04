// RE on POJ. AC on Baekjoon OJ: https://www.acmicpc.net/source/55258540

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int[] packetNums = new int[7];
      boolean hasNonZero = false;
      for (int i = 1; i < packetNums.length; ++i) {
        packetNums[i] = sc.nextInt();
        if (packetNums[i] != 0) {
          hasNonZero = true;
        }
      }

      if (!hasNonZero) {
        break;
      }

      System.out.println(solve(packetNums));
    }

    sc.close();
  }

  static int solve(int[] packetNums) {
    int result = fit6(packetNums);
    result += fit5(packetNums);
    result += fit4(packetNums);
    result += fit3(packetNums);
    result += fit2(packetNums);
    result += fit1(packetNums);

    return result;
  }

  static int fit6(int[] packetNums) {
    return packetNums[6];
  }

  static int fit5(int[] packetNums) {
    fitExtra1(packetNums, 11 * packetNums[5]);

    return packetNums[5];
  }

  static int fit4(int[] packetNums) {
    fitExtra2(packetNums, 5 * packetNums[4], 0);

    return packetNums[4];
  }

  static int fit3(int[] packetNums) {
    int rest = packetNums[3] % 4;
    if (rest == 1) {
      fitExtra2(packetNums, 5, 7);
    } else if (rest == 2) {
      fitExtra2(packetNums, 3, 6);
    } else if (rest == 3) {
      fitExtra2(packetNums, 1, 5);
    }

    return (packetNums[3] + 3) / 4;
  }

  static int fit2(int[] packetNums) {
    if (packetNums[2] % 9 != 0) {
      fitExtra1(packetNums, 36 - packetNums[2] % 9 * 4);
    }

    return (packetNums[2] + 8) / 9;
  }

  static int fit1(int[] packetNums) {
    return (packetNums[1] + 35) / 36;
  }

  static void fitExtra2(int[] packetNums, int extra2, int extra1) {
    int free2 = Math.min(packetNums[2], extra2);
    packetNums[2] -= free2;
    extra2 -= free2;

    fitExtra1(packetNums, extra1 + 4 * extra2);
  }

  static void fitExtra1(int[] packetNums, int extra1) {
    packetNums[1] -= Math.min(packetNums[1], extra1);
  }
}
