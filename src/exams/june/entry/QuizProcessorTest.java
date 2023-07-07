package exams.june.entry;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

class QuizAttempt {
    String studentId;
    List<String> correct;
    List<String> answers;

    public QuizAttempt(String studentId, List<String> correct, List<String> answers) {
        this.studentId = studentId;
        this.correct = correct;
        this.answers = answers;
    }

    public static QuizAttempt createAttempt(String line) throws Exception {
        String[] parts = line.split(";");
        String studentId = parts[0];
        List<String> correct = Arrays.stream(parts[1].split(", ")).collect(Collectors.toList());
        List<String> answers = Arrays.stream(parts[2].split(", ")).collect(Collectors.toList());
        if (correct.size() != answers.size()) {
            throw new Exception();
        }

        return new QuizAttempt(studentId, correct, answers);
    }

    public double totalPoints() {
        double totalPoints = 0;
        for (int i = 0; i < correct.size(); i++) {
            if (correct.get(i).equals(answers.get(i))) {
                totalPoints += 1.0;
            } else {
                totalPoints -= 0.25;
            }
        }
        return totalPoints;
    }

    public String getStudentId() {
        return studentId;
    }
}

class QuizProcessor {
    public static Map<String, Double> processAnswers(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        List<QuizAttempt> attempts = br.lines().map(q -> {
                    try {
                        return QuizAttempt.createAttempt(q);
                    } catch (Exception e) {
                        System.out.println("A quiz must have same number of correct and selected answers");
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());

        return attempts.stream().collect(Collectors.toMap(QuizAttempt::getStudentId, QuizAttempt::totalPoints, Double::sum, TreeMap::new));
    }
}

public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}
