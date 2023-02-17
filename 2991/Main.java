import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (sc.hasNextInt()) {
      int n = sc.nextInt();
      int c = sc.nextInt();
      int[] l = new int[n];
      for (int i = 0; i < l.length; ++i) {
        l[i] = sc.nextInt();
      }
      int[] s = new int[c];
      int[] a = new int[c];
      for (int i = 0; i < c; ++i) {
        s[i] = sc.nextInt();
        a[i] = sc.nextInt();
      }

      System.out.println(solve(l, s, a));
    }

    sc.close();
  }

  static String solve(int[] l, int[] s, int[] a) {
    double[] prevAngles = new double[l.length];
    Arrays.fill(prevAngles, Math.PI);

    Node segmentTree = buildSegmentTree(l, 0, l.length - 1);

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < s.length; ++i) {
      update(s[i] - 1, Math.toRadians(a[i]) - prevAngles[s[i] - 1], segmentTree);
      result.append(String.format("%.2f %.2f", segmentTree.x, segmentTree.y)).append("\n");

      prevAngles[s[i] - 1] = Math.toRadians(a[i]);
    }

    return result.toString();
  }

  static void update(int index, double angleDelta, Node node) {
    if (index >= node.beginIndex && index <= node.endIndex && node.beginIndex != node.endIndex) {
      update(index, angleDelta, node.left);
      update(index, angleDelta, node.right);

      if (index <= node.left.endIndex) {
        node.rightToLeftAngle += angleDelta;
      }

      node.x =
          node.left.x
              + (Math.cos(node.rightToLeftAngle) * node.right.x
                  - Math.sin(node.rightToLeftAngle) * node.right.y);
      node.y =
          node.left.y
              + (Math.sin(node.rightToLeftAngle) * node.right.x
                  + Math.cos(node.rightToLeftAngle) * node.right.y);
    }
  }

  static Node buildSegmentTree(int[] l, int beginIndex, int endIndex) {
    if (beginIndex == endIndex) {
      return new Node(beginIndex, endIndex, 0, l[beginIndex], 0, null, null);
    }

    int middleIndex = (beginIndex + endIndex) / 2;
    Node left = buildSegmentTree(l, beginIndex, middleIndex);
    Node right = buildSegmentTree(l, middleIndex + 1, endIndex);

    return new Node(beginIndex, endIndex, 0, left.y + right.y, 0, left, right);
  }
}

class Node {
  int beginIndex;
  int endIndex;
  double x;
  double y;
  double rightToLeftAngle;
  Node left;
  Node right;

  Node(
      int beginIndex,
      int endIndex,
      double x,
      double y,
      double rightToLeftAngle,
      Node left,
      Node right) {
    this.beginIndex = beginIndex;
    this.endIndex = endIndex;
    this.x = x;
    this.y = y;
    this.rightToLeftAngle = rightToLeftAngle;
    this.left = left;
    this.right = right;
  }
}
