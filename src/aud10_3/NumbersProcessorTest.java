package aud10_3;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


class Row {
    List<Integer> numbers;

    Row(String line) {
        numbers = Arrays.stream(line.split("\\s+"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public boolean condition() {
        //max number to have the max frequency as well

        int max = max();

        //{1:1, 2:1, 3:1, 4:3}
        Map<Integer, Long> countingMap = numbers.stream()
                .collect(Collectors.groupingBy(
                        i -> i,
                        Collectors.counting()
                ));

        int frequencyOfMaxNumber = countingMap.get(max).intValue();

        int maxFrequency = countingMap.values().stream()
                .mapToInt(Long::intValue)
                .max()
                .getAsInt();

        return frequencyOfMaxNumber == maxFrequency;

    }

    public int max () {
        return numbers.stream().mapToInt(i -> i).max().getAsInt();
    }
}

class NumberProcessor {

    List<Row> rows;

    public NumberProcessor() {
        rows = new ArrayList<>();
    }

    void readRows(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        rows = br.lines()
                .map(Row::new)
                .collect(Collectors.toList());
    }


    public void printMaxFromRows(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);

        rows.stream()
                .filter(Row::condition)
                .map(Row::max)
                .forEach(pw::println);

        pw.flush();
        pw.close();
    }
}

public class NumbersProcessorTest {

    public static void main(String[] args) {


        NumberProcessor numberProcessor = new NumberProcessor();

        numberProcessor.readRows(System.in);

        numberProcessor.printMaxFromRows(System.out);


    }
}