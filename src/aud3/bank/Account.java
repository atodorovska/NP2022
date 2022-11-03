package aud3.bank;

abstract public class Account {
    String name;
    long id;
    static long ID_COUNTER = 1000;
    double balance;

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
        id = ID_COUNTER++; //ke zeme 1000, pa ke se zgolemi na 1001
    }

    public Account(String name) {
        this.name = name;
        this.balance = 0;
        id = ID_COUNTER++; //ke zeme 1000, pa ke se zgolemi na 1001
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) throws InsufficientAmountException {
        if (amount <= balance) {
            balance -= amount;
        } else {
            throw new InsufficientAmountException(amount);
        }
    }

    abstract boolean canHaveInterest();
}
