import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int tc = 1;
    while (true) {
      int n = sc.nextInt();
      if (n == 0) {
        break;
      }

      int[] xMins = new int[n];
      int[] xMaxs = new int[n];
      int[] yMins = new int[n];
      int[] yMaxs = new int[n];
      for (int i = 0; i < n; ++i) {
        xMins[i] = sc.nextInt();
        xMaxs[i] = sc.nextInt();
        yMins[i] = sc.nextInt();
        yMaxs[i] = sc.nextInt();
      }
      int[] xs = new int[n];
      int[] ys = new int[n];
      for (int i = 0; i < n; ++i) {
        xs[i] = sc.nextInt();
        ys[i] = sc.nextInt();
      }

      System.out.println(
          String.format("Heap %d\n%s\n", tc, solve(xMins, xMaxs, yMins, yMaxs, xs, ys)));

      ++tc;
    }

    sc.close();
  }

  static String solve(int[] xMins, int[] xMaxs, int[] yMins, int[] yMaxs, int[] xs, int[] ys) {
    int n = xMins.length;

    List<Element> elements = new ArrayList<Element>();
    Outcome outcome = findMatchings(xMins, xMaxs, yMins, yMaxs, xs, ys, -1, -1);
    for (int i = 0; i < n; ++i) {
      if (findMatchings(xMins, xMaxs, yMins, yMaxs, xs, ys, i, outcome.leftVertices[i].matching)
              .matchingCount
          != n) {
        elements.add(new Element((char) (i + 'A'), outcome.leftVertices[i].matching + 1));
      }
    }

    StringBuilder result = new StringBuilder();
    for (Element element : elements) {
      if (result.length() != 0) {
        result.append(" ");
      }

      result.append(String.format("(%c,%d)", element.label, element.number));
    }

    return (result.length() == 0) ? "none" : result.toString();
  }

  static Outcome findMatchings(
      int[] xMins,
      int[] xMaxs,
      int[] yMins,
      int[] yMaxs,
      int[] xs,
      int[] ys,
      int leftRemovedIndex,
      int rightRemovedIndex) {
    int n = xMins.length;

    Vertex[] leftVertices = new Vertex[n];
    for (int i = 0; i < leftVertices.length; ++i) {
      leftVertices[i] = new Vertex();
    }
    Vertex[] rightVertices = new Vertex[n];
    for (int i = 0; i < rightVertices.length; ++i) {
      rightVertices[i] = new Vertex();
    }
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        if (!(i == leftRemovedIndex && j == rightRemovedIndex)
            && xs[j] > xMins[i]
            && xs[j] < xMaxs[i]
            && ys[j] > yMins[i]
            && ys[j] < yMaxs[i]) {
          leftVertices[i].adjs.add(j);
          rightVertices[j].adjs.add(i);
        }
      }
    }

    int matchingCount = 0;
    for (int i = 0; i < leftVertices.length; ++i) {
      if (leftVertices[i].matching == -1
          && search(leftVertices, rightVertices, new boolean[rightVertices.length], i)) {
        ++matchingCount;
      }
    }

    return new Outcome(matchingCount, leftVertices, rightVertices);
  }

  static boolean search(
      Vertex[] leftVertices, Vertex[] rightVertices, boolean[] rightVisited, int leftIndex) {
    for (int rightIndex : leftVertices[leftIndex].adjs) {
      if (!rightVisited[rightIndex]) {
        rightVisited[rightIndex] = true;

        if (rightVertices[rightIndex].matching == -1
            || search(
                leftVertices, rightVertices, rightVisited, rightVertices[rightIndex].matching)) {
          leftVertices[leftIndex].matching = rightIndex;
          rightVertices[rightIndex].matching = leftIndex;

          return true;
        }
      }
    }

    return false;
  }
}

class Outcome {
  int matchingCount;
  Vertex[] leftVertices;
  Vertex[] rightVertices;

  Outcome(int matchingCount, Vertex[] leftVertices, Vertex[] rightVertices) {
    this.matchingCount = matchingCount;
    this.leftVertices = leftVertices;
    this.rightVertices = rightVertices;
  }
}

class Vertex {
  List<Integer> adjs = new ArrayList<Integer>();
  int matching = -1;
}

class Element {
  char label;
  int number;

  Element(char label, int number) {
    this.label = label;
    this.number = number;
  }
}