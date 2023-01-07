package exams.midterm1.term2.products;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.stream.Collectors;

class InvalidOperationException extends Exception {
    InvalidOperationException(String message) {
        super(message);
    }
}

class Product {

    private int id;
    private String name;
    private double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%d %s %.2f", id, name, price);
    }
}

abstract class Item implements Comparable<Item> {

    Product product;
    double itemPrice;

    public Item(int productId, String productName, double productPrice) {
        this.product = new Product(productId, productName, productPrice);
    }

    public static Item createItem(String itemData) throws InvalidOperationException {
        String[] parts = itemData.split(";");
        String type = parts[0];
        int productId = Integer.parseInt(parts[1]);
        String productName = parts[2];
        double productPrice = Double.parseDouble(parts[3]);

        if (type.equals("WS")) {
            int quantity = Integer.parseInt(parts[4]);
            if (quantity == 0)
                throw new InvalidOperationException("The quantity of the product with id " + productId + " can not be 0.");
            return new WSItem(productId, productName, productPrice, quantity);
        } else {
            double quantity = Double.parseDouble(parts[4]);
            if (quantity == 0)
                throw new InvalidOperationException("The quantity of the product with id " + productId + " can not be 0.");
            return new PSItem(productId, productName, productPrice, quantity);
        }
    }

    public abstract double calculateItemPrice();

    @Override
    public int compareTo(Item other) {
        return Double.compare(this.itemPrice, other.itemPrice);
    }
}

class PSItem extends Item{

    private double quantity;

    public PSItem(int productId, String productName, double productPrice, double quantity) {
        super(productId, productName, productPrice);
        this.quantity = quantity;

        itemPrice = calculateItemPrice();
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f", product.getId(), calculateItemPrice());
    }

    @Override
    public double calculateItemPrice() {
        return (product.getPrice() / 1000.0) * quantity;
    }
}

class WSItem extends Item{

    private int quantity;

    public WSItem(int productId, String productName, double productPrice, int quantity) {
        super(productId, productName, productPrice);
        this.quantity = quantity;

        itemPrice = calculateItemPrice();
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f", product.getId(), calculateItemPrice());
    }

    @Override
    public double calculateItemPrice() {
        return product.getPrice() * quantity;
    }
}

class ShoppingCart {

    private List<Item> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public void addItem(String itemData) throws InvalidOperationException {
        items.add(Item.createItem(itemData));
    }

    public void printShoppingCart(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        items.stream().sorted(Comparator.reverseOrder()).forEach(pw::println);
        pw.flush();
    }

    public void blackFridayOffer(List<Integer> discountItems, OutputStream os) throws InvalidOperationException {
        if (discountItems.isEmpty())
            throw new InvalidOperationException("There are no products with discount.");

        PrintWriter pw = new PrintWriter(os);
        List<Item> discountedItems = items.stream()
                .filter(i -> discountItems.contains(i.product.getId()))
                .collect(Collectors.toList());

        discountedItems.forEach(i -> {
            i.product.setPrice(i.product.getPrice() * 0.9);
            double oldItemPrice = i.itemPrice;
            double newItemPrice = i.calculateItemPrice();
            System.out.printf("%s - %.2f\n", i.product.getId(), oldItemPrice - newItemPrice);
        });
        pw.flush();
    }
}

public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}
