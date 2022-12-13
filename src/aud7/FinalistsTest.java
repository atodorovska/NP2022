package aud7;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

class Finalist {
    int numberOfParticipants;
    int numberOfPrizes;
    static Random RANDOM = new Random();

    public Finalist(int numberOfParticipants, int numberOfPrizes) {
        this.numberOfParticipants = numberOfParticipants;
        this.numberOfPrizes = numberOfPrizes;
    }

    void pick() {
        List<Integer> picked = new ArrayList<>();

        for (int i = 0; i < numberOfPrizes; i++) {
            int selected = RANDOM.nextInt(numberOfParticipants)+1; //1-numberOfParticipants
            while (picked.contains(selected)){
                selected = RANDOM.nextInt(numberOfParticipants)+1;
            }
            picked.add(selected);
        }

//        picked.forEach(System.out::println);
        IntStream.range(0, picked.size()).forEach(i -> System.out.printf("%d. %d\n", i+1, picked.get(i)));

    }

}

public class FinalistsTest {
    public static void main(String[] args) {
        Finalist finalist = new Finalist(100,3);
        finalist.pick();
    }
}
