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

    @Test
    void deposit_command_with_only_keyword_returns_false() {
        assertFalse(commandValidator.isValid("deposit"));
    }

    @Test
    void command_with_mixed_case_account_type_is_valid() {
        assertTrue(commandValidator.isValid("create SaVinGs 12345678 0.5"));
    }

    @Test
    void valid_create_command_is_recognized() {
        assertTrue(commandValidator.isValid("create savings 12345676 0.5"));
    }

    @Test
    void valid_deposit_command_is_recognized() {
        assertTrue(commandValidator.isValid("deposit 13345678 500"));
    }

    @Test
    void leading_whitespace_is_invalid() {
        assertFalse(commandValidator.isValid(" create checking 12345678 0.5"));
    }

    @Test
    void multiple_spaces_between_tokens_is_invalid() {
        assertFalse(commandValidator.isValid("create  checking 12345678 0.5"));
    }

    @Test
    void trailing_whitespace_is_valid() {
        assertTrue(commandValidator.isValid("create checking 12345678 0.5 "));
    }

    @Test
    void command_with_too_few_arguments_is_invalid() {
        assertFalse(commandValidator.isValid("create checking"));              // too short
        assertFalse(commandValidator.isValid("create checking 12345678"));     // still under min
    }

    @Test
    void command_with_too_many_arguments_is_invalid() {
        assertFalse(commandValidator.isValid("create cd 12345678 2.5 1000 extra")); // 6 args
        assertFalse(commandValidator.isValid("deposit 12345678 100 extra"));       // 4 args
    }
}
