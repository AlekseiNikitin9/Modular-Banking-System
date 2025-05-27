import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DepositCommandValidatorTest {
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
    void valid_deposit_to_checking() {
        assertTrue(commandValidator.isValid("deposit 13345678 100"));
    }

    @Test
    void valid_deposit_to_savings() {
        assertTrue(commandValidator.isValid("deposit 97654321 2500"));
    }

    @Test
    void deposit_to_cd_is_invalid() {
        assertFalse(commandValidator.isValid("deposit 18345679 500"));
    }

    @Test
    void deposit_over_limit_checking_is_invalid() {
        assertFalse(commandValidator.isValid("deposit 13345678 1001"));
    }

    @Test
    void deposit_over_limit_savings_is_invalid() {
        assertFalse(commandValidator.isValid("deposit 97654321 2501"));
    }

    @Test
    void deposit_negative_amount_is_invalid() {
        assertFalse(commandValidator.isValid("deposit 13345678 -500"));
    }

    @Test
    void deposit_to_nonexistent_account_is_invalid() {
        assertFalse(commandValidator.isValid("deposit 00000000 500"));
    }

    @Test
    void deposit_with_missing_amount_is_invalid() {
        assertFalse(commandValidator.isValid("deposit 13345678"));
    }

    @Test
    void deposit_with_missing_id_is_invalid() {
        assertFalse(commandValidator.isValid("deposit 500"));
    }

    @Test
    void deposit_with_id_too_long_is_invalid() {
        assertFalse(commandValidator.isValid("deposit 123456789 500"));
    }

    @Test
    void deposit_with_id_too_short_is_invalid() {
        assertFalse(commandValidator.isValid("deposit 1234567 500"));
    }

    @Test
    void deposit_with_non_numeric_id_is_invalid() {
        assertFalse(commandValidator.isValid("deposit abcdefgh 500"));
    }

    @Test
    void deposit_with_non_numeric_amount_is_invalid() {
        assertFalse(commandValidator.isValid("deposit 13345678 fifty"));
    }

    @Test
    void deposit_with_decimal_amount_is_valid() {
        assertTrue(commandValidator.isValid("deposit 13345678 99.99"));
    }

    @Test
    void deposit_with_multiple_decimal_points_is_invalid() {
        assertFalse(commandValidator.isValid("deposit 13345678 50.5.5"));
    }

    @Test
    void deposit_with_trailing_whitespace_is_valid() {
        assertTrue(commandValidator.isValid("deposit 13345678 100 "));
    }

    @Test
    void deposit_with_leading_whitespace_is_invalid() {
        assertFalse(commandValidator.isValid(" deposit 13345678 100"));
    }

    @Test
    void deposit_with_multiple_spaces_is_invalid() {
        assertFalse(commandValidator.isValid("deposit  13345678 100")); // two spaces
    }

    @Test
    void deposit_with_extra_argument_is_invalid() {
        assertFalse(commandValidator.isValid("deposit 13345678 500 extra"));
    }

    @Test
    void deposit_with_scientific_notation_is_invalid() {
        assertFalse(commandValidator.isValid("deposit 13345678 1e3"));
    }
}

