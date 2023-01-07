package exams.midterm1.term2.risk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Comparator;
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

    int wins(Dices other) {
        List<Integer> dicesSorted = getSortedDesc(this.dices);
        List<Integer> otherDicesSorted = getSortedDesc(other.dices);

        return (int) IntStream.range(0, otherDicesSorted.size())
                .filter(i -> dicesSorted.get(i) > otherDicesSorted.get(i))
                .count();
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

    public int wins() {
        return attacker.wins(attacked);
    }
}

class Risk {
    List<Attack> attacks;

    public void processAttacksData(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        attacks = br.lines().map(Attack::new).collect(Collectors.toList());

        attacks.stream()
                .map(Attack::wins)
                .forEach(i -> System.out.println(i + " " + (3 - i)));
    }
}

public class RiskTester {

    public static void main(String[] args) {
        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}