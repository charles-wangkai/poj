import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int T = Integer.parseInt(st.nextToken());
    for (int tc = 0; tc < T; ++tc) {
      br.readLine();
      st = new StringTokenizer(br.readLine());
      int N = Integer.parseInt(st.nextToken());
      int K = Integer.parseInt(st.nextToken());
      int[] a = new int[N];
      int[] b = new int[N];
      int[] w = new int[N];
      for (int i = 0; i < N; ++i) {
        st = new StringTokenizer(br.readLine());
        a[i] = Integer.parseInt(st.nextToken());
        b[i] = Integer.parseInt(st.nextToken());
        w[i] = Integer.parseInt(st.nextToken());
      }

      System.out.println(solve(a, b, w, K));
    }
  }

  static int solve(int[] a, int[] b, int[] w, int K) {
    int N = a.length;

    int[] points = buildPoints(a, b);

    Map<Integer, Integer> pointToIndex = new HashMap<Integer, Integer>();
    for (int i = 0; i < points.length; ++i) {
      pointToIndex.put(points[i], i);
    }

    List<Edge> edges = new ArrayList<Edge>();

    @SuppressWarnings("unchecked")
    List<Integer>[] edgeLists = new List[points.length + 2];
    for (int i = 0; i < edgeLists.length; ++i) {
      edgeLists[i] = new ArrayList<Integer>();
    }
    addEdges(edges, edgeLists, 0, 1, K, 0);
    addEdges(edges, edgeLists, points.length, points.length + 1, K, 0);
    for (int i = 1; i < points.length; ++i) {
      addEdges(edges, edgeLists, i, i + 1, K + N, 0);
    }
    for (int i = 0; i < N; ++i) {
      int u = pointToIndex.get(a[i]) + 1;
      int v = pointToIndex.get(b[i]) + 1;

      addEdges(edges, edgeLists, v, u, 1, w[i]);
      addEdges(edges, edgeLists, 0, v, 1, 0);
      addEdges(edges, edgeLists, u, points.length + 1, 1, 0);
    }

    int result = -computeMinCostFlow(edges, edgeLists, 0, points.length + 1, K + N);
    for (int i = 0; i < N; ++i) {
      result += w[i];
    }

    return result;
  }

  static int computeMinCostFlow(List<Edge> edges, List<Integer>[] edgeLists, int s, int t, int f) {
    int N = edgeLists.length;

    int result = 0;
    int[] h = new int[N];
    while (f != 0) {
      PriorityQueue<Element> pq =
          new PriorityQueue<Element>(
              1,
              new Comparator<Element>() {
                public int compare(Element e1, Element e2) {
                  return e1.distance - e2.distance;
                }
              });

      int[] prevEdges = new int[N];
      int[] distances = new int[N];
      Arrays.fill(distances, Integer.MAX_VALUE);
      distances[s] = 0;
      pq.offer(new Element(s, 0));
      while (!pq.isEmpty()) {
        Element head = pq.poll();
        if (head.distance <= distances[head.index]) {
          for (int e : edgeLists[head.index]) {
            Edge edge = edges.get(e);
            if (edge.capacity != 0
                && distances[head.index] + edge.cost + h[head.index] - h[edge.to]
                    < distances[edge.to]) {
              distances[edge.to] = distances[head.index] + edge.cost + h[head.index] - h[edge.to];
              prevEdges[edge.to] = e;

              pq.offer(new Element(edge.to, distances[edge.to]));
            }
          }
        }
      }

      for (int i = 0; i < h.length; ++i) {
        h[i] += distances[i];
      }

      int d = f;
      for (int v = t; v != s; v = edges.get(prevEdges[v]).from) {
        d = Math.min(d, edges.get(prevEdges[v]).capacity);
      }
      f -= d;
      result += d * h[t];

      for (int v = t; v != s; v = edges.get(prevEdges[v]).from) {
        Edge edge = edges.get(prevEdges[v]);

        edge.capacity -= d;
        edges.get(prevEdges[v] ^ 1).capacity += d;
      }
    }

    return result;
  }

  static int[] buildPoints(int[] a, int[] b) {
    SortedSet<Integer> points = new TreeSet<Integer>();
    for (int i = 0; i < a.length; ++i) {
      points.add(a[i]);
      points.add(b[i]);
    }

    int[] result = new int[points.size()];
    int index = 0;
    for (int point : points) {
      result[index] = point;
      ++index;
    }

    return result;
  }

  static void addEdges(List<Edge> edges, List<Integer>[] edgeLists, int u, int v, int z, int cost) {
    edges.add(new Edge(u, v, z, cost));
    edgeLists[u].add(edges.size() - 1);

    edges.add(new Edge(v, u, 0, -cost));
    edgeLists[v].add(edges.size() - 1);
  }
}

class Edge {
  int from;
  int to;
  int capacity;
  int cost;

  Edge(int from, int to, int capacity, int cost) {
    this.from = from;
    this.to = to;
    this.capacity = capacity;
    this.cost = cost;
  }
}

class Element {
  int index;
  int distance;

  Element(int index, int distance) {
    this.index = index;
    this.distance = distance;
  }
}