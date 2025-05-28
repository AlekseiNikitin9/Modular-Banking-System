import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandValidatorTest {
    private Bank bank;
    private CommandValidator commandValidator;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);

        bank.addAccount(new CheckingAccount(13345678, 0.01));
        bank.addAccount(new SavingsAccount(97654321, 0.01));
        bank.addAccount(new CDAccount(18345679, 2.5, 1000));
    }

    @Test
    void empty_command_returns_false() {
        assertFalse(commandValidator.isValid(""));
    }

    @Test
    void unknown_command_returns_false() {
        assertFalse(commandValidator.isValid("unknown 12345678 500"));
    }

    @Test
    void command_with_no_action_returns_false() {
        assertFalse(commandValidator.isValid("12345678 500"));
    }
    //5/28
}
