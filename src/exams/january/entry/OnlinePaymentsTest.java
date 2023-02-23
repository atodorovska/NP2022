package exams.january.entry;

//package mk.ukim.finki.midterm;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Student {
    String id;
    List<Item> items;

    Student(String id) {
        this.id = id;
        items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public int totalNet() {
        return items.stream().mapToInt(i -> i.price).sum();
    }

    public int fee() {
        int total = totalNet();
        double fee = Math.round(total * 0.0114);
        if (fee > 300) {
            fee = 300;
        }
        if (fee < 3) {
            fee = 3;
        }
        return (int) fee;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Student: %s Net: %d Fee: %d Total: %d\n", id, totalNet(), fee(), totalNet() + fee()));
        sb.append("Items:\n");
        items.sort(Comparator.comparing(Item::getPrice).thenComparing(Item::getName).reversed());
//        sb.append(items.stream().map(item -> item.toString()).collect(Collectors.joining("\n")));
        List<String> outputs = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            outputs.add(String.format("%d. %s", i + 1, items.get(i).toString()));
        }
        sb.append(outputs.stream().collect(Collectors.joining("\n")));
        return sb.toString();
    }
}

class Item implements Comparable<Item> {
    String id;
    String name;
    int price;

    public Item(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Item create(String line) {
        String[] parts = line.split(";");
        return new Item(
                parts[0],
                parts[1],
                Integer.parseInt(parts[2])
        );
    }

    @Override
    public String toString() {
        return String.format("%s %d", name, price);
    }

    @Override
    public int compareTo(Item o) {
        int res = Integer.compare(this.price, o.price);
        if (res==0){
            return this.name.compareTo(o.name);
        } else {
            return 0;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}

class OnlinePayments {
    Map<String, Student> studentMap;

    OnlinePayments() {
        studentMap = new HashMap<>();
    }

    public void readItems(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        br.lines().map(Item::create).forEach(item -> {
            studentMap.putIfAbsent(item.id, new Student(item.id));
            studentMap.get(item.id).addItem(item);
        });
    }

    void printStudentReport(String index, OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        Student s = studentMap.get(index);

        if (s == null) {
            pw.println(String.format("Student %s not found!", index));
        } else {
            pw.println(s);
        }

        pw.flush();

    }


}

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}
