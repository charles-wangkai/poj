import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int n = Integer.parseInt(st.nextToken());
    int q = Integer.parseInt(st.nextToken());
    int s = Integer.parseInt(st.nextToken());
    int[] a = new int[n - 1];
    int[] b = new int[n - 1];
    int[] w = new int[n - 1];
    for (int i = 0; i < n - 1; ++i) {
      st = new StringTokenizer(br.readLine());
      a[i] = Integer.parseInt(st.nextToken());
      b[i] = Integer.parseInt(st.nextToken());
      w[i] = Integer.parseInt(st.nextToken());
    }

    solve(br, a, b, w, q, s);
  }

  static void solve(BufferedReader br, int[] a, int[] b, int[] w, int q, int s) throws Throwable {
    int n = a.length + 1;

    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[n];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    for (int i = 0; i < a.length; ++i) {
      edgeLists[a[i] - 1].add(i);
      edgeLists[b[i] - 1].add(i);
    }

    List<Integer> visitedNodes = new ArrayList<Integer>();
    List<Integer> visitedDepths = new ArrayList<Integer>();
    int[] firstIndices = new int[n];
    int[] binaryIndexedTree = new int[Integer.highestOneBit(n * 2) * 2 + 1];
    int[] downEdgeIndices = new int[n - 1];
    int[] upEdgeIndices = new int[n - 1];
    search(
        a,
        b,
        w,
        edgeLists,
        visitedNodes,
        visitedDepths,
        firstIndices,
        binaryIndexedTree,
        downEdgeIndices,
        upEdgeIndices,
        0,
        0,
        -1);

    int size = Integer.toBinaryString(visitedNodes.size()).length();
    int[][] minIndices = new int[visitedNodes.size()][size];
    for (int i = 0; i < minIndices.length; ++i) {
      minIndices[i][0] = i;
    }
    for (int j = 1; j < size; ++j) {
      for (int i = 0; i < minIndices.length; ++i) {
        if (i + (1 << j) <= minIndices.length) {
          minIndices[i][j] =
              mergeIndex(
                  visitedDepths, minIndices[i][j - 1], minIndices[i + (1 << (j - 1))][j - 1]);
        }
      }
    }

    int v = s - 1;
    for (int i = 0; i < q; ++i) {
      String query = br.readLine();

      String[] parts = query.split(" ");
      if (parts[0].equals("0")) {
        int u = Integer.parseInt(parts[1]) - 1;

        int beginIndex = Math.min(firstIndices[v], firstIndices[u]);
        int endIndex = Math.max(firstIndices[v], firstIndices[u]);

        int exp = Integer.toBinaryString(endIndex - beginIndex + 1).length() - 1;
        int lca =
            visitedNodes.get(
                mergeIndex(
                    visitedDepths,
                    minIndices[beginIndex][exp],
                    minIndices[endIndex - (1 << exp) + 1][exp]));

        System.out.println(
            computeSum(binaryIndexedTree, firstIndices[v])
                + computeSum(binaryIndexedTree, firstIndices[u])
                - 2 * computeSum(binaryIndexedTree, firstIndices[lca]));

        v = u;
      } else {
        int edge = Integer.parseInt(parts[1]) - 1;
        int weight = Integer.parseInt(parts[2]);

        add(binaryIndexedTree, downEdgeIndices[edge], weight - w[edge]);
        add(binaryIndexedTree, upEdgeIndices[edge], w[edge] - weight);

        w[edge] = weight;
      }
    }
  }

  static int mergeIndex(List<Integer> visitedDepths, int index1, int index2) {
    return (visitedDepths.get(index1) < visitedDepths.get(index2)) ? index1 : index2;
  }

  static void search(
      int[] a,
      int[] b,
      int[] w,
      List<Integer>[] edgeLists,
      List<Integer> visitedNodes,
      List<Integer> visitedDepths,
      int[] firstIndices,
      int[] binaryIndexedTree,
      int[] downEdgeIndices,
      int[] upEdgeIndices,
      int depth,
      int node,
      int parent) {
    firstIndices[node] = visitedNodes.size();

    visitedNodes.add(node);
    visitedDepths.add(depth);

    for (int edge : edgeLists[node]) {
      int other = (node == a[edge] - 1) ? (b[edge] - 1) : (a[edge] - 1);
      if (other != parent) {
        add(binaryIndexedTree, visitedNodes.size(), w[edge]);
        downEdgeIndices[edge] = visitedNodes.size();

        search(
            a,
            b,
            w,
            edgeLists,
            visitedNodes,
            visitedDepths,
            firstIndices,
            binaryIndexedTree,
            downEdgeIndices,
            upEdgeIndices,
            depth + 1,
            other,
            node);

        visitedNodes.add(node);
        visitedDepths.add(depth);

        add(binaryIndexedTree, visitedNodes.size(), -w[edge]);
        upEdgeIndices[edge] = visitedNodes.size();
      }
    }
  }

  static void add(int[] binaryIndexedTree, int i, int x) {
    while (i < binaryIndexedTree.length) {
      binaryIndexedTree[i] += x;
      i += i & -i;
    }
  }

  static int computeSum(int[] binaryIndexedTree, int i) {
    int result = 0;
    while (i != 0) {
      result += binaryIndexedTree[i];
      i -= i & -i;
    }

    return result;
  }
}