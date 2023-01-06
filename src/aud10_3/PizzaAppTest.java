package aud10_3;

import java.util.*;

class UserAlreadyExistsException extends Exception {
    UserAlreadyExistsException(String message) {
        super(message);
    }
}

class User {
    String email;
    String name;
    String phoneNumber;
    int id;
    static int ID_COUNTER = 1;

    public User(String email, String name, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        id = ID_COUNTER++;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getId() {
        return id;
    }
}

class PizzaApp {

    Map<String, User> userByEmail;
    Map<String, Float> revenueByPizza;
    // Pizza Name -> {Email -> Frequency}
    Map<String, Map<String, Integer>> frequenciesByUserAndPizza;

    PizzaApp() {
        userByEmail = new HashMap<>(); //O(1)
        revenueByPizza = new TreeMap<>();
        frequenciesByUserAndPizza = new TreeMap<>();
    }

    void registerUser(String email, String name, String phoneNumber) throws UserAlreadyExistsException {
        if (userByEmail.containsKey(email)) {
            throw new UserAlreadyExistsException(String.format("User with email %s already exists!", email));
        }

        User user = new User(email, name, phoneNumber);
        userByEmail.put(email, user);
    }

    void makeOrder(String email, String pizzaName, float pizzaPrice) {
        revenueByPizza.putIfAbsent(pizzaName, 0.0f);
//        revenueByPizza.put(pizzaName, revenueByPizza.get(pizzaName)+pizzaPrice);

        revenueByPizza.computeIfPresent(pizzaName, (k, v) -> {
            v += pizzaPrice;
            return v;
        });

        frequenciesByUserAndPizza.putIfAbsent(pizzaName, new HashMap<>());
        frequenciesByUserAndPizza.get(pizzaName).putIfAbsent(email, 0);
        frequenciesByUserAndPizza.get(pizzaName).computeIfPresent(email, (k,v) -> ++v);
    }

    public void printRevenueByPizza() {

        revenueByPizza.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(String.format("%s %.2f", entry.getKey(), entry.getValue())));
//        revenueByPizza.forEach((k,v)-> System.out.println(String.format("%s %.2f", k, v)));
    }

    public void printMostFrequentUserForPizza() {
        for (Map.Entry<String, Map<String, Integer>> entry : frequenciesByUserAndPizza.entrySet()) {
            System.out.println(String.format("Pizza: %s", entry.getKey()));
            Map<String, Integer> frequencyByEmail = entry.getValue();

            int maxFrequency = frequencyByEmail.values().stream().mapToInt(i -> i).max().getAsInt();

            System.out.println("ID email frequency");
            frequencyByEmail.keySet().stream()
                    .filter(email -> frequencyByEmail.get(email)==maxFrequency)
                    .map(email -> userByEmail.get(email))
                    .sorted(Comparator.comparing(User::getId))
                    .forEach(user -> System.out.printf("%d %s %d%n", user.getId(), user.getEmail(), maxFrequency));

        }
    }
}

public class PizzaAppTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        PizzaApp pizzaApp = new PizzaApp();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String method = parts[0];

            if (method.equalsIgnoreCase("registerUser")) { //email, name, phone
                String email = parts[1];
                String name = parts[2];
                String phone = parts[3];
                try {
                    pizzaApp.registerUser(email, name, phone);
                } catch (UserAlreadyExistsException e) {
                    System.out.println(e.getMessage());
                }
            } else if (method.equalsIgnoreCase("makeOrder")) { //email, pizzaName, price
                String email = parts[1];
                String pizzaName = parts[2];
                float price = Float.parseFloat(parts[3]);
                pizzaApp.makeOrder(email, pizzaName, price);
            } else if (method.equalsIgnoreCase("printRevenueByPizza")) {
                System.out.println("Print revenue by pizza");
                pizzaApp.printRevenueByPizza();
            } else { //printMostFrequentUserForPizza
                System.out.println("Print most frequent user(s) by pizza");

                pizzaApp.printMostFrequentUserForPizza();
            }
        }
    }
}
