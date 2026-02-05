package edu.ithaca.dturnbull.bank;

public class BankAccount {

	private String email;
	private double balance;

	/**
	 * Checks whether a monetary amount is valid for normal bank account operations.
	 *
	 * An amount is valid if it is not negative and has at most two decimal places.
	 *
	 * @param amount the money amount to validate
	 * @return true if amount is not negative and has two decimal places or less;
	 *         false otherwise
	 */
	public static boolean isAmountValid(double amount) {
		if (amount < 0) {
			return false;
		}

		// check that there are no more than 2 significant decimal places
		double scaled = amount * 100.0;
		return Math.abs(scaled - Math.round(scaled)) < 0.000000001;
	}

	/**
	 * @throws IllegalArgumentException if email is invalid
	 */
	public BankAccount(String email, double startingBalance) {
		if (!isEmailValid(email)) {
			throw new IllegalArgumentException("Email address: " + email + " is invalid, cannot create account");
		}
		if (!isAmountValid(startingBalance)) {
			throw new IllegalArgumentException(
					"Starting balance: " + startingBalance + " is invalid, cannot create account");
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
		if (!isAmountValid(amount)) {
			throw new IllegalArgumentException("Amount: " + amount + " is invalid, cannot withdraw");
		}
		if (amount <= balance) {
			balance -= amount;
		} else {
			throw new InsufficientFundsException("Not enough money");
		}
	}

	/**
	 * Deposits the given amount into this account.
	 *
	 * @param amount the amount to deposit; must be a valid monetary amount (not
	 *               negative and with at most two decimal places)
	 * @throws IllegalArgumentException if amount is invalid
	 * @post increases the balance by amount if amount is valid
	 */
	public void deposit(double amount) {
		if (!isAmountValid(amount)) {
			throw new IllegalArgumentException("Amount: " + amount + " is invalid, cannot deposit");
		}
		balance += amount;
	}

	/**
	 * Transfers the given amount from this account to the provided destination
	 * account.
	 *
	 * @param amount      the amount to transfer; must be a valid monetary amount
	 *                    (not negative and with at most two decimal places)
	 * @param destination the account to receive the funds; must not be null
	 * @throws IllegalArgumentException   if amount is invalid or destination is
	 *                                    null
	 * @throws InsufficientFundsException if amount is greater than the current
	 *                                    balance
	 * @post decreases this balance by amount and increases destination balance by
	 *       amount if amount is valid and funds are sufficient
	 */
	public void transfer(double amount, BankAccount destination) throws InsufficientFundsException {
		if (destination == null) {
			throw new IllegalArgumentException("Destination account is null, cannot transfer");
		}
		if (!isAmountValid(amount)) {
			throw new IllegalArgumentException("Amount: " + amount + " is invalid, cannot transfer");
		}
		if (amount > balance) {
			throw new InsufficientFundsException("Not enough money");
		}
		this.withdraw(amount);
		destination.deposit(amount);
	}

	public static boolean isEmailValid(String email) {
		if (email.indexOf('@') == -1) {
			return false;
		} else {
			return true;
		}
	}
}
