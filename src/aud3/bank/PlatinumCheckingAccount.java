package aud3.bank;

public class PlatinumCheckingAccount extends InterestCheckingAccount{
    public PlatinumCheckingAccount(String name, double balance) {
        super(name, balance);
    }

    public PlatinumCheckingAccount(String name) {
        super(name);
    }

    @Override
    public void addInterest() {
        this.balance*=(1+INTEREST_RATE*2);
    }

    @Override
    public String toString() {
        return "PlatinumCheckingAccount{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", balance=" + balance +
                '}';
    }
}
