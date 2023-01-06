package aud10_2;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}


class Product {
    String id;
    String name;
    LocalDateTime createdAt;
    double price;
    int quantitySold;

    public Product(String id, String name, LocalDateTime createdAt, double price) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
    }

    public double buy(int quantity) {
        quantitySold += quantity;
        return quantity * price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + quantitySold +
                '}';
    }
}


class OnlineShop {

    Map<String, List<Product>> productsByCategory;
    Map<String, Product> lookupProducts;

    OnlineShop() {
        productsByCategory = new HashMap<>();
        lookupProducts = new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price) {
        Product product = new Product(id, name, createdAt, price);

        productsByCategory.putIfAbsent(category, new ArrayList<>());
        productsByCategory.get(category).add(product);

        lookupProducts.put(id, product);
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException {
        if (!lookupProducts.containsKey(id)) {
            throw new ProductNotFoundException(String.format("Product with id %s does not exist in the online shop!", id));
        }

        return lookupProducts.get(id).buy(quantity);
    }

    private Comparator<Product> createComparator(COMPARATOR_TYPE comparatorType) {
        Comparator<Product> byDate = Comparator.comparing(Product::getCreatedAt);
        Comparator<Product> byQuantity = Comparator.comparing(Product::getQuantitySold);
        Comparator<Product> byPrice = Comparator.comparing(Product::getPrice);

        if (comparatorType.equals(COMPARATOR_TYPE.NEWEST_FIRST)) {
            return byDate.reversed();
        } else if (comparatorType.equals(COMPARATOR_TYPE.OLDEST_FIRST)) {
            return byDate;
        } else if (comparatorType.equals(COMPARATOR_TYPE.MOST_SOLD_FIRST)) {
            return byQuantity.reversed();
        } else if (comparatorType.equals(COMPARATOR_TYPE.LEAST_SOLD_FIRST)) {
            return byQuantity;
        } else if (comparatorType.equals(COMPARATOR_TYPE.LOWEST_PRICE_FIRST)) {
            return byPrice;
        } else {
            return byPrice.reversed();
        }
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        //1. da gi zememe produktite sto ni trebaat
        List<Product> relevantProducts;
        if (category == null) {
            relevantProducts = new ArrayList<>(lookupProducts.values());
        } else {
            relevantProducts = productsByCategory.get(category);
        }

        //2. Sortiranje na vistinski nacin
        relevantProducts = relevantProducts.stream()
                .sorted(createComparator(comparatorType))
                .collect(Collectors.toList());

        //3. Paginacija
        int pages = (int) Math.ceil((double) relevantProducts.size() / pageSize);

        List<Integer> starts = IntStream.range(0, pages)
                .map(i -> i * pageSize)
                .boxed()
                .collect(Collectors.toList());

        List<List<Product>> result = new ArrayList<>();

        for (int start : starts) {
            //20, 22 -> 20
            //25, 22 -> 22
            int end = Math.min((start + pageSize), relevantProducts.size());
            result.add(relevantProducts.subList(start, end));
        }


        return result;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category = null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

