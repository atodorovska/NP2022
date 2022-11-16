package aud6_1;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


class LineProcessor {

    List<String> lines;

    public LineProcessor() {
        lines = new ArrayList<>();
    }

    private int countOcc(String line, char c) {
        return (int) line.toLowerCase()
                .chars().filter(i -> ((char) i == c))
                .count();
    }


    public void readLines(InputStream in, OutputStream out, char c) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        lines = br.lines().collect(Collectors.toList());

        Comparator<String> comparator = Comparator.comparing(str -> countOcc(str,c));

        String max = lines.stream()
                .max(comparator.thenComparing(Comparator.naturalOrder()))
                .orElse("");


        PrintWriter pw = new PrintWriter(out);
        pw.println(max);
        pw.flush();
    }
}

public class LineProcessorTest {
    public static void main(String[] args) {
        LineProcessor lineProcessor = new LineProcessor();

        lineProcessor.readLines(System.in, System.out, 'a');
    }

}

