import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while (true) {
      int N = sc.nextInt();
      if (N == 0) {
        break;
      }

      double L = sc.nextDouble();
      double V = sc.nextDouble();
      char[] DIRs = new char[N];
      double[] POSs = new double[N];
      String[] NAMEs = new String[N];
      for (int i = 0; i < N; ++i) {
        DIRs[i] = sc.next().charAt(0);
        POSs[i] = sc.nextDouble();
        NAMEs[i] = sc.next();
      }

      System.out.println(solve(DIRs, POSs, NAMEs, L, V));
    }

    sc.close();
  }

  static String solve(char[] DIRs, double[] POSs, String[] NAMEs, double L, double V) {
    int N = DIRs.length;

    Element[] elements = new Element[N];
    for (int i = 0; i < elements.length; ++i) {
      boolean leftOrRight = Character.toLowerCase(DIRs[i]) == 'n';
      elements[i] = new Element((leftOrRight ? POSs[i] : (L - POSs[i])) / V, leftOrRight);
    }
    Arrays.sort(
        elements,
        new Comparator<Element>() {
          public int compare(Element e1, Element e2) {
            return Double.compare(e1.time, e2.time);
          }
        });

    int leftIndex = 0;
    for (int i = 0; i < elements.length - 1; ++i) {
      if (elements[i].leftOrRight) {
        ++leftIndex;
      }
    }

    return String.format(
        "%13.2f %s", Math.floor(elements[elements.length - 1].time * 100) / 100, NAMEs[leftIndex]);
  }
}

class Element {
  double time;
  boolean leftOrRight;

  Element(double time, boolean leftOrRight) {
    this.time = time;
    this.leftOrRight = leftOrRight;
  }
}