package aud9;

import java.util.*;

class Student {
    String id;
    int grades;

    public Student(String id, int grades) {
        this.id = id;
        this.grades = grades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return grades == student.grades && id.equals(student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, grades);
    }
}

public class SetIntro {
    public static void main(String[] args) {
        //mnozestva - sets

        Set<String> set;

        //O(1) add,remove,search O(n) iteriranje
//        set = new HashSet<String>();
//        String s1 = "Stefan";
//        String s2 = "Stefan";
//
//        set.add(s1);
//        set.add(s2);
//        set.add("NP");
//        set.add("NApredno programiranje");
//
//        System.out.println(set);

        //TreeSet
        //O(logN) add,remove,search
        set = new TreeSet<>(String::compareToIgnoreCase);
        String s1 = "Stefan";
        String s2 = "stefan";

        set.add(s1);
        set.add(s2);
        set.add("NP");
        set.add("NApredno programiranje");

        System.out.println(set);

//        Set<Student> students = new TreeSet<>();

    }
}
