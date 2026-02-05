package edu.ithaca.dturnbull.bank;

public class BankAccount {

	private String email;
	private double balance;

	/**
	 * @param amount amount to validate
	 * @return true iff amount >= 0 and has at most 2 decimal places
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
	 * @throws IllegalArgumentException if email or startingBalance is invalid
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
	 * @param amount amount to withdraw; must be valid
	 * @throws IllegalArgumentException   if amount is invalid
	 * @throws InsufficientFundsException if amount > balance
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
	 * @param amount amount to deposit; must be valid
	 * @throws IllegalArgumentException if amount is invalid
	 */
	public void deposit(double amount) {
		if (!isAmountValid(amount)) {
			throw new IllegalArgumentException("Amount: " + amount + " is invalid, cannot deposit");
		}
		balance += amount;
	}

	/**
	 * @param amount      the amount to transfer; must be valid
	 * @param destination destination account; must not be null
	 * @throws IllegalArgumentException   if amount is invalid or destination is
	 *                                    null
	 * @throws InsufficientFundsException if amount > balance
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
		if (email == null || email.isEmpty())
			return false;

		// @ location
		int at = email.indexOf('@');
		if (at <= 0 || at != email.lastIndexOf('@') || at == email.length() - 1)
			return false;

		String local = email.substring(0, at), domain = email.substring(at + 1);

		// enforce no start/end or double periods and that every char is a
		// letter/digit/period
		if (local.startsWith(".") || local.endsWith(".") || local.contains(".."))
			return false;
		if (!local.chars().allMatch(c -> Character.isLetterOrDigit(c) || c == '.'))
			return false;

		// ensure there is a domain, it has only one period but not at end or start
		if (domain.isEmpty() || !domain.contains(".") || domain.startsWith(".") || domain.endsWith(".")
				|| domain.contains(".."))
			return false;

		// enforce every char is letter/digit/period
		if (!domain.chars().allMatch(c -> Character.isLetterOrDigit(c) || c == '.'))
			return false;

		// enforce tld(.com type) is at least 2 characters
		String tld = domain.substring(domain.lastIndexOf('.') + 1);
		return tld.length() >= 2;
	}
}
