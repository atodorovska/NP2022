package aud6_1;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class InvalidIdException extends Exception {
    public InvalidIdException(String message) {
        super(message);
    }
}

class IdValidator {
    public static boolean isValid(String id, int length) {
        if (id.length() != length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(id.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}

abstract class Customer {
    String id;
    double minutes;
    int SMSs;
    double GBs;

    public Customer(String id, double minutes, int SMSs, double GBs) throws InvalidIdException {
        if (!IdValidator.isValid(id, 7)) {
            throw new InvalidIdException(String.format("%s is not a valid user ID", id));
        }
        this.id = id;
        this.minutes = minutes;
        this.SMSs = SMSs;
        this.GBs = GBs;
    }

    abstract double totalPrice();

    abstract double commission();
}

class SCustomer extends Customer {

    static double BASE_PRICE_S = 500.0;

    static double FREE_MINUTES_S = 100.0;
    static int FREE_SMS_S = 50;
    static double FREE_GB_INTERNET_S = 5.0;

    static double PRICE_PER_MINUTES = 5.0;
    static double PRICE_PER_SMS = 6.0;
    static double PRICE_PER_GB = 25.0;

    static double COMMISSION_RATE = 0.07;

    public SCustomer(String id, double minutes, int SMSs, double GBs) throws InvalidIdException {
        super(id, minutes, SMSs, GBs);
    }

    @Override
    double totalPrice() {
        double total = BASE_PRICE_S;
        total += PRICE_PER_MINUTES * Math.max(0, minutes - FREE_MINUTES_S);
        total += PRICE_PER_SMS * Math.max(0, SMSs - FREE_SMS_S);
        total += PRICE_PER_GB * Math.max(0, GBs - FREE_GB_INTERNET_S);
        return total;
    }

    @Override
    double commission() {
        return totalPrice() * COMMISSION_RATE;
    }
}

class MCustomer extends Customer {

    static double BASE_PRICE_M = 750.0;

    static double FREE_MINUTES_M = 150.0;
    static int FREE_SMS_M = 60;
    static double FREE_GB_INTERNET_M = 10.0;

    static double PRICE_PER_MINUTES = 4.0;
    static double PRICE_PER_SMS = 4.0;
    static double PRICE_PER_GB = 20.0;

    static double COMMISSION_RATE = 0.04;

    public MCustomer(String id, double minutes, int SMSs, double GBs) throws InvalidIdException {
        super(id, minutes, SMSs, GBs);
    }

    @Override
    double totalPrice() {
        double total = BASE_PRICE_M;
        total += PRICE_PER_MINUTES * Math.max(0, minutes - FREE_MINUTES_M);
        total += PRICE_PER_SMS * Math.max(0, SMSs - FREE_SMS_M);
        total += PRICE_PER_GB * Math.max(0, GBs - FREE_GB_INTERNET_M);
        return total;
    }

    @Override
    double commission() {
        return totalPrice() * COMMISSION_RATE;
    }
}

class SalesRep implements Comparable<SalesRep> {
    String id;
    List<Customer> customers;

    public SalesRep(String id, List<Customer> customers) {
        this.id = id;
        this.customers = customers;
    }

    public static SalesRep createSalesRep(String line) throws InvalidIdException {
        //0   1       2 3   4  5     6       7 8   9  10   11
        //475 4642771 M 248 90 14.94 2281930 S 139 48 6.19 4040003 M 189 100 11.90 5064198 M 159 78 9.32
        String[] parts = line.split("\\s+");
        String id = parts[0];
        if (!IdValidator.isValid(id, 3)) {
            throw new InvalidIdException(String.format("%s is not a valid sales rep ID", id));
        }
        List<Customer> customers = new ArrayList<>();
        for (int i = 1; i < parts.length; i += 5) {
            String customerId = parts[i];
            String type = parts[i + 1];
            double minutes = Double.parseDouble(parts[i + 2]);
            int sms = Integer.parseInt(parts[i + 3]);
            double gbs = Double.parseDouble(parts[i + 4]);
            try {
                if (type.equals("M")) {
                    customers.add(new MCustomer(customerId, minutes, sms, gbs));
                } else {
                    customers.add(new SCustomer(customerId, minutes, sms, gbs));
                }
            } catch (InvalidIdException e) {
                System.out.println(e.getMessage());
            }

        }
        return new SalesRep(id, customers);
    }

    private double totalCommission() {
        return customers.stream().mapToDouble(Customer::commission).sum();
    }

    @Override
    public String toString() {
        DoubleSummaryStatistics summaryStatistics = customers.stream()
                .mapToDouble(customer -> customer.totalPrice())
                .summaryStatistics();
        //ID number_of_bills min_bill average_bill max_bill total_commission
        return String.format("%s Count: %d Min: %.2f Average: %.2f Max: %.2f Commission: %.2f",
                id,
                summaryStatistics.getCount(),
                summaryStatistics.getMin(),
                summaryStatistics.getAverage(),
                summaryStatistics.getMax(),
                totalCommission()
        );
    }

    @Override
    public int compareTo(SalesRep o) {
        return Double.compare(this.totalCommission(), o.totalCommission());
    }
}

class MobileOperator {

    List<SalesRep> salesReps;

    void readSalesRepData(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        salesReps = br.lines()
                .map(line -> {
                    try {
                        return SalesRep.createSalesRep(line);
                    } catch (InvalidIdException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        br.close();
    }

    void printSalesReport(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);

        salesReps.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(salesRep -> pw.println(salesRep));

        pw.flush();

    }
}

public class MobileOperatorTest {
    public static void main(String[] args) {
        MobileOperator mobileOperator = new MobileOperator();
        System.out.println("---- READING OF THE SALES REPORTS ----");
        try {
            mobileOperator.readSalesRepData(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("---- PRINTING FINAL REPORTS FOR SALES REPRESENTATIVES ----");
        mobileOperator.printSalesReport(System.out);
    }
}