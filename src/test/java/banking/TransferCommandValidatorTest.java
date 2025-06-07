package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransferCommandValidatorTest {
    private Bank bank;
    private CommandValidator validator;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        validator = new CommandValidator(bank);

        bank.addAccount(new CheckingAccount(12345678, 0.01));
        bank.depositById(12345678, 1000);
        bank.addAccount(new SavingsAccount(87654321, 0.01));
        bank.depositById(87654321, 1000);
        bank.addAccount(new CDAccount(22334455, 2.5, 1000));
    }

    @Test
    void transfer_with_valid_checking_to_savings_is_valid() {
        assertTrue(validator.isValid("transfer 12345678 87654321 400"));
    }

    @Test
    void transfer_with_more_than_checking_limit_is_invalid() {
        assertFalse(validator.isValid("transfer 12345678 87654321 401"));
    }

    @Test
    void transfer_with_more_than_savings_limit_is_invalid() {
        assertFalse(validator.isValid("transfer 87654321 12345678 1001"));
    }

    @Test
    void transfer_with_valid_savings_to_checking_is_valid() {
        assertTrue(validator.isValid("transfer 87654321 12345678 999"));
    }

    @Test
    void transfer_zero_is_valid() {
        assertTrue(validator.isValid("transfer 87654321 12345678 0"));
    }

    @Test
    void transfer_with_savings_and_prior_withdraw_is_invalid() {
        ((SavingsAccount) bank.retrieveAccount(87654321)).flipStatus();
        assertFalse(validator.isValid("transfer 87654321 12345678 500"));
    }

    @Test
    void transfer_with_invalid_sender_id_is_invalid() {
        assertFalse(validator.isValid("transfer 99999999 12345678 100"));
    }

    @Test
    void transfer_with_same_sender_and_receiver_is_invalid() {
        assertFalse(validator.isValid("transfer 12345678 12345678 100"));
    }

    @Test
    void transfer_with_non_numeric_amount_is_invalid() {
        assertFalse(validator.isValid("transfer 12345678 87654321 ten"));
    }

    @Test
    void transfer_with_missing_arguments_is_invalid() {
        assertFalse(validator.isValid("transfer 12345678 87654321"));
    }

    @Test
    void transfer_from_cd_account_is_invalid() {
        assertFalse(validator.isValid("transfer 22334455 12345678 100"));
    }

    @Test
    void transfer_to_cd_account_is_invalid() {
        assertFalse(validator.isValid("transfer 12345678 22334455 100"));
    }

    @Test
    void transfer_with_negative_amount_is_invalid() {
        assertFalse(validator.isValid("transfer 12345678 87654321 -100"));
    }

    @Test
    void transfer_with_text_amount_is_invalid() {
        assertFalse(validator.isValid("transfer 12345678 87654321 twenty"));
    }
}
