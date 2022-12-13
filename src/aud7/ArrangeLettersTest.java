package aud7;

import java.util.Arrays;
import java.util.stream.Collectors;

class ArrangeLetters {

    public static String arrangeWord (String word) {
        return word.chars()
                .sorted()
                .mapToObj(ascii -> String.valueOf((char) ascii))
                .collect(Collectors.joining(""));
    }

    public static String arrange (String sentence) {
        return Arrays.stream(sentence.split("\\s+"))
                .map(ArrangeLetters::arrangeWord)
                .sorted().collect(Collectors.joining(" "));
    }
}

public class ArrangeLettersTest {
    public static void main(String[] args) {
        System.out.println(ArrangeLetters.arrange("kO pSk sO"));
    }
}
