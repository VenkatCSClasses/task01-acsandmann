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
