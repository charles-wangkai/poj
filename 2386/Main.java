import java.util.Scanner;
import java.util.Stack;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int N = sc.nextInt();
    int M = sc.nextInt();
    char[][] squares = new char[N][M];
    for (int r = 0; r < N; ++r) {
      String line = sc.next();
      for (int c = 0; c < M; ++c) {
        squares[r][c] = line.charAt(c);
      }
    }

    System.out.println(solve(squares));

    sc.close();
  }

  static int solve(char[][] squares) {
    int N = squares.length;
    int M = squares[0].length;

    int result = 0;
    Stack<Point> stack = new Stack<Point>();
    for (int r = 0; r < N; ++r) {
      for (int c = 0; c < M; ++c) {
        if (squares[r][c] == 'W') {
          stack.push(new Point(r, c));
          while (!stack.empty()) {
            Point point = stack.pop();

            squares[point.r][point.c] = '.';

            for (int dr = -1; dr <= 1; ++dr) {
              for (int dc = -1; dc <= 1; ++dc) {
                int adjR = point.r + dr;
                int adjC = point.c + dc;
                if (adjR >= 0 && adjR < N && adjC >= 0 && adjC < M && squares[adjR][adjC] == 'W') {
                  stack.push(new Point(adjR, adjC));
                }
              }
            }
          }

          ++result;
        }
      }
    }

    return result;
  }
}

class Point {
  int r;
  int c;

  Point(int r, int c) {
    this.r = r;
    this.c = c;
  }
}
