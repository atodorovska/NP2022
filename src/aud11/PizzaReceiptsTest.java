package aud11;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

interface IPizza {
    double price();

    default String print () {
        return String.format("Price: %.2f$%n", price());
    }
}

class WhiteSaucePizza implements IPizza {

    @Override
    public double price() {
        return 11.0;
    }
}

class RedSaucePizza implements IPizza {

    @Override
    public double price() {
        return 10.0;
    }
}

abstract class PizzaDecorator implements IPizza {
    IPizza base;

    public PizzaDecorator(IPizza base) {
        this.base = base;
    }
}

class PepperoniDecorator extends PizzaDecorator {

    public PepperoniDecorator(IPizza base) {
        super(base);
    }

    @Override
    public double price() {
        return base.price() + 1.5;
    }
}

class MushroomDecorator extends PizzaDecorator {

    public MushroomDecorator(IPizza pizza) {
        super(pizza);
    }

    @Override
    public double price() {
        return base.price() + 1.75;
    }
}

class MozzarellaDecorator extends PizzaDecorator {

    public MozzarellaDecorator(IPizza pizza) {
        super(pizza);
    }

    @Override
    public double price() {
        return base.price() + 2.65;
    }
}

class PizzaReceipts {

    Map<String, List<IPizza>> pizzasByUser;

    PizzaReceipts() {
        pizzasByUser = new HashMap<>();
    }

    public void readOrders(InputStream in) {
        Scanner sc = new Scanner(in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String name = parts[0];
            String pizzaInfo = parts[1];
            String[] pizzaComponents = pizzaInfo.split(";");
            String basePizza = pizzaComponents[0];

            IPizza pizza;

            if (basePizza.equals("whiteSauce")) {
                pizza = new WhiteSaucePizza();
            } else {
                pizza = new RedSaucePizza();
            }

            for (int i = 1; i < pizzaComponents.length; i++) {
                if (pizzaComponents[i].equals("pepperoni")) {
                    pizza = new PepperoniDecorator(pizza);
                } else if (pizzaComponents[i].equals("mushrooms")) {
                    pizza = new MushroomDecorator(pizza);
                } else { //mozzarella
                    pizza = new MozzarellaDecorator(pizza);
                }
            }

            pizzasByUser.putIfAbsent(name, new ArrayList<>());
            pizzasByUser.get(name).add(pizza);
        }
    }

    public void printRevenueByUser(OutputStream out) {

    }

    public void printUserOrders(OutputStream out, String user) {
        PrintWriter pw = new PrintWriter(out);
        pizzasByUser.getOrDefault(user, new ArrayList<>()).forEach(pizza -> pw.println(pizza.print()));

        pw.flush();



    }

    public Map<String, Integer> usersByPizzaIngredient(String pepperoni) {
        return new HashMap<>();
    }
}

public class PizzaReceiptsTest {

    public static void printMap(Map<String, Integer> map) {
        map.forEach((k, v) -> System.out.println(String.format("%s -> %d", k, v)));
    }

    public static void main(String[] args) {
        PizzaReceipts pizzaReceipts = new PizzaReceipts();

        System.out.println("READING DATA FROM INPUT STREAM");
        pizzaReceipts.readOrders(System.in);

        System.out.println("TESTING printRevenueByUser");
        pizzaReceipts.printRevenueByUser(System.out);

        System.out.println("TESTING printUserOrders");
        System.out.println("Stefan");
        pizzaReceipts.printUserOrders(System.out, "Stefan");
        System.out.println("Ana");
        pizzaReceipts.printUserOrders(System.out, "Ana");
        System.out.println("Gjorgji");
        pizzaReceipts.printUserOrders(System.out, "Gjorgji");

        System.out.println("TESTING usersByPizzaIngredient");
        Map<String, Integer> pepperoniMap = pizzaReceipts.usersByPizzaIngredient("pepperoni");
        Map<String, Integer> mushroomsMap = pizzaReceipts.usersByPizzaIngredient("mushrooms");
        Map<String, Integer> mozzarellaMap = pizzaReceipts.usersByPizzaIngredient("mozzarella");

        System.out.println("PEPPERONI");
        printMap(pepperoniMap);
        System.out.println("MUSHROOMS");
        printMap(mushroomsMap);
        System.out.println("MOZZARELLA");
        printMap(mozzarellaMap);

    }
}
