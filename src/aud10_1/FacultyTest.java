package aud10_1;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;


class StudentOnCourse {
    String courseId;
    String studentId;
    int totalPoints;

    public StudentOnCourse(String courseId, String studentId, int totalPoints) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.totalPoints = totalPoints;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public String getCourseId() {
        return courseId;
    }

    int getGrade() {
        int grade = totalPoints / 10 + 1;
        if (grade < 5) {
            grade = 5;
        }
        if (grade > 10) {
            grade = 10;
        }
        return grade;
    }

    @Override
    public String toString() {
        //201566 85 (9)
        return String.format("%s %d (%d)", studentId, totalPoints, getGrade());
    }

    public String reportWithCourse() {
        return String.format("%s %d (%d)", courseId, totalPoints, getGrade());
    }
}

class Faculty {

    Map<String, List<StudentOnCourse>> studentsByCourse = new HashMap<>();
    Map<String, List<StudentOnCourse>> coursesByStudent = new HashMap<>();

    void addInfo(String courseId, String studentId, int totalPoints) {
        StudentOnCourse studentOnCourse = new StudentOnCourse(courseId, studentId, totalPoints);

        studentsByCourse.putIfAbsent(courseId, new ArrayList<>());
        studentsByCourse.get(courseId).add(studentOnCourse);

        coursesByStudent.putIfAbsent(studentId, new ArrayList<>());
        coursesByStudent.get(studentId).add(studentOnCourse);
    }

    void printCourseReport(String courseId, String comparatorStr, boolean descending) {
        List<StudentOnCourse> students = studentsByCourse.get(courseId);

        Comparator<StudentOnCourse> comparator;

        if (comparatorStr.equalsIgnoreCase("byid")) {
            comparator = Comparator.comparing(StudentOnCourse::getStudentId);
        } else {
            comparator = Comparator.comparing(StudentOnCourse::getGrade)
                    .thenComparing(StudentOnCourse::getTotalPoints)
                    .thenComparing(StudentOnCourse::getStudentId);
        }

        if (descending) {
            comparator = comparator.reversed();
        }

        students.stream()
                .sorted(comparator)
                .forEach(System.out::println);
    }

    public void printStudentReport(String studentId) {
        Comparator<StudentOnCourse> comparator = Comparator.comparing(StudentOnCourse::getCourseId);

        coursesByStudent.get(studentId)
                .stream()
                .sorted(comparator)
                .forEach(s -> System.out.println(s.reportWithCourse()));
    }

    public Map<Integer, Integer> gradeDistribution(String courseId) {
//        List<StudentOnCourse> studentOnCourses = studentsByCourse.get(courseId);
//        Map<Integer, Integer> countingMap = new TreeMap<>();
//
//        for (StudentOnCourse studentOnCourse : studentOnCourses) {
//            int grade = studentOnCourse.getGrade();
//            countingMap.putIfAbsent(grade,0);
//            countingMap.computeIfPresent(grade, (key, value) -> ++value);
//        }
//
//        return countingMap;

        return studentsByCourse.get(courseId)
                .stream()
//                .map(StudentOnCourse::getGrade)
                .collect(Collectors.groupingBy(
                        StudentOnCourse::getGrade,
                        HashMap::new,
                        Collectors.summingInt(grade -> 1)
                ));


    }
}

public class FacultyTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s++");
            if (parts[0].equals("addInfo")) {
                String courseId = parts[1];
                String studentId = parts[2];
                int totalPoints = Integer.parseInt(parts[3]);
                faculty.addInfo(courseId, studentId, totalPoints);
            } else if (parts[0].equals("printCourseReport")) {
                String courseId = parts[1];
                String comparator = parts[2];
                boolean descending = Boolean.parseBoolean(parts[3]);
                faculty.printCourseReport(courseId, comparator, descending);
            } else if (parts[0].equals("printStudentReport")) { //printStudentReport
                String studentId = parts[1];
                faculty.printStudentReport(studentId);
            } else {
                String courseId = parts[1];
                Map<Integer, Integer> grades = faculty.gradeDistribution(courseId);
                grades.forEach((key, value) -> System.out.println(String.format("%2d -> %3d", key, value)));
            }
        }
    }
}
