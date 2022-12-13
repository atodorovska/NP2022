package aud8;

import java.util.Collection;
import java.util.stream.Stream;

public class CountOccurrenceTest {

    public static int count(Collection<Collection<String>> collections, String str) {
        int counter = 0;
        for (Collection<String> collection : collections) {
            for (String element : collection) {
                if (element.equals(str)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static int count2(Collection<Collection<String>> collections, String str) {
        return (int) collections.stream()
                .flatMap(Collection::stream)
                .filter(element -> element.equals(str))
                .count();
    }

    public static void main(String[] args) {

    }
}
