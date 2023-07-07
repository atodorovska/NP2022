package exams.june.delivery;

import java.util.*;

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

class DeliveryPerson {
    String id;
    String name;
    Location location;

    int totalDeliveries;
    List<Integer> moneyEarned;

    public DeliveryPerson(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        totalDeliveries = 0;
        moneyEarned = new ArrayList<>();
    }

    public int compareDistanceToRestaurant(DeliveryPerson other, Location restaurantLocation) {
        int thisDistance = location.distance(restaurantLocation);
        int otherDistance = other.location.distance(restaurantLocation);
        if (thisDistance == otherDistance) {
            return Integer.compare(this.totalDeliveries, other.totalDeliveries);
        } else {
            return thisDistance - otherDistance;
        }
    }

    public void processDelivery(int distance, Location location) {
        this.location = location;
        this.totalDeliveries++;
        this.moneyEarned.add(90 + 10 * (distance / 10));
    }

    double totalEarned() {
        return moneyEarned.stream().mapToInt(i -> i).sum();
    }

    @Override
    public String toString() {
        DoubleSummaryStatistics iss = moneyEarned.stream().mapToDouble(i -> i).summaryStatistics();
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f", this.id, this.name, iss.getCount(), iss.getSum(), iss.getAverage());
    }

    public String getId() {
        return id;
    }
}

class Restaurant {
    String id;
    String name;
    Location location;

    List<Float> moneyEarned;

    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        moneyEarned = new ArrayList<>();
    }

    public void processDelivery(float cost) {
        moneyEarned.add(cost);
    }

    public double getAverageEarned() {
        return moneyEarned.stream().mapToDouble(i -> i).average().orElse(0.0);
    }

    @Override
    public String toString() {
        DoubleSummaryStatistics iss = moneyEarned.stream().mapToDouble(i -> i).summaryStatistics();
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f", this.id, this.name, iss.getCount(), iss.getSum(), iss.getAverage());
    }

    public String getId() {
        return id;
    }
}

class Address {
    String name;
    Location location;

    public Address(String name, Location location) {
        this.name = name;
        this.location = location;
    }
}

class User {
    String id;
    String name;

    Map<String, Address> addresses;

    List<Float> moneySpent;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        addresses = new HashMap<>();
        moneySpent = new ArrayList<>();
    }


    public void processDelivery(float cost) {
        moneySpent.add(cost);
    }

    public double totalSpent() {
        return moneySpent.stream().mapToDouble(i -> i).sum();
    }

    @Override
    public String toString() {
        DoubleSummaryStatistics dss = moneySpent.stream().mapToDouble(i -> i).summaryStatistics();
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f", this.id, this.name, dss.getCount(), dss.getSum(), dss.getAverage());
    }

    public String getId() {
        return id;
    }
}

class DeliveryApp {
    Map<String, User> users = new HashMap<>(); // lookup map
    Map<String, DeliveryPerson> deliveryPersonMap = new HashMap<>();
    Map<String, Restaurant> restaurantMap = new HashMap<>();

    String name;

    DeliveryApp(String name) {
        this.name = name;
    }

    void registerDeliveryPerson(String id, String name, Location currentLocation) {
        deliveryPersonMap.put(id, new DeliveryPerson(id, name, currentLocation));
    }

    void addRestaurant(String id, String name, Location location) {
        restaurantMap.put(id, new Restaurant(id, name, location));
    }

    void addUser(String id, String name) {
        users.put(id, new User(id, name));
    }

    void addAddress(String id, String addressName, Location location) {
        users.get(id).addresses.put(addressName, new Address(addressName, location));
    }

    void orderFood(String userId, String userAddressName, String restaurantId, float cost) {
        User user = users.get(userId);
        Address address = user.addresses.get(userAddressName);
        Restaurant restaurant = restaurantMap.get(restaurantId);

        DeliveryPerson deliveryPerson = deliveryPersonMap.values().stream().min((l, r) -> l.compareDistanceToRestaurant(r, restaurant.location)).get();

        int distance = deliveryPerson.location.distance(restaurant.location);

        deliveryPerson.processDelivery(distance, address.location);
        user.processDelivery(cost);
        restaurant.processDelivery(cost);
    }

    void printUsers() {
        users.values().stream()
                .sorted(Comparator.comparing(User::totalSpent).thenComparing(User::getId).reversed())
                .forEach(System.out::println);
    }

    void printRestaurants() {
        restaurantMap.values().stream()
                .sorted(Comparator.comparing(Restaurant::getAverageEarned).thenComparing(Restaurant::getId).reversed())
                .forEach(System.out::println);
    }

    void printDeliveryPeople() {
        deliveryPersonMap.values().stream()
                .sorted(Comparator.comparing(DeliveryPerson::totalEarned).thenComparing(DeliveryPerson::getId).reversed())
                .forEach(System.out::println);
    }

}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}
