package edu.ithaca.dturnbull.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

	@Test
	void getBalanceTest() throws InsufficientFundsException {
		BankAccount bankAccount = new BankAccount("a@b.com", 200);

		assertEquals(200, bankAccount.getBalance(), 0.001);

		// positive starting balance
		BankAccount positive = new BankAccount("pos@b.com", 150.75);
		assertEquals(150.75, positive.getBalance(), 0.001);

		// zero starting balance
		BankAccount zero = new BankAccount("zero@b.com", 0);
		assertEquals(0, zero.getBalance(), 0.001);

		// negative starting balance should be invalid
		assertThrows(IllegalArgumentException.class, () -> new BankAccount("neg@b.com", -20.5));

		BankAccount bankAccount2 = new BankAccount("a@b.com", 100);

		bankAccount2.withdraw(30);
		assertEquals(70, bankAccount2.getBalance(), 0.001);

		assertThrows(InsufficientFundsException.class, () -> bankAccount2.withdraw(1000));
		assertEquals(70, bankAccount2.getBalance(), 0.001);

		bankAccount2.withdraw(70);
		assertEquals(0, bankAccount2.getBalance(), 0.001);
	}

	@Test
	void withdrawTest() throws InsufficientFundsException {
		BankAccount bankAccount = new BankAccount("a@b.com", 200);
		bankAccount.withdraw(100);

		assertEquals(100, bankAccount.getBalance(), 0.001);
		assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(300));

		assertThrows(IllegalArgumentException.class, () -> bankAccount.withdraw(-50));

		// invalid amounts: more than 2 decimal places should throw
		// IllegalArgumentException
		assertThrows(IllegalArgumentException.class, () -> bankAccount.withdraw(0.001)); // border: 3 decimal places
		assertThrows(IllegalArgumentException.class, () -> bankAccount.withdraw(1.23456)); // middle: many decimal
																							// places

		BankAccount bankAccount2 = new BankAccount("a@b.com", 200);
		// withdrawing zero should be a no-op
		bankAccount2.withdraw(0);

		assertEquals(200, bankAccount2.getBalance(), 0.001);
	}

	@Test
	void depositTest() {
		// Equivalence classes for deposit(amount):
		// 1) amount < 0 => throw IllegalArgumentException
		// 2) amount >= 0 and has <= 2 decimal places => deposit succeeds, balance
		// increases
		// 2a) border: amount == 0 (no-op)
		// 2b) border: exactly 2 decimal places
		// 3) amount >= 0 and has > 2 decimal places => throw IllegalArgumentException
		// 3a) border: just barely more than 2 decimal places (e.g., 0.001)
		// 3b) middle: multiple extra decimal places (e.g., 1.23456)

		BankAccount account = new BankAccount("a@b.com", 100.00);

		// Class 1: negative amounts (border and middle)
		assertThrows(IllegalArgumentException.class, () -> account.deposit(-0.01));
		assertThrows(IllegalArgumentException.class, () -> account.deposit(-10.50));

		// Class 2: valid deposits (border and middle)
		account.deposit(0.0); // border: zero deposit is a no-op
		assertEquals(100.00, account.getBalance(), 0.001);

		account.deposit(10.0); // middle: whole number
		assertEquals(110.00, account.getBalance(), 0.001);

		account.deposit(10.50); // border: exactly 2 decimal places
		assertEquals(120.50, account.getBalance(), 0.001);

		// Class 3: too many decimal places (border and middle)
		assertThrows(IllegalArgumentException.class, () -> account.deposit(0.001)); // border: 3 decimal places
		assertThrows(IllegalArgumentException.class, () -> account.deposit(1.23456)); // middle: many decimal places

		// ensure failed deposits do not change balance
		assertEquals(120.50, account.getBalance(), 0.001);
	}

	@Test
	void transferTest() throws InsufficientFundsException {
		// Equivalence classes for transfer(amount, destination):
		// 1) destination == null => throw IllegalArgumentException
		// 2) amount invalid (amount < 0 OR > 2 decimal places) => throw
		// IllegalArgumentException
		// 2a) border: amount == -0.01
		// 2b) border: amount == 0.001
		// 3) amount valid, destination non-null, amount > balance => throw
		// InsufficientFundsException
		// 3a) border: amount == balance + 0.01
		// 4) amount valid, destination non-null, amount <= balance => transfer succeeds
		// 4a) border: amount == 0.0 (no-op)
		// 4b) border: amount == balance (sender becomes 0)
		// 4c) middle: 2 decimal places normal transfer (e.g., 10.50)

		BankAccount from = new BankAccount("from@b.com", 100.00);
		BankAccount to = new BankAccount("to@b.com", 20.00);

		// Class 1: null destination
		assertThrows(IllegalArgumentException.class, () -> from.transfer(10.00, null));
		assertEquals(100.00, from.getBalance(), 0.001);
		assertEquals(20.00, to.getBalance(), 0.001);

		// Class 2: invalid amounts (negative and too many decimals)
		assertThrows(IllegalArgumentException.class, () -> from.transfer(-0.01, to)); // border: negative
		assertThrows(IllegalArgumentException.class, () -> from.transfer(0.001, to)); // border: 3 decimals
		assertThrows(IllegalArgumentException.class, () -> from.transfer(1.23456, to)); // middle: many decimals
		assertEquals(100.00, from.getBalance(), 0.001);
		assertEquals(20.00, to.getBalance(), 0.001);

		// Class 3: insufficient funds (border and middle)
		assertThrows(InsufficientFundsException.class, () -> from.transfer(100.01, to)); // border: just over balance
		assertThrows(InsufficientFundsException.class, () -> from.transfer(1000.00, to)); // middle: way over
		assertEquals(100.00, from.getBalance(), 0.001);
		assertEquals(20.00, to.getBalance(), 0.001);

		// Class 4: successful transfers
		from.transfer(0.0, to); // border: no-op
		assertEquals(100.00, from.getBalance(), 0.001);
		assertEquals(20.00, to.getBalance(), 0.001);

		from.transfer(10.50, to); // middle: typical transfer
		assertEquals(89.50, from.getBalance(), 0.001);
		assertEquals(30.50, to.getBalance(), 0.001);

		from.transfer(89.50, to); // border: transfer entire remaining balance
		assertEquals(0.00, from.getBalance(), 0.001);
		assertEquals(120.00, to.getBalance(), 0.001);
	}

	@Test
	void isAmountValidTest() {
		// Equivalence classes for isAmountValid(amount):
		// 1) amount < 0 => invalid
		// 2) amount >= 0 and has <= 2 decimal places => valid
		// 2a) border: amount == 0
		// 2b) border: exactly 2 decimal places
		// 3) amount >= 0 and has > 2 decimal places => invalid
		// 3a) border: just barely more than 2 decimal places (e.g., 1.001)
		// 3b) middle: multiple extra decimal places (e.g., 1.23456)

		// Class 1: negative amounts (border and middle)
		assertFalse(BankAccount.isAmountValid(-0.01));
		assertFalse(BankAccount.isAmountValid(-10.50));

		// Class 2: non-negative with <= 2 decimal places (border and middle)
		assertTrue(BankAccount.isAmountValid(0.0)); // border: zero
		assertTrue(BankAccount.isAmountValid(10.0)); // middle: whole number
		assertTrue(BankAccount.isAmountValid(10.5)); // border-ish: one decimal place
		assertTrue(BankAccount.isAmountValid(10.50)); // border: two decimal places

		// Class 3: non-negative with > 2 decimal places (border and middle)
		assertFalse(BankAccount.isAmountValid(0.001)); // border: 3 decimal places
		assertFalse(BankAccount.isAmountValid(1.23456)); // middle: many decimal places
	}

	@Test
	void isEmailValidTest() {
		assertTrue(BankAccount.isEmailValid("a@b.com")); // valid email address
		assertFalse(BankAccount.isEmailValid("")); // empty string

		// prefix
		assertFalse(BankAccount.isEmailValid("abc-@mail.com"));
		assertFalse(BankAccount.isEmailValid("abc..def@mail.com"));
		assertFalse(BankAccount.isEmailValid(".abc@mail.com"));
		assertFalse(BankAccount.isEmailValid("abc#def@mail.com"));

		assertTrue(BankAccount.isEmailValid("abc.def@mail.com"));

		// domain
		assertFalse(BankAccount.isEmailValid("abc.def@mail.c"));
		assertFalse(BankAccount.isEmailValid("abc.def@mail#archive.com"));
		assertFalse(BankAccount.isEmailValid("abc.def@mail"));
		assertFalse(BankAccount.isEmailValid("abc.def@mail..com"));

		assertTrue(BankAccount.isEmailValid("abc.def@mail.org"));
	}

	@Test
	void constructorTest() {
		BankAccount bankAccount = new BankAccount("a@b.com", 200);

		assertEquals("a@b.com", bankAccount.getEmail());
		assertEquals(200, bankAccount.getBalance(), 0.001);
		// check for exception thrown correctly
		assertThrows(IllegalArgumentException.class, () -> new BankAccount("", 100));

		// invalid amounts for normal bank accounts should throw
		// IllegalArgumentException
		assertThrows(IllegalArgumentException.class, () -> new BankAccount("a@b.com", -0.01)); // negative (border)
		assertThrows(IllegalArgumentException.class, () -> new BankAccount("a@b.com", -10.50)); // negative (middle)

		assertThrows(IllegalArgumentException.class, () -> new BankAccount("a@b.com", 0.001)); // > 2 decimals (border)
		assertThrows(IllegalArgumentException.class, () -> new BankAccount("a@b.com", 1.23456)); // > 2 decimals
																									// (middle)
	}

}
