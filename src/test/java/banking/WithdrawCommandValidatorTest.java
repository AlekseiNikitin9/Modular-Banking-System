package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WithdrawCommandValidatorTest {
    private Bank bank;
    private CommandValidator commandValidator;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);

        bank.addAccount(new CheckingAccount(13345678, 0.01));
        bank.depositById(13345678, 1000);
        bank.addAccount(new SavingsAccount(97654321, 0.01));
        bank.depositById(97654321, 1000);
        bank.addAccount(new CDAccount(18345679, 2.5, 1000));
    }

    @Test
    void withdraw_command_with_missing_all_arguments_is_invalid() {
        assertFalse(commandValidator.isValid("withdraw"));
    }

    @Test
    void withdraw_command_with_missing_amount_is_invalid() {
        assertFalse(commandValidator.isValid("withdraw 13345678"));
    }

    @Test
    void withdraw_command_with_missing_id_is_invalid() {
        assertFalse(commandValidator.isValid("withdraw 1000"));
    }

    @Test
    void withdraw_command_with_too_many_arguments_is_invalid() {
        assertFalse(commandValidator.isValid("withdraw 13345678 100 extra"));
    }

    @Test
    void withdraw_with_non_numeric_id_is_invalid() {
        assertFalse(commandValidator.isValid("withdraw invalidID 100"));
    }

    @Test
    void withdraw_with_non_numeric_amount_is_invalid() {
        assertFalse(commandValidator.isValid("withdraw 13345678 fifty"));
    }

    @Test
    void withdraw_from_nonexistent_account_is_invalid() {
        assertFalse(commandValidator.isValid("withdraw 00000000 100"));
    }

    @Test
    void withdraw_from_cd_account_younger_than_12_months_is_invalid() {
        assertFalse(commandValidator.isValid("withdraw 18345679 1000"));
    }

    @Test
    void withdraw_exact_balance_from_13_month_old_cd_is_valid() {
        bank.retrieveAccount(18345679).incrementAge(13);
        assertTrue(commandValidator.isValid("withdraw 18345679 1000"));
    }

    @Test
    void withdraw_less_than_balance_from_13_month_old_cd_is_invalid() {
        bank.retrieveAccount(18345679).incrementAge(13);
        assertFalse(commandValidator.isValid("withdraw 18345679 500"));
    }

    @Test
    void withdraw_more_than_balance_from_13_month_old_cd_is_valid() {
        bank.retrieveAccount(18345679).incrementAge(13);
        assertTrue(commandValidator.isValid("withdraw 18345679 1100"));
    }

    @Test
    void withdraw_exceeding_savings_limit_is_invalid() {
        assertFalse(commandValidator.isValid("withdraw 97654321 3000"));
    }

    @Test
    void withdraw_exact_savings_limit_is_valid() {
        assertTrue(commandValidator.isValid("withdraw 97654321 1000"));
    }

    @Test
    void withdraw_below_savings_limit_is_valid() {
        assertTrue(commandValidator.isValid("withdraw 97654321 999"));
    }

    @Test
    void withdraw_one_dollar_from_savings_is_valid() {
        assertTrue(commandValidator.isValid("withdraw 97654321 1"));
    }

    @Test
    void withdraw_zero_from_savings_is_valid() {
        assertTrue(commandValidator.isValid("withdraw 97654321 0"));
    }

    @Test
    void withdraw_negative_amount_from_savings_is_invalid() {
        assertFalse(commandValidator.isValid("withdraw 97654321 -100"));
    }

    @Test
    void withdraw_exceeding_checking_limit_is_invalid() {
        assertFalse(commandValidator.isValid("withdraw 13345678 401"));
    }

    @Test
    void withdraw_exact_checking_limit_is_valid() {
        assertTrue(commandValidator.isValid("withdraw 13345678 400"));
    }

    @Test
    void withdraw_below_checking_limit_is_valid() {
        assertTrue(commandValidator.isValid("withdraw 13345678 399"));
    }

    @Test
    void withdraw_one_dollar_from_checking_is_valid() {
        assertTrue(commandValidator.isValid("withdraw 13345678 1"));
    }

    @Test
    void _12_months_old_valid_cd() {
        bank.retrieveAccount(18345679).incrementAge(12);
        assertTrue(commandValidator.isValid("withdraw 18345679 1000"));
    }

    @Test
    void cd_can_only_withdraw_full_sum() {
        bank.retrieveAccount(18345679).incrementAge(12);
        assertFalse(commandValidator.isValid("withdraw 12 10"));
    }

    @Test
    void withdraw_zero_from_checking_is_valid() {
        assertTrue(commandValidator.isValid("withdraw 13345678 0"));
    }

    @Test
    void withdraw_negative_amount_from_checking_is_invalid() {
        assertFalse(commandValidator.isValid("withdraw 13345678 -1"));
    }

    @Test
    void withdraw_fails_if_savings_has_already_withdrawn_this_month() {
        SavingsAccount acc = new SavingsAccount(12345678, 1.0);
        acc.flipStatus();  // Simulate that it already withdrew this month
        bank.addAccount(acc);

        boolean result = commandValidator.isValid("withdraw 12345678 100");

        assertFalse(result);
    }

}
