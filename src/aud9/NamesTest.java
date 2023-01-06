package aud9;

import java.io.*;
import java.util.*;

public class NamesTest {
    private static Map<String, Integer> readNames(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        Map<String, Integer> result = new HashMap<>();
        br.lines().forEach(
                line -> {
                    String[] parts = line.split("\\s+");
                    String name = parts[0];
                    int age = Integer.parseInt(parts[1]);
                    result.put(name, age);
                }
        );
        return result;
    }

    public static void main(String[] args) {
        try {
            Map<String, Integer> boyNames = readNames(new FileInputStream("/Users/stefan.a/Documents/GitWorkspace/NP_2022_cet10/src/mk/ukim/finki/aud9/data/boynames.txt"));
            Map<String, Integer> girlNames = readNames(new FileInputStream("/Users/stefan.a/Documents/GitWorkspace/NP_2022_cet10/src/mk/ukim/finki/aud9/data/girlnames.txt"));

            Set<String> uniqueNames = new HashSet<>();

//            boyNames.keySet().stream().filter(name -> girlNames.containsKey(name)).forEach(name -> uniqueNames.add(name));
//            girlNames.keySet().stream().filter(name -> boyNames.containsKey(name)).forEach(name -> uniqueNames.add(name));

            uniqueNames.addAll(boyNames.keySet());
            uniqueNames.addAll(girlNames.keySet());


            TreeMap<String, Integer> unisexNamesMap = new TreeMap<>(Comparator.reverseOrder());
            uniqueNames.stream()
                    .filter(name -> boyNames.containsKey(name) && girlNames.containsKey(name))
                    .forEach(name -> unisexNamesMap.put(name, boyNames.get(name) + girlNames.get(name)));

            //HUGE ERROR DON'T DO THIS!
//            TreeMap<Integer, String> namesByFrequency = new TreeMap<>(Comparator.reverseOrder());
//            unisexNamesMap.forEach((k, v) -> namesByFrequency.put(v, k));
//            namesByFrequency.forEach((k, v) -> System.out.println(String.format("%s -> %d", v, k)));

            //correct way to sort map by VALUE
            unisexNamesMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(entry -> System.out.println(String.format("%s -> %d", entry.getKey(), entry.getValue())));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
