package edu.ithaca.dturnbull.bank;

public class BankAccount {

    private String email;
    private double balance;

    /**
     * @throws IllegalArgumentException if email is invalid
     */
    public BankAccount(String email, double startingBalance){
        if (isEmailValid(email)){
            this.email = email;
            this.balance = startingBalance;
        }
        else {
            throw new IllegalArgumentException("Email address: " + email + " is invalid, cannot create account");
        }
    }

    public double getBalance(){
        return balance;
    }

    public String getEmail(){
        return email;
    }

    /**
     * @post reduces the balance by amount if amount is non-negative and smaller than balance
     */
    public void withdraw (double amount) throws InsufficientFundsException{
        if (amount <= balance){
            balance -= amount;
        }
        else {
            throw new InsufficientFundsException("Not enough money");
        }
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
    /**
 * Determines whether a monetary amount is valid for a normal bank account.
 *
 * An amount is considered valid if:
 * - it is non-negative (greater than or equal to 0)
 * - it has no more than two digits after the decimal point
 *
 * @param amount the monetary amount to validate
 * @return true if the amount is valid, false otherwise
 */
public static boolean isAmountValid(double amount) {
    if (amount < 0) {
        return false;
    }

    double rounded = Math.round(amount * 100) / 100.0;
    if (rounded != amount) {
        return false;
    }

    return true;
}

}
