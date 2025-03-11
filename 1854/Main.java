// https://www.hankcs.com/program/algorithm/poj-1854-evil-straw-warts-live.html

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int n = sc.nextInt();
    for (int tc = 0; tc < n; ++tc) {
      String s = sc.next();

      System.out.println(solve(s));
    }

    sc.close();
  }

  static String solve(String s) {
    if (!isPossible(s)) {
      return "Impossible";
    }

    int result = 0;
    char[] letters = s.toCharArray();
    for (int i = 0, j = letters.length - 1; i < j; ++i, --j) {
      int leftIndex = i;
      while (letters[leftIndex] != letters[j]) {
        ++leftIndex;
      }

      int rightIndex = j;
      while (letters[rightIndex] != letters[i]) {
        --rightIndex;
      }

      if (leftIndex - i <= j - rightIndex) {
        result += leftIndex - i;
        for (int k = leftIndex; k > i; --k) {
          swap(letters, k, k - 1);
        }
      } else {
        result += j - rightIndex;
        for (int k = rightIndex; k < j; ++k) {
          swap(letters, k, k + 1);
        }
      }
    }

    return String.valueOf(result);
  }

  static void swap(char[] letters, int index1, int index2) {
    char temp = letters[index1];
    letters[index1] = letters[index2];
    letters[index2] = temp;
  }

  static boolean isPossible(String s) {
    Map<Character, Integer> letterToCount = new HashMap<Character, Integer>();
    for (char letter : s.toCharArray()) {
      if (!letterToCount.containsKey(letter)) {
        letterToCount.put(letter, 0);
      }
      letterToCount.put(letter, letterToCount.get(letter) + 1);
    }

    int odd = 0;
    for (int count : letterToCount.values()) {
      if (count % 2 == 1) {
        ++odd;
      }
    }

    return odd <= 1;
  }
}