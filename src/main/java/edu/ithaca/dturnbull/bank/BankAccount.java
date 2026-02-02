package edu.ithaca.dturnbull.bank;

public class BankAccount {

	private String email;
	private double balance;

	/**
	 * @throws IllegalArgumentException if email is invalid
	 */
	public BankAccount(String email, double startingBalance) {
		if (isEmailValid(email)) {
			this.email = email;
			this.balance = startingBalance;
		} else {
			throw new IllegalArgumentException("Email address: " + email + " is invalid, cannot create account");
		}
	}

	public double getBalance() {
		return balance;
	}

	public String getEmail() {
		return email;
	}

	/**
	 * Withdraws the given amount from the account.
	 *
	 * @param amount the amount to withdraw; must be non-negative
	 * @throws IllegalArgumentException   if amount is negative
	 * @throws InsufficientFundsException if amount is greater than the current
	 *                                    balance
	 * @post reduces the balance by amount if amount is non-negative and less than
	 *       or equal to balance
	 */
	public void withdraw(double amount) throws InsufficientFundsException {
		if (amount <= balance) {
			balance -= amount;
		} else {
			throw new InsufficientFundsException("Not enough money");
		}
	}

	public static boolean isEmailValid(String email) {
		if (email.indexOf('@') == -1) {
			return false;
		} else {
			return true;
		}
	}
}
