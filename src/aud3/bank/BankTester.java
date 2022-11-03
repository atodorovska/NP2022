package aud3.bank;

public class BankTester {
    public static void main(String[] args) {
        Bank bank = new Bank("FINKI");
        System.out.println(bank);

        Account a1 = new InterestCheckingAccount("Stefan", 1000);
        Account a2 = new PlatinumCheckingAccount("Leticija", 10000);
        Account a3 = new NonInterestCheckingAccount("Jovan", 500);

        bank.addAccount(a1);
        bank.addAccount(a2);
        bank.addAccount(a3);

        System.out.println(bank);

        bank.addInterest();
        System.out.println();
        System.out.println(bank);

        for (int i=0;i<bank.n;i++){
            try {
                bank.accounts[i].withdraw(1000);
            } catch (InsufficientAmountException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println(bank);
    }
}
