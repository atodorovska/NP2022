package aud3.bank;

public class InterestCheckingAccount extends Account implements InterestBearingAccount {

    static double INTEREST_RATE = 0.03; //3%

    public InterestCheckingAccount(String name, double balance) {
        super(name, balance);
    }

    public InterestCheckingAccount(String name) {
        super(name);
    }

    @Override
    boolean canHaveInterest() {
        return true;
    }

    @Override
    public void addInterest() {
        this.balance*=(1+INTEREST_RATE);
    }

    @Override
    public String toString() {
        return "InterestCheckingAccount{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", balance=" + balance +
                '}';
    }
}
