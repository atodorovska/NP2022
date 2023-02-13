package office_hours;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * January 2016 Exam problem 1
 */
public class StudentRecordsTest {
    public static void main(String[] args) throws IOException {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}


class Student {
    String code;
    String program;
    List<Integer> grades;

    public static Student create(String line) {
        //sneft1 KNI 9 10 8 8 10 9 6 7 7 6 9 9 7 7 10 9 6 10 10 8 6 7 9 7 9 7 6
        String[] parts = line.split("\\s+");
        String id = parts[0];
        String program = parts[1];
        List<Integer> grades = Arrays.stream(parts).skip(2).map(Integer::parseInt).collect(Collectors.toList());

        return new Student(id, program, grades);
    }

    public Student(String code, String direction, List<Integer> grades) {
        this.code = code;
        this.program = direction;
        this.grades = grades;
    }

    public String getCode() {
        return code;
    }

    public String getProgram() {
        return program;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public double getAvgGrade() { //TODO DoubleSummaryStatistics
        return grades.stream().mapToInt(i -> i).average().getAsDouble(); //Stream<Integer> -> IntStream
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", code, getAvgGrade());
    }
}

class Program {
    String name;
    TreeSet<Student> students = new TreeSet<>(Comparator.comparing(Student::getAvgGrade).reversed().thenComparing(Student::getCode));
    List<Integer> allGrades = new ArrayList<>();

    public Program(String name) {
        this.name = name;
    }

    public void addStudent(Student s) {
        students.add(s);
        allGrades.addAll(s.grades);
    }

    private String stars(long n) {
        int s = (int) Math.ceil(n / 10.0);
        return IntStream.range(0, s)
                .mapToObj(i -> "*")
                .collect(Collectors.joining(""));
    }

    public long getAs (){
        return allGrades.stream().filter(i -> i==10).count();
    }

    public String distribution() {
        Map<Integer, Long> gradeDistribution = allGrades.stream().collect(Collectors.groupingBy(
                i -> i,
                TreeMap::new,
                Collectors.counting()
        ));

        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");

        gradeDistribution.forEach((grade,count) -> {
            sb.append(String.format("%2d | %s(%d)", grade, stars(count), count)).append("\n");
        });

        return sb.toString();
    }

}

class StudentRecords {


    Map<String, Program> programs;

    public StudentRecords() {

        programs = new TreeMap<>();
    }

    int readRecords(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


        List<Student> students = bufferedReader.lines().map(Student::create).collect(Collectors.toList());

        students.forEach(student -> {
            programs.putIfAbsent(student.program, new Program(student.program));
            programs.get(student.program).addStudent(student);
        });

        return students.size();
    }

    void writeTable(OutputStream outputStream) { //TODO: vrakja lista od studenti, a sakam samo student
        PrintWriter pw = new PrintWriter(outputStream);

        programs.forEach((programName, program) -> {
            pw.println(programName);
            program.students.forEach(pw::println);
        });

        pw.flush();
    }

    void writeDistribution(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);

        programs.values().stream().sorted(Comparator.comparing(Program::getAs)).forEach(program -> pw.println(program.distribution()));

        pw.flush();

    }






}
