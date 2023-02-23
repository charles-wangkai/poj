import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int n = sc.nextInt();
    int m = sc.nextInt();
    int[] beginIndices = new int[m];
    int[] endIndices = new int[m];
    for (int i = 0; i < m; ++i) {
      beginIndices[i] = sc.nextInt() - 1;
      endIndices[i] = sc.nextInt() - 1;
    }

    System.out.println(solve(n, beginIndices, endIndices));

    sc.close();
  }

  static int solve(int n, int[] beginIndices, int[] endIndices) {
    int[] minLengths = new int[n];
    Arrays.fill(minLengths, Integer.MAX_VALUE);
    minLengths[0] = 0;

    Node segmentTree = buildSegmentTree(minLengths, 0, minLengths.length - 1);
    for (int i = 0; i < beginIndices.length; ++i) {
      int prevLength = query(beginIndices[i], endIndices[i] - 1, segmentTree);
      if (prevLength != Integer.MAX_VALUE && prevLength + 1 < minLengths[endIndices[i]]) {
        minLengths[endIndices[i]] = prevLength + 1;
        update(endIndices[i], prevLength + 1, segmentTree);
      }
    }

    return minLengths[minLengths.length - 1];
  }

  static Node buildSegmentTree(int[] minLengths, int beginIndex, int endIndex) {
    if (beginIndex == endIndex) {
      return new Node(beginIndex, endIndex, minLengths[beginIndex], null, null);
    }

    int middleIndex = (beginIndex + endIndex) / 2;
    Node left = buildSegmentTree(minLengths, beginIndex, middleIndex);
    Node right = buildSegmentTree(minLengths, middleIndex + 1, endIndex);

    return new Node(beginIndex, endIndex, Math.min(left.minValue, right.minValue), left, right);
  }

  static int query(int beginIndex, int endIndex, Node node) {
    if (node.beginIndex > endIndex || node.endIndex < beginIndex) {
      return Integer.MAX_VALUE;
    }

    return (node.beginIndex >= beginIndex && node.endIndex <= endIndex)
        ? node.minValue
        : Math.min(query(beginIndex, endIndex, node.left), query(beginIndex, endIndex, node.right));
  }

  static void update(int index, int value, Node node) {
    if (node.beginIndex <= index && node.endIndex >= index) {
      if (node.beginIndex == node.endIndex) {
        node.minValue = value;
      } else {
        update(index, value, node.left);
        update(index, value, node.right);

        node.minValue = Math.min(node.left.minValue, node.right.minValue);
      }
    }
  }
}

class Node {
  int beginIndex;
  int endIndex;
  int minValue;
  Node left;
  Node right;

  Node(int beginIndex, int endIndex, int minValue, Node left, Node right) {
    this.beginIndex = beginIndex;
    this.endIndex = endIndex;
    this.minValue = minValue;
    this.left = left;
    this.right = right;
  }
}
