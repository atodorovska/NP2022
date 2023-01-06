package aud11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Searcher extends Thread {
    int start;
    int end;

    int min;
    int max;

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public Searcher(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "Searcher{" +
                "start=" + start +
                ", end=" + end +
                ", min=" + min +
                ", max=" + max +
                '}';
    }

    @Override
    public void run() {
        min = max = ArrayParallelization.ARRAY[start];
        for (int i = start + 1; i < end; i++) {
            if (ArrayParallelization.ARRAY[i] < min) {
                min = ArrayParallelization.ARRAY[i];
            }
            if (ArrayParallelization.ARRAY[i] > max) {
                max = ArrayParallelization.ARRAY[i];
            }
        }
    }
}

public class ArrayParallelization {

    public static int ARRAY_SIZE = 10000000;
    public static int THREADS_COUNT = 100;
    public static int[] ARRAY = new int[ARRAY_SIZE];

    public static Random RANDOM = new Random();

    public static void main(String[] args) {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            ARRAY[i] = RANDOM.nextInt(51); //[0,100]
        }

        ARRAY[150000]=-5; //global min
        ARRAY[451111]=102; //global max


        List<Searcher> searchers = new ArrayList<>();
        for (int i = 0; i < THREADS_COUNT; i++) {
            int start = i * ARRAY_SIZE / THREADS_COUNT;
            int end = start + ARRAY_SIZE / THREADS_COUNT;
            searchers.add(new Searcher(start, end));
        }

        searchers.forEach(Searcher::start);

        searchers.forEach(s -> {
            try {
                s.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });

//        searchers.forEach(System.out::println);

        int globalMin = searchers.stream().mapToInt(Searcher::getMin).min().getAsInt();
        int globalMax = searchers.stream().mapToInt(Searcher::getMax).max().getAsInt();

        System.out.println(globalMin);
        System.out.println(globalMax);

    }
}
