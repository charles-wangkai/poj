// https://www.hankcs.com/program/algorithm/poj-1201-intervals.html

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class Main {
  static final int LIMIT = 50000;

  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int n = Integer.parseInt(st.nextToken());
    int[] a = new int[n];
    int[] b = new int[n];
    int[] c = new int[n];
    for (int i = 0; i < n; ++i) {
      st = new StringTokenizer(br.readLine());
      a[i] = Integer.parseInt(st.nextToken());
      b[i] = Integer.parseInt(st.nextToken());
      c[i] = Integer.parseInt(st.nextToken());
    }

    System.out.println(solve(a, b, c));
  }

  static int solve(int[] a, final int[] b, int[] c) {
    List<Integer> sortedIndices = new ArrayList<Integer>();
    for (int i = 0; i < a.length; ++i) {
      sortedIndices.add(i);
    }
    Collections.sort(
        sortedIndices,
        new Comparator<Integer>() {
          @Override
          public int compare(Integer i1, Integer i2) {
            return b[i1] - b[i2];
          }
        });

    NavigableSet<Integer> restIndices = new TreeSet<Integer>();
    for (int i = 0; i <= LIMIT; ++i) {
      restIndices.add(i);
    }

    int result = 0;
    int[] binaryIndexedTree = new int[Integer.highestOneBit(LIMIT + 1) * 2 + 1];
    for (int index : sortedIndices) {
      int covered =
          computeSum(binaryIndexedTree, b[index] + 1) - computeSum(binaryIndexedTree, a[index]);

      int lastIndex = b[index] + 1;
      while (covered < c[index]) {
        lastIndex = restIndices.lower(lastIndex);
        restIndices.remove(lastIndex);

        add(binaryIndexedTree, lastIndex + 1, 1);
        ++covered;
        ++result;
      }
    }

    return result;
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