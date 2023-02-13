import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws Throwable {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    StringTokenizer st = new StringTokenizer(br.readLine());
    int P = Integer.parseInt(st.nextToken());
    int[] ideas = new int[P];
    st = new StringTokenizer(br.readLine());
    for (int i = 0; i < ideas.length; ++i) {
      ideas[i] = Integer.parseInt(st.nextToken());
    }

    System.out.println(solve(ideas));
  }

  static int solve(int[] ideas) {
    Set<Integer> ideaSet = new HashSet<Integer>();
    for (int idea : ideas) {
      ideaSet.add(idea);
    }

    int result = Integer.MAX_VALUE;
    Map<Integer, Integer> ideaToCount = new HashMap<Integer, Integer>();
    int beginIndex = 0;
    for (int endIndex = 0; endIndex < ideas.length; ++endIndex) {
      if (!ideaToCount.containsKey(ideas[endIndex])) {
        ideaToCount.put(ideas[endIndex], 0);
      }
      ideaToCount.put(ideas[endIndex], ideaToCount.get(ideas[endIndex]) + 1);

      while (ideaToCount.size() == ideaSet.size() && ideaToCount.get(ideas[beginIndex]) >= 2) {
        ideaToCount.put(ideas[beginIndex], ideaToCount.get(ideas[beginIndex]) - 1);
        ++beginIndex;
      }

      if (ideaToCount.size() == ideaSet.size()) {
        result = Math.min(result, endIndex - beginIndex + 1);
      }
    }

    return result;
  }
}
