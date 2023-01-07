package exams.midterm1.term1.risk;

//package mk.ukim.finki.midterm;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Dices {
    List<Integer> dices;

    Dices(String input) {
        dices = Arrays.stream(input.split("\\s+"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private static List<Integer> getSortedDesc(List<Integer> list) {
        return list.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }

    boolean wins(Dices other) {
        List<Integer> dicesSorted = getSortedDesc(this.dices);
        List<Integer> otherDicesSorted = getSortedDesc(other.dices);

        return IntStream.range(0, otherDicesSorted.size())
                .allMatch(i -> dicesSorted.get(i) > otherDicesSorted.get(i));
    }
}

class Attack {
    Dices attacker;
    Dices attacked;

    public Attack(String line) {
        String[] parts = line.split(";");
        attacker = new Dices(parts[0]);
        attacked = new Dices(parts[1]);
    }

    public boolean win() {
        return attacker.wins(attacked);
    }
}

class Risk {

    List<Attack> attacks;

    public int processAttacksData(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        attacks = br.lines().map(Attack::new).collect(Collectors.toList());

        return (int) attacks.stream()
                .filter(Attack::win)
                .count();
    }
}


public class RiskTester {
    public static void main(String[] args) {

        Risk risk = new Risk();

        System.out.println(risk.processAttacksData(System.in));

    }
}
