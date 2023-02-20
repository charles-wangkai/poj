import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int Q = sc.nextInt();
    int[] A = new int[N];
    for (int i = 0; i < A.length; ++i) {
      A[i] = sc.nextInt();
    }
    sc.nextLine();
    String[] operations = new String[Q];
    for (int i = 0; i < operations.length; ++i) {
      operations[i] = sc.nextLine();
    }

    System.out.println(solve(A, operations));

    sc.close();
  }

  static String solve(int[] A, String[] operations) {
    Node segmentTree = buildSegmentTree(A, 0, A.length - 1);

    StringBuilder result = new StringBuilder();
    for (String operation : operations) {
      String[] parts = operation.split(" ");
      if (parts[0].equals("C")) {
        int a = Integer.parseInt(parts[1]);
        int b = Integer.parseInt(parts[2]);
        int c = Integer.parseInt(parts[3]);

        update(a - 1, b - 1, c, segmentTree);
      } else {
        int a = Integer.parseInt(parts[1]);
        int b = Integer.parseInt(parts[2]);

        result.append(query(a - 1, b - 1, segmentTree)).append("\n");
      }
    }

    return result.toString();
  }

  static void update(int beginIndex, int endIndex, int delta, Node node) {
    if (!(node.beginIndex > endIndex || node.endIndex < beginIndex)) {
      if (node.beginIndex >= beginIndex && node.endIndex <= endIndex) {
        node.extra += delta;
      } else {
        update(beginIndex, endIndex, delta, node.left);
        update(beginIndex, endIndex, delta, node.right);

        node.sum =
            query(node.left.beginIndex, node.left.endIndex, node.left)
                + query(node.right.beginIndex, node.right.endIndex, node.right);
      }
    }
  }

  static long query(int beginIndex, int endIndex, Node node) {
    if (node.beginIndex > endIndex || node.endIndex < beginIndex) {
      return 0;
    }

    return ((node.beginIndex >= beginIndex && node.endIndex <= endIndex)
            ? node.sum
            : (query(beginIndex, endIndex, node.left) + query(beginIndex, endIndex, node.right)))
        + node.extra
            * (Math.min(endIndex, node.endIndex) - Math.max(beginIndex, node.beginIndex) + 1L);
  }

  static Node buildSegmentTree(int[] A, int beginIndex, int endIndex) {
    if (beginIndex == endIndex) {
      return new Node(beginIndex, endIndex, A[beginIndex], 0, null, null);
    }

    int middleIndex = (beginIndex + endIndex) / 2;
    Node left = buildSegmentTree(A, beginIndex, middleIndex);
    Node right = buildSegmentTree(A, middleIndex + 1, endIndex);

    return new Node(beginIndex, endIndex, left.sum + right.sum, 0, left, right);
  }
}

class Node {
  int beginIndex;
  int endIndex;
  long sum;
  int extra;
  Node left;
  Node right;

  Node(int beginIndex, int endIndex, long sum, int extra, Node left, Node right) {
    this.beginIndex = beginIndex;
    this.endIndex = endIndex;
    this.sum = sum;
    this.extra = extra;
    this.left = left;
    this.right = right;
  }
}
