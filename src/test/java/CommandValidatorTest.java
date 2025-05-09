import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandValidatorTest {
    private Bank bank;
    private CommandValidator validator;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        bank.addAccount(new CheckingAccount(12345678, 0.5));
        bank.addAccount(new SavingsAccount(87654321, 2.0));
        bank.addAccount(new CDAccount(11111111, 3.0, 2000));
        validator = new CommandValidator(bank);
    }

    @Test
    void validCheckingCreate() {
        assertTrue(validator.isValid("Create checking 99999999 0.01"));
    }

    @Test
    void validSavingsCreate() {
        assertTrue(validator.isValid("Create savings 88888888 0.6"));
    }

    @Test
    void validCDCreate() {
        assertTrue(validator.isValid("Create cd 77777777 1.5 2000"));
    }

    @Test
    void invalidAccountType() {
        assertFalse(validator.isValid("Create investment 12345678 0.01"));
    }

    @Test
    void duplicateId() {
        assertFalse(validator.isValid("Create checking 12345678 0.5"));
    }

    @Test
    void idTooShort() {
        assertFalse(validator.isValid("Create checking 1234567 0.01"));
    }

    @Test
    void idTooLong() {
        assertFalse(validator.isValid("Create checking 123456789 0.01"));
    }
}