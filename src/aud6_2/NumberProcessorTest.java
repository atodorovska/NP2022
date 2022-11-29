package aud6_2;

import java.util.*;
import java.util.stream.Collectors;

interface NumberProcessor<IN extends Number, R> {
    R compute(ArrayList<IN> numbers);
}

class Numbers<N extends Number> {
    ArrayList<N> numbers;

    public Numbers(ArrayList<N> numbers) {
        this.numbers = numbers;
    }

    void process(NumberProcessor<N, ?> processor) {
        System.out.println(processor.compute(numbers));
    }
}

public class NumberProcessorTest {

    public static void main(String[] args) {

        ArrayList<Integer> integerArrayList = new ArrayList<>();
        ArrayList<Double> doubleArrayList = new ArrayList<>();

        int countOfIntegers;
        Scanner sc = new Scanner(System.in);
        countOfIntegers = sc.nextInt();
        while (countOfIntegers > 0) {
            integerArrayList.add(sc.nextInt());
            --countOfIntegers;
        }

        int countOfDoubles;
        countOfDoubles = sc.nextInt();
        while (countOfDoubles > 0) {
            doubleArrayList.add(sc.nextDouble());
            --countOfDoubles;
        }

        Numbers<Integer> integerNumbers = new Numbers<>(integerArrayList);

        NumberProcessor<Integer, Long> firstProcessor = numbers -> numbers.stream()
                .filter(n -> n < 0)
                .count();

        System.out.println("RESULTS FROM THE FIRST NUMBER PROCESSOR");
        integerNumbers.process(firstProcessor);


        NumberProcessor<Integer, String> secondProcessor = numbers -> {
            DoubleSummaryStatistics sumStats = numbers.stream().mapToDouble(i -> i).summaryStatistics();
            return String.format("Count: %d Min: %.2f Average: %.2f Max: %.2f",
                    sumStats.getCount(),
                    sumStats.getMin(),
                    sumStats.getAverage(),
                    sumStats.getMax()
            );
        };
        System.out.println("RESULTS FROM THE SECOND NUMBER PROCESSOR");
        integerNumbers.process(secondProcessor);

        Numbers<Double> doubleNumbers = new Numbers<>(doubleArrayList);

        //TODO third processor
        NumberProcessor<Double, List<Double>> thirdProcessor = numbers -> numbers.stream().sorted().collect(Collectors.toList());

        System.out.println("RESULTS FROM THE THIRD NUMBER PROCESSOR");
        doubleNumbers.process(thirdProcessor);

        //TODO fourth processor
        NumberProcessor<Double, Double> fourthProcessor = numbers -> {
            List<Double> sorted = thirdProcessor.compute(numbers);
            int size = sorted.size();
            if (size % 2 == 0) {
                return (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2.0;
            } else {
                return sorted.get(size / 2);
            }
        };
        System.out.println("RESULTS FROM THE FOURTH NUMBER PROCESSOR");
        doubleNumbers.process(fourthProcessor);

    }

}
