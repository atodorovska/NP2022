package aud3.bank;

import java.util.Arrays;

public class Bank {
    String name;
    Account[] accounts;
    int capacity;
    int n;

    public Bank(String name) {
        this.name = name;
        capacity = 10;
        n = 0;
        accounts = new Account[capacity];
    }

    public void addAccount(Account account) {
        if (n == capacity) {
            accounts = Arrays.copyOf(accounts, 2 * capacity);
            capacity *= 2;
        }
        accounts[n] = account;
        n++;
    }

    public double totalAssets() {
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum+=accounts[i].getBalance();
        }
        return sum;
    }

    public void addInterest() {
        for (int i=0;i<n;i++) {
            if (accounts[i].canHaveInterest()) {
                InterestBearingAccount iba = (InterestBearingAccount) accounts[i];
                iba.addInterest();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Bank name: %s\n", this.name));
        for (int i=0;i<n;i++){
            sb.append(accounts[i].toString()).append("\n");
        }
        return sb.toString();
    }
}
