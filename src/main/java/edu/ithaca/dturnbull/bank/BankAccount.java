package edu.ithaca.dturnbull.bank;

public class BankAccount {

    private String email;
    private double balance;

    public BankAccount(String email, double startingBalance) {
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException(
                "Email address: " + email + " is invalid, cannot create account");
        }

        if (!isAmountValid(startingBalance)) {
            throw new IllegalArgumentException("Starting balance is invalid");
        }

        this.email = email;
        this.balance = startingBalance;
    }

    public double getBalance() {
        return balance;
    }

    public String getEmail() {
        return email;
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (!isAmountValid(amount)) {
            throw new IllegalArgumentException("Invalid withdrawal amount");
        }

        if (amount > balance) {
            throw new InsufficientFundsException("Not enough money");
        }

        balance -= amount;
    }

    public void deposit(double amount) {
        if (!isAmountValid(amount)) {
            throw new IllegalArgumentException("Invalid deposit amount");
        }

        balance += amount;
    }

    public void transfer(BankAccount other, double amount)
            throws InsufficientFundsException {

        if (!isAmountValid(amount)) {
            throw new IllegalArgumentException("Invalid transfer amount");
        }

        if (amount > balance) {
            throw new InsufficientFundsException("Not enough money");
        }

        this.balance -= amount;
        other.balance += amount;
    }

    public static boolean isEmailValid(String email) {
        if (email == null || email.length() == 0) {
            return false;
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            return false;
        }

        int dotIndex = email.indexOf('.', atIndex);
        if (dotIndex == -1 || dotIndex == email.length() - 1) {
            return false;
        }

        return true;
    }

    public static boolean isAmountValid(double amount) {
        if (amount < 0) {
            return false;
        }

        double rounded = Math.round(amount * 100) / 100.0;
        return rounded == amount;
    }
}
