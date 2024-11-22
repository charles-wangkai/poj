import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
  static Map<String, Boolean> dateToWin;

  public static void main(String[] args) {
    precompute();

    Scanner sc = new Scanner(System.in);

    int T = sc.nextInt();
    for (int tc = 0; tc < T; ++tc) {
      int year = sc.nextInt();
      int month = sc.nextInt();
      int day = sc.nextInt();

      System.out.println(solve(year, month, day) ? "YES" : "NO");
    }

    sc.close();
  }

  static void precompute() {
    dateToWin = new HashMap<String, Boolean>();

    Calendar calendar = new GregorianCalendar(2001, 10, 4);
    while (true) {
      dateToWin.put(
          toDate(calendar), isLoss(calendar, Calendar.DATE) || isLoss(calendar, Calendar.MONTH));

      if (toDate(calendar).equals("19000101")) {
        break;
      }

      calendar.add(Calendar.DATE, -1);
    }
  }

  static boolean isLoss(Calendar current, int field) {
    Calendar next = (Calendar) current.clone();
    next.add(field, 1);
    if (field == Calendar.MONTH
        && next.get(Calendar.DAY_OF_MONTH) != current.get(Calendar.DAY_OF_MONTH)) {
      return false;
    }

    String date = toDate(next);

    return dateToWin.containsKey(date) && !dateToWin.get(date);
  }

  static boolean solve(int year, int month, int day) {
    return dateToWin.get(toDate(new GregorianCalendar(year, month - 1, day)));
  }

  static String toDate(Calendar calendar) {
    return new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
  }
}