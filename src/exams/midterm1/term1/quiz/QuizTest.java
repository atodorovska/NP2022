package exams.midterm1.term1.quiz;

//package mk.ukim.finki.midterm;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

class InvalidOperationException extends Exception {
    InvalidOperationException (String message) {
        super(message);
    }
}

abstract class Question implements Comparable<Question> {
    String question;
    int points;

    public Question(String question, int points) {
        this.question = question;
        this.points = points;
    }

    static Question createQuestion(String questionData) throws InvalidOperationException {
        String[] parts = questionData.split(";");
        String type = parts[0];
        String question = parts[1];
        int points = Integer.parseInt(parts[2]);
        if (type.equals("TF")) {
            boolean trueOrFalse = Boolean.parseBoolean(parts[3]);
            return new TrueFalseQuestion(question, points, trueOrFalse);
        } else {
            String correctChoice = parts[3];
            return new MultipleChoiceQuestion(question, points, correctChoice);
        }
    }

    abstract double answer(String answer);

    @Override
    public int compareTo(Question o) {
        return Integer.compare(this.points, o.points);
    }
}

class TrueFalseQuestion extends Question {
    boolean trueOrFalse;

    public TrueFalseQuestion(String question, int points, boolean trueOrFalse) {
        super(question, points);
        this.trueOrFalse = trueOrFalse;
    }

    @Override
    double answer(String answer) {
        return Boolean.parseBoolean(answer) == this.trueOrFalse ? points : 0.0;
    }

    @Override
    public String toString() {
        return String.format("True/False Question: %s Points: %d Answer: %s", question, points, trueOrFalse);
    }
}

class MultipleChoiceQuestion extends Question {
    String correctChoice;

    public MultipleChoiceQuestion(String question, int points, String correctChoice) throws InvalidOperationException {
        super(question, points);
        if (correctChoice.charAt(0)<'A' || correctChoice.charAt(0)>'E')
            throw new InvalidOperationException(String.format("%s is not allowed option for this question", correctChoice));
        this.correctChoice = correctChoice;
    }

    @Override
    double answer(String answer) {
        if (answer.equals(correctChoice)) {
            return points;
        } else {
            return points * -0.2;
        }
    }

    @Override
    public String toString() {
        return String.format("Multiple Choice Question: %s Points %d Answer: %s",
                question,
                points,
                correctChoice
        );
    }
}

class Quiz {
    List<Question> questions;

    Quiz() {
        questions = new ArrayList<>();
    }

    public void addQuestion(String questionData) throws InvalidOperationException {
        questions.add(Question.createQuestion(questionData));
    }

    public void printQuiz(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        questions.stream().sorted(Comparator.reverseOrder()).forEach(pw::println);
        pw.flush();
    }

    public void answerQuiz(List<String> answers, OutputStream os) throws InvalidOperationException {
        if (answers.size()!=questions.size()){
            throw new InvalidOperationException("Answers and questions must be of same length!");
        }
        PrintWriter pw = new PrintWriter(os);
        double totalPoints = IntStream.range(0, answers.size()).mapToDouble(i -> {
            double gainedPoints = questions.get(i).answer(answers.get(i));
            pw.println(String.format("%d. %.2f", i+1, gainedPoints));
            return gainedPoints;
        }).sum();
        pw.println(String.format("Total points: %.2f", totalPoints));
        pw.flush();
    }
}

public class QuizTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i=0;i<questions;i++) {
            try {
                quiz.addQuestion(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<String> answers = new ArrayList<>();

        int answersCount =  Integer.parseInt(sc.nextLine());

        for (int i=0;i<answersCount;i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase==1) {
            quiz.printQuiz(System.out);
        } else if (testCase==2) {
            try {
                quiz.answerQuiz(answers, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }

//        Quiz quiz = new Quiz();
//
//        quiz.addQuestion("TF;Question1;1;true");
//        quiz.addQuestion("TF;Question2;1;false");
//        quiz.addQuestion("MC;Question3;3;A");
//        quiz.addQuestion("MC;Question4;5;A");
//        quiz.addQuestion("MC;Question4;2;A");
//
//        quiz.printQuiz(System.out);
//
//        quiz.answerQuiz(List.of("true", "true", "A", "A", "D"), System.out);
    }
}
