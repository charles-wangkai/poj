import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  static final int LIMIT = 1000000000;

  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int n = Integer.parseInt(st.nextToken());
    int m = Integer.parseInt(st.nextToken());
    int[] a = new int[n];
    st = new StringTokenizer(br.readLine());
    for (int i = 0; i < a.length; ++i) {
      a[i] = Integer.parseInt(st.nextToken());
    }
    int[] beginIndices = new int[m];
    int[] endIndices = new int[m];
    int[] sequences = new int[m];
    for (int i = 0; i < m; ++i) {
      st = new StringTokenizer(br.readLine());
      beginIndices[i] = Integer.parseInt(st.nextToken()) - 1;
      endIndices[i] = Integer.parseInt(st.nextToken()) - 1;
      sequences[i] = Integer.parseInt(st.nextToken());
    }

    System.out.println(solve(a, beginIndices, endIndices, sequences));
  }

  static String solve(int[] a, int[] beginIndices, int[] endIndices, int[] sequences) {
    Node segmentTree = buildSegmentTree(a, 0, a.length - 1);

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < beginIndices.length; ++i) {
      result
          .append(findKthNumber(segmentTree, beginIndices[i], endIndices[i], sequences[i]))
          .append("\n");
    }

    return result.toString();
  }

  static Node buildSegmentTree(int[] a, int beginIndex, int endIndex) {
    if (beginIndex == endIndex) {
      return new Node(beginIndex, endIndex, new int[] {a[beginIndex]}, null, null);
    }

    int[] sorted = new int[endIndex - beginIndex + 1];
    for (int i = 0; i < sorted.length; ++i) {
      sorted[i] = a[beginIndex + i];
    }
    Arrays.sort(sorted);

    int middleIndex = (beginIndex + endIndex) / 2;

    return new Node(
        beginIndex,
        endIndex,
        sorted,
        buildSegmentTree(a, beginIndex, middleIndex),
        buildSegmentTree(a, middleIndex + 1, endIndex));
  }

  static int findKthNumber(Node segmentTree, int beginIndex, int endIndex, int k) {
    int result = Integer.MIN_VALUE;
    int lower = -LIMIT;
    int upper = LIMIT;
    while (lower <= upper) {
      int middle = (lower + upper) / 2;
      if (query(beginIndex, endIndex, middle, segmentTree) >= k) {
        result = middle;
        upper = middle - 1;
      } else {
        lower = middle + 1;
      }
    }

    return result;
  }

  static int query(int beginIndex, int endIndex, int target, Node node) {
    if (node.beginIndex > endIndex || node.endIndex < beginIndex) {
      return 0;
    }
    if (!(node.beginIndex >= beginIndex && node.endIndex <= endIndex)) {
      return query(beginIndex, endIndex, target, node.left)
          + query(beginIndex, endIndex, target, node.right);
    }

    int index = Arrays.binarySearch(node.sorted, target);
    if (index < 0) {
      index = -1 - index - 1;
    }

    return index + 1;
  }
}

class Node {
  int beginIndex;
  int endIndex;
  int[] sorted;
  Node left;
  Node right;

  Node(int beginIndex, int endIndex, int[] sorted, Node left, Node right) {
    this.beginIndex = beginIndex;
    this.endIndex = endIndex;
    this.sorted = sorted;
    this.left = left;
    this.right = right;
  }
}
