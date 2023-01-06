package aud9;

import java.util.*;

public class MapIntro {
    public static void main(String[] args) {
        Map<String, Integer> map;

        //HashMap
        //komplesnost od O(1) za dodavanje, brishenje, prebaruvanje
        //komplesnost od O(n) za iteriranje na mapata
        //redosled e proizvolen (spored hashCode())
        //map = new HashMap<>();

        //LinkedHashMap
        //se zadrzuva redosledot na vnesuvanje na elementi
//        map = new LinkedHashMap<>();

        //TreeMap
        //redosledot e sortiran spored Comparable na klucot K
        map = new TreeMap<>(String::compareToIgnoreCase);

        map.put("NP1", 1);
        map.put("NP2", 2);
        map.put("AA", 2);
        map.put("NP3", 3);

        System.out.println(map);

        map.put("np1",8);
        System.out.println(map);

        //get
        System.out.println(map.get("Np1"));
        System.out.println(map.get("Np10")); //opasnost za null pointer exception

        //containsKey
        if (map.containsKey("NP10")) {
            System.out.println("NP10 is in the map");
            System.out.println(map.get("NP10"));
        } else {
            System.out.println("NP10 is not in the map");
        }

        //keySet
        System.out.println(map.keySet());

        //values
        System.out.println(map.values());
    }
}
