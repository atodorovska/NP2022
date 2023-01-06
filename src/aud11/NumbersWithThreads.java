package aud11;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//class NumberThread extends Thread {
//
//    int number;
//
//    public NumberThread(int number) {
//        this.number = number;
//    }
//
//    @Override
//    public void run() {
//        System.out.println(number);
//    }
//}

public class NumbersWithThreads {
    public static void main(String[] args) {

        //kreiranje na nitkite
        List<Thread> threads = IntStream.range(1, 101)
                .mapToObj(i -> new Thread(() -> System.out.println(i)))
                .collect(Collectors.toList());

        //startuvanje na nitkite. Sekogas so .start(), a ne so .run()
        threads.forEach(Thread::start);

        //chekanje na nitkite da zavrshat
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });


    }
}
