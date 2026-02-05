package edu.ithaca.dturnbull.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void getBalanceTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        assertEquals(200, bankAccount.getBalance(), 0.001);
    }

    @Test
    void withdrawTest() throws InsufficientFundsException {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        // Valid withdrawal
        bankAccount.withdraw(100);
        assertEquals(100, bankAccount.getBalance(), 0.001);

        // Equivalence class: insufficient funds
        assertThrows(InsufficientFundsException.class,
                () -> bankAccount.withdraw(300));

        // Equivalence class: invalid amount — negative
        assertThrows(IllegalArgumentException.class,
                () -> bankAccount.withdraw(-10.00));

        // Equivalence class: invalid amount — too many decimals
        assertThrows(IllegalArgumentException.class,
                () -> bankAccount.withdraw(10.999));
    }

    @Test
    void isEmailValidTest() {

        // Equivalence class: VALID email
        assertTrue(BankAccount.isEmailValid("a@b.com"));

        // Equivalence class: INVALID — empty string (border case)
        assertFalse(BankAccount.isEmailValid(""));

        // Equivalence class: INVALID — missing '@'
        assertFalse(BankAccount.isEmailValid("ab.com"));

        // Equivalence class: INVALID — missing local part (border case)
        assertFalse(BankAccount.isEmailValid("@ab.com"));

        // Equivalence class: INVALID — missing '.' after '@'
        assertFalse(BankAccount.isEmailValid("a@bcom"));
    }

    @Test
    void isAmountValidTest() {

        // Middle case
        assertTrue(BankAccount.isAmountValid(10.50));

        // Border cases
        assertTrue(BankAccount.isAmountValid(0.00));
        assertTrue(BankAccount.isAmountValid(25.0));
        assertTrue(BankAccount.isAmountValid(3.5));


        assertFalse(BankAccount.isAmountValid(-10.25));
        assertFalse(BankAccount.isAmountValid(-0.01));

        assertFalse(BankAccount.isAmountValid(1.234));
        assertFalse(BankAccount.isAmountValid(0.001));
        assertFalse(BankAccount.isAmountValid(12.34567));
    }

    @Test
    void constructorTest() {

        // Valid construction
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        assertEquals("a@b.com", bankAccount.getEmail());
        assertEquals(200, bankAccount.getBalance(), 0.001);

        // Invalid email
        assertThrows(IllegalArgumentException.class,
                () -> new BankAccount("", 100));

        // Invalid starting balance — negative
        assertThrows(IllegalArgumentException.class,
                () -> new BankAccount("a@b.com", -10.00));

        // Invalid starting balance — too many decimals
        assertThrows(IllegalArgumentException.class,
                () -> new BankAccount("a@b.com", 100.001));
    }

    @Test
    void depositTest() {
        BankAccount account = new BankAccount("a@b.com", 100);

        // Middle case
        account.deposit(50.00);
        assertEquals(150.00, account.getBalance(), 0.001);

        // Border cases
        account.deposit(0.00);
        assertEquals(150.00, account.getBalance(), 0.001);

        account.deposit(0.25);
        assertEquals(150.25, account.getBalance(), 0.001);

        assertThrows(IllegalArgumentException.class,
                () -> account.deposit(-10.00));

        assertThrows(IllegalArgumentException.class,
                () -> account.deposit(5.123));
    }

    @Test
    void transferTest() throws InsufficientFundsException {
        BankAccount a1 = new BankAccount("a@b.com", 100);
        BankAccount a2 = new BankAccount("c@d.com", 50);

        // Middle case
        a1.transfer(a2, 25.00);
        assertEquals(75.00, a1.getBalance(), 0.001);
        assertEquals(75.00, a2.getBalance(), 0.001);

        // Border case: transfer entire balance
        a1.transfer(a2, 75.00);
        assertEquals(0.00, a1.getBalance(), 0.001);
        assertEquals(150.00, a2.getBalance(), 0.001);

        // Insufficient funds
        assertThrows(InsufficientFundsException.class,
                () -> a1.transfer(a2, 1.00));

        // Invalid amount — negative
        assertThrows(IllegalArgumentException.class,
                () -> a2.transfer(a1, -10.00));

        // Invalid amount — too many decimals
        assertThrows(IllegalArgumentException.class,
                () -> a2.transfer(a1, 5.999));
    }
}
