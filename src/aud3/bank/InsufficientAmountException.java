package aud3.bank;

public class InsufficientAmountException extends Exception{
    InsufficientAmountException(double amount) { //You don't have X % on your account
        super(String.format("You don't have %.2f$ on your account", amount));
    }
}
