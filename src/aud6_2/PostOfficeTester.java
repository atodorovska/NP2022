package aud6_2;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class InvalidPackageException extends Exception {

    public InvalidPackageException() {
    }

    public InvalidPackageException(String message) {
        super(message);
    }
}


interface IPackage extends Comparable<IPackage> {
    double price();
    int weight();

    @Override
    default int compareTo(IPackage o) {
        return Double.compare(this.price(), o.price());
    }
}
abstract class Package implements IPackage {
    String name;
    String address;
    int trackingNumber;
    int weight;

    public Package(String name, String address, int trackingNumber, int weight) {
        this.name = name;
        this.address = address;
        this.trackingNumber = trackingNumber;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %d, %d", name, address, trackingNumber, weight);
    }

    @Override
    public int weight() {
        return weight;
    }
}

class PackageFactory {
    public static IPackage createPackage(String line) throws InvalidPackageException {
        //I John_Doe Main_St_123 111 30 America
        //L Delia_Mesa Quaking_Lake 621 2 false
        String[] parts = line.split("\\s+");
        String type = parts[0];
        String name = parts[1];
        String address = parts[2];
        int trackingNumber = Integer.parseInt(parts[3]);
        int weight = Integer.parseInt(parts[4]);
        if (weight <= 0) {
            throw new InvalidPackageException(line);
        }

        if (type.equals("I")) {
            String region = parts[5];
            return new InternationalPackage(name, address, trackingNumber, weight, region);
        } else if (type.equals("L")) { //local package
            boolean priority = Boolean.parseBoolean(parts[5]);
            return new LocalPackage(name, address, trackingNumber, weight, priority);
        } else {
            throw new InvalidPackageException(line);
        }
    }
}

class InternationalPackage extends Package {
    String region;
    final static double PRICE_PER_GRAM = 1.5;

    public InternationalPackage(String name, String address, int trackingNumber, int weight, String region) {
        super(name, address, trackingNumber, weight);
        this.region = region;
    }

    @Override
    public double price() {
        return weight * PRICE_PER_GRAM;
    }

    @Override
    public String toString() {
        return String.format("I, %s, %s", super.toString(), region);
    }
}

class LocalPackage extends Package {
    boolean priority;
    final static double PRICE_WITH_PRIORITY = 5.0;
    final static double REGULAR_PRICE = 3.0;

    public LocalPackage(String name, String address, int trackingNumber, int weight, boolean priority) {
        super(name, address, trackingNumber, weight);
        this.priority = priority;
    }

    @Override
    public double price() {
//        return priority ? PRICE_WITH_PRIORITY : REGULAR_PRICE;
        if (priority) {
            return PRICE_WITH_PRIORITY;
        } else {
            return REGULAR_PRICE;
        }
    }

    @Override
    public String toString() {
        return String.format("L, %s, %s", super.toString(), priority);
    }
}

class GroupPackage implements IPackage {

    List<Package> packages; //od bilo koj tip I, L, G

    public GroupPackage() {
        packages = new ArrayList<>();
    }

    void addPackage (Package p){
        packages.add(p);
    }

    @Override
    public double price() {
        return packages.stream()
                .mapToDouble(IPackage::price)
                .sum();
    }

    @Override
    public int weight() {
        return packages.stream()
                .mapToInt(Package::weight)
                .sum();
    }

    //TODO toString

}

class PostOffice {
    String name;
    String city;
    List<IPackage> packages;

    public PostOffice(String name, String city) {
        this.name = name;
        this.city = city;
        packages = new ArrayList<>();
    }

    public void loadPackages(Scanner sc) throws InvalidPackageException {
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            packages.add(PackageFactory.createPackage(line));
        }
    }

    public boolean addPackage(Package p) {
        return packages.add(p);
    }

    public IPackage mostExpensive() {

//        Package max = packages.get(0);
//        for (Package p : packages) {
//            if (p.compareTo(max)>0){
//                max = p;
//            }
//        }
//        return max;
        return packages.stream()
                .max(Comparator.naturalOrder())
                .get();
    }

    public void printPackages(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);
        packages.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(p -> pw.println(p));
        pw.flush();
    }
}

public class PostOfficeTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PostOffice office = new PostOffice("Poshta", "Skopje");
        try {
            office.loadPackages(sc);
            System.out.println("======Packages======");
            office.printPackages(System.out);
            System.out.println();
            System.out.println("======MostExpensive======");
            System.out.println(office.mostExpensive());

        } catch (Exception e) {
            System.out.println("======TestingException======");
            System.out.println("Invalid value:");
            System.out.println(e.getMessage());
        }
        sc.close();
    }
}

