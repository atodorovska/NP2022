package exams.midterm2.students;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Student {
    String id;
    String name;
    int firstMidterm;
    int secondMidterm;
    int labs;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public double summaryPoints() {
        return 0.45 * firstMidterm + 0.45 * secondMidterm + labs;
    }

    public int grade() {
        int g = (int) summaryPoints() / 10 + 1;
        if (g > 10) {
            g = 10;
        }
        if (g < 5) {
            g = 5;
        }
        return g;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d", this.id, this.name, this.firstMidterm, this.secondMidterm, this.labs, this.summaryPoints(), this.grade());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFirstMidterm() {
        return firstMidterm;
    }

    public int getSecondMidterm() {
        return secondMidterm;
    }

    public int getLabs() {
        return labs;
    }
}

interface UpdateFunction {
    void updatePoints(Student s, int points) throws Exception;
}

class AdvancedProgrammingCourse {
    Map<String, Student> studentMap = new HashMap<>();

    Map<String, UpdateFunction> updateFunctionMap = new HashMap<>();

    AdvancedProgrammingCourse() {
        updateFunctionMap.put("midterm1", (s, p) -> {
            if (p > 100) throw new Exception();
            s.firstMidterm = p;
        });

        updateFunctionMap.put("midterm2", (s, p) -> {
            if (p > 100) throw new Exception();
            s.secondMidterm = p;
        });

        updateFunctionMap.put("labs", (s, p) -> {
            if (p > 10) throw new Exception();
            s.labs = p;
        });
    }

    public void addStudent(Student s) {
        studentMap.put(s.id, s);
    }

    public void updateStudent(String idNumber, String activity, int points) {
        try {
            updateFunctionMap.get(activity).updatePoints(studentMap.get(idNumber), points);
        } catch (Exception e) {
            //DO NOTHING, just continue;
        }
    }

    public List<Student> getFirstNStudents(int n) {
        Comparator<Student> comparator = Comparator.comparing(Student::summaryPoints).thenComparing(Student::getId);
        return studentMap.values().stream()
                .sorted(comparator.reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public Map<Integer, Integer> getGradeDistribution() {
        Map<Integer, Integer> result = studentMap.values().stream()
                .map(Student::grade)
                .collect(Collectors.groupingBy(
                                grade -> grade,
                                TreeMap::new,
                                Collectors.summingInt(i -> 1)
                        )
                );

        IntStream.range(5, 11).forEach(i -> result.putIfAbsent(i, 0));
        return result;
    }

    public void printStatistics() {
        DoubleSummaryStatistics dss = studentMap.values().stream()
                .filter(s -> s.summaryPoints() >= 50.0)
                .mapToDouble(Student::summaryPoints)
                .summaryStatistics();

        System.out.printf("Count: %d Min: %.2f Average: %.2f Max: %.2f", dss.getCount(), dss.getMin(), dss.getAverage(), dss.getMax());
    }
}

public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}

