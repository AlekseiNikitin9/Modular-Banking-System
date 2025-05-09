import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandValidatorTest {
    private Bank bank;
    private CommandValidator validator;

    @BeforeEach
    void setUp(){
        Bank bank = new Bank();

        bank.addAccount(new CheckingAccount(12345678, 0.5));
        bank.addAccount(new SavingsAccount(87654321, 2.0));
        bank.addAccount(new CDAccount(11111111, 3.0, 2000));

        validator = new CommandValidator(bank);
    }

    @Test
    void validCheckingCreate() {
        assertTrue(validator.isValid("Create checking 12345678 0.01"));
    }
}
