package aud8;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Eratosthenes {

    private boolean isPrime(int number) {

        return IntStream.range(2, number)
                .noneMatch(i -> number % i == 0);
//        for (int i=2;i<number;i++){
//            if (number%i==0){
//                return false;
//            }
//        }
//
//        return true;
    }

    void process(List<Integer> numbers) {
        for (int i = 0; i < numbers.size(); i++) {
            if (isPrime(numbers.get(i))) {
                System.out.println(numbers.get(i));
                for (int j = i + 1; j < numbers.size(); j++) {
                    if (numbers.get(j)%numbers.get(i)==0){
                        numbers.remove(j);
                        --j;
                    }
                }
            }
        }
    }
}

public class EratosthenesTest {
    public static void main(String[] args) {
        List<Integer> numbers = IntStream.range(2, 1000).boxed().collect(Collectors.toList());

        Eratosthenes eratosthenes = new Eratosthenes();
        eratosthenes.process(numbers);

        System.out.println(numbers);
    }
}
