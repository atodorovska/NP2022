package exams.midterm1.term1.rules;

//package mk.ukim.finki.midterm;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Rule<IN, OUT> {
    Predicate<IN> condition;
    Function<IN, OUT> mapper;

    public Rule(Predicate<IN> condition, Function<IN, OUT> mapper) {
        this.condition = condition;
        this.mapper = mapper;
    }

    public Optional<OUT> apply(IN in) {
        if (condition.test(in)) {
            return Optional.of(mapper.apply(in));
        } else {
            return Optional.empty();
        }
    }
}

class RuleProcessor {
    public static <IN, OUT> void process(List<IN> inputs, List<Rule<IN, OUT>> rules) {
        inputs.forEach(input -> {
            System.out.println(String.format("Input: %s", input));
            rules.forEach(rule -> {
                Optional<OUT> result = rule.apply(input);
                result.ifPresent(r -> System.out.println(String.format("Result: %s", r)));
                if (result.isEmpty()) {
                    System.out.println("Condition not met");
                }
            });
        });
    }
}

class Student {
    String id;
    List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public static Student create(String line) {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Integer> grades = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
        return new Student(id, grades);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", grades=" + grades +
                '}';
    }
}

public class RuleTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { //Test for String,Integer
            List<Rule<String, Integer>> rules = new ArrayList<>();

            /*
            TODO: Add a rule where if the string contains the string "NP", the result would be index of the first occurrence of the string "NP"
            * */
            rules.add(new Rule<>(
                    str -> str.contains("NP"),
                    str -> str.indexOf("NP")
            ));

            /*
            TODO: Add a rule where if the string starts with the string "NP", the result would be length of the string
            * */
            rules.add(new Rule<>(
                    str -> str.startsWith("NP"),
                    str -> str.length()
            ));

            List<String> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(sc.nextLine());
            }

            RuleProcessor.process(inputs, rules);


        } else { //Test for Student, Double
            List<Rule<Student, Double>> rules = new ArrayList<>();

            //TODO Add a rule where if the student has at least 3 grades, the result would be the max grade of the student
            rules.add(new Rule<>(
                    student -> student.grades.size() > 2,
                    student -> (double) student.grades.stream().mapToInt(i -> i).max().getAsInt()
            ));

            //TODO Add a rule where if the student has an ID that starts with 21, the result would be the average grade of the student
            rules.add(new Rule<>(
                    student -> student.id.startsWith("20"),
                    student -> student.grades.stream().mapToInt(i -> i).average().orElse(5.0)
            ));

            List<Student> students = new ArrayList<>();
            while (sc.hasNext()){
                students.add(Student.create(sc.nextLine()));
            }

            RuleProcessor.process(students, rules);
        }
    }
}

