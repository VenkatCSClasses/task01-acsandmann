package edu.ithaca.dturnbull.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

	@Test
	void getBalanceTest() {
		BankAccount bankAccount = new BankAccount("a@b.com", 200);

		assertEquals(200, bankAccount.getBalance(), 0.001);

		// positive starting balance
		BankAccount positive = new BankAccount("pos@b.com", 150.75);
		assertEquals(150.75, positive.getBalance(), 0.001);

		// zero starting balance
		BankAccount zero = new BankAccount("zero@b.com", 0);
		assertEquals(0, zero.getBalance(), 0.001);

		// negative starting balance
		BankAccount negative = new BankAccount("neg@b.com", -20.5);
		assertEquals(-20.5, negative.getBalance(), 0.001);

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

		BankAccount bankAccount2 = new BankAccount("a@b.com", 200);
		// withdrawing zero should be a no-op
		bankAccount2.withdraw(0);

		assertEquals(200, bankAccount2.getBalance(), 0.001);
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
	}

}
