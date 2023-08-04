import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


class Result {

    /*
     * Complete the 'slotWheels' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts STRING_ARRAY history as parameter.
     */

    public static int slotWheels(List<String> history) {
        return calculate(0, history);
    }

    public static int calculate(int stops, List<String> history) {
        List<String> newHistory = new ArrayList<>();
        List<Integer> maxValues = new ArrayList<>();
        if (history.stream().allMatch(String::isEmpty)) {
            return stops;
        } else {
            for (String row : history) {
                boolean seen = false;
                int best = 0;
                for (String s : row.split("")) {
                    int asNumber = Integer.parseInt(s);
                    if (!seen || asNumber > best) {
                        seen = true;
                        best = asNumber;
                    }
                }
                int result;
                if (seen) {
                    result = best;
                } else {
                    result = 0;
                }
                maxValues.add(result);
                newHistory.add(row.replaceFirst(Integer.toString(result), ""));
            }
        }
        int max = Collections.max(maxValues);
        return stops + calculate(max, newHistory);
    }
}
class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int historyCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> history = IntStream.range(0, historyCount).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(toList());

        int result = Result.slotWheels(history);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}

