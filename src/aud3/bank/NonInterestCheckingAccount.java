package aud3.bank;

public class NonInterestCheckingAccount extends Account{
    public NonInterestCheckingAccount(String name, double balance) {
        super(name, balance);
    }

    public NonInterestCheckingAccount(String name) {
        super(name);
    }

    @Override
    boolean canHaveInterest() {
        return false;
    }

    @Override
    public String toString() {
        return "NonInterestCheckingAccount{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", balance=" + balance +
                '}';
    }
}
