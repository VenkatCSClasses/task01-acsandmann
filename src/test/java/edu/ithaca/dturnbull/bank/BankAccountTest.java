package edu.ithaca.dturnbull.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


class BankAccountTest {

    @Test
    void getBalanceTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals(200, bankAccount.getBalance(), 0.001);
    }

    @Test
    void withdrawTest() throws InsufficientFundsException{
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        bankAccount.withdraw(100);

        assertEquals(100, bankAccount.getBalance(), 0.001);
        assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(300));
    }

    @Test
void isEmailValidTest(){

    // Equivalence class: VALID email formats
    // Contains exactly one '@', characters before '@', and a '.' after '@'
    // Border case: NO (this is a typical valid email)
    assertTrue(BankAccount.isEmailValid("a@b.com"));

    // Equivalence class: INVALID email formats – empty input
    // Border case: YES (minimum-length input)
    assertFalse(BankAccount.isEmailValid(""));

    // Equivalence class: INVALID email formats – missing '@' symbol
    // Border case: NO (general invalid format)
    assertFalse(BankAccount.isEmailValid("ab.com"));

    // Equivalence class: INVALID email formats – missing local part (before '@')
    // Border case: YES (boundary where '@' is at index 0)
    assertFalse(BankAccount.isEmailValid("@ab.com"));

    // Equivalence class: INVALID email formats – missing '.' in domain
    // Border case: YES (very close to valid but missing required separator)
    assertFalse(BankAccount.isEmailValid("a@bcom"));
}

    @Test
    void isAmountValidTest() {

    // Middle case: typical positive amount with two decimals
    assertTrue(BankAccount.isAmountValid(10.50));

    // Border case: zero amount
    assertTrue(BankAccount.isAmountValid(0.00));

    // Border case: whole number (no decimal places)
    assertTrue(BankAccount.isAmountValid(25.0));

    // Border case: one decimal place
    assertTrue(BankAccount.isAmountValid(3.5));

    // Middle case: clearly negative amount
    assertFalse(BankAccount.isAmountValid(-10.25));

    // Border case: very small negative amount
    assertFalse(BankAccount.isAmountValid(-0.01));

    // Middle case: three decimal places
    assertFalse(BankAccount.isAmountValid(1.234));

    // Border case: just barely invalid (third decimal place)
    assertFalse(BankAccount.isAmountValid(0.001));

    // Middle case: many decimal places
    assertFalse(BankAccount.isAmountValid(12.34567));

    // Middle case: negative with excessive precision
    assertFalse(BankAccount.isAmountValid(-4.567));
}



    @Test
    void constructorTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals("a@b.com", bankAccount.getEmail());
        assertEquals(200, bankAccount.getBalance(), 0.001);
        //check for exception thrown correctly
        assertThrows(IllegalArgumentException.class, ()-> new BankAccount("", 100));
    }

}