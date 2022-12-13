package aud7;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class BenfordLaw {
    String filePath;
    int[] counterArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int totalNumbers = 0;

    public BenfordLaw(String filePath) {
        this.filePath = filePath;
    }

    public void process() throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.filePath)));
        List<Integer> firstDigit = br.lines()
                .map(line -> String.valueOf(line.charAt(0)))
                .map(Integer::parseInt)
                .collect(Collectors.toList()); //1 5 1 2 1 2 4 4

        firstDigit.forEach(digit -> counterArray[digit]++);
        totalNumbers = firstDigit.size();
    }

    public void printResults() {
        //1 -> 0.30
        //2 -> 0.25
        //3 -> 0.20
        IntStream.range(1, 10).forEach(i -> System.out.printf("%d -> %.2f\n", i, (float) counterArray[i]/totalNumbers));

    }
}

public class BenfordLawTest {
    public static void main(String[] args) {
        BenfordLaw benfordLaw = new BenfordLaw("/Users/stefan.a/Documents/GitWorkspace/NP_2022_cet10/src/mk/ukim/finki/av7/data/librarybook.txt");
        try {
            benfordLaw.process();
            benfordLaw.printResults();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }
}
