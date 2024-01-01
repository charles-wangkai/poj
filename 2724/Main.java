// http://poj.org/showmessage?message_id=357399

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int N = sc.nextInt();
      int M = sc.nextInt();
      if (N == 0 && M == 0) {
        break;
      }

      String[] states = new String[M];
      for (int i = 0; i < states.length; ++i) {
        states[i] = sc.next();
      }

      System.out.println(solve(states));
    }

    sc.close();
  }

  static int solve(String[] states) {
    List<Vertex> leftVertices = new ArrayList<Vertex>();
    List<Vertex> rightVertices = new ArrayList<Vertex>();
    Set<String> cheeses = new HashSet<String>();
    for (String state : states) {
      cheeses.add(state.replace('*', '0'));
      cheeses.add(state.replace('*', '1'));
    }
    for (String cheese : cheeses) {
      ((cheese.replace("1", "").length() % 2 == 0) ? leftVertices : rightVertices)
          .add(new Vertex(cheese));
    }
    for (int leftIndex = 0; leftIndex < leftVertices.size(); ++leftIndex) {
      for (int rightIndex = 0; rightIndex < rightVertices.size(); ++rightIndex) {
        if (computeDiffCount(
                leftVertices.get(leftIndex).cheese, rightVertices.get(rightIndex).cheese)
            == 1) {
          leftVertices.get(leftIndex).adjs.add(rightIndex);
          rightVertices.get(rightIndex).adjs.add(leftIndex);
        }
      }
    }

    int matchingCount = 0;
    for (int i = 0; i < leftVertices.size(); ++i) {
      if (leftVertices.get(i).matching == -1
          && search(leftVertices, rightVertices, new boolean[rightVertices.size()], i)) {
        ++matchingCount;
      }
    }

    return leftVertices.size() + rightVertices.size() - matchingCount;
  }

  static boolean search(
      List<Vertex> leftVertices,
      List<Vertex> rightVertices,
      boolean[] rightVisited,
      int leftIndex) {
    for (int rightIndex : leftVertices.get(leftIndex).adjs) {
      if (!rightVisited[rightIndex]) {
        rightVisited[rightIndex] = true;

        if (rightVertices.get(rightIndex).matching == -1
            || search(
                leftVertices,
                rightVertices,
                rightVisited,
                rightVertices.get(rightIndex).matching)) {
          leftVertices.get(leftIndex).matching = rightIndex;
          rightVertices.get(rightIndex).matching = leftIndex;

          return true;
        }
      }
    }

    return false;
  }

  static int computeDiffCount(String s1, String s2) {
    int result = 0;
    for (int i = 0; i < s1.length(); ++i) {
      if (s1.charAt(i) != s2.charAt(i)) {
        ++result;
      }
    }

    return result;
  }
}

class Vertex {
  String cheese;
  List<Integer> adjs = new ArrayList<Integer>();
  int matching = -1;

  Vertex(String cheese) {
    this.cheese = cheese;
  }
}