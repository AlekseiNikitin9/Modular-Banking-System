package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateCommandValidatorTest {
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
    void create_command_with_too_many_arguments_is_invalid() {
        assertFalse(commandValidator.isValid("create cd 12345678 2.5 1000 extra"));
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
    void command_with_mixed_case_account_type_is_valid() {
        assertTrue(commandValidator.isValid("create SaVinGs 12345678 0.5"));
    }

    @Test
    void valid_create_command_is_recognized() {
        assertTrue(commandValidator.isValid("create savings 12345676 0.5"));
    }

    @Test
    void valid_create_command() {
        assertTrue(commandValidator.isValid("create savings 12345676 0.5"));
    }

    @Test
    void valid_create_checking_account() {
        assertTrue(commandValidator.isValid("create checking 12345678 5"));
    }

    @Test
    void valid_create_savings_account() {
        assertTrue(commandValidator.isValid("create savings 12345678 3"));
    }

    @Test
    void create_with_apr_as_text_is_invalid() {
        assertFalse(commandValidator.isValid("create checking 12345678 wowweee"));
    }

    @Test
    void apr_as_negative_is_invalid() {
        assertFalse(commandValidator.isValid("create checking 12345678 -1"));
    }

    @Test
    void apr_as_zero_is_valid() {
        assertTrue(commandValidator.isValid("create checking 12345678 0"));
    }

    @Test
    void apr_one_above_the_minimum_is_valid() {
        assertTrue(commandValidator.isValid("create checking 12345678 1"));
    }

    @Test
    void apr_within_bounds_is_valid() {
        assertTrue(commandValidator.isValid("create checking 12345678 5"));
    }

    @Test
    void apr_one_below_max_is_valid() {
        assertTrue(commandValidator.isValid("create checking 12345678 9"));
    }

    @Test
    void apr_equal_to_max_is_valid() {
        assertTrue(commandValidator.isValid("create checking 12345678 10"));
    }

    @Test
    void apr_above_max_is_invalid() {
        assertFalse(commandValidator.isValid("create checking 12345678 11"));
    }

    @Test
    void apr_with_decimal_is_valid() {
        assertTrue(commandValidator.isValid("create checking 12345678 2.2"));
    }

    @Test
    void create_cd_without_balance_is_invalid() {
        assertFalse(commandValidator.isValid("create cd 12345678 2.5"));
    }

    @Test
    void create_cd_with_balance_as_text_is_invalid() {
        assertFalse(commandValidator.isValid("create cd 12345678 2.5 cewe"));
    }

    @Test
    void create_cd_with_negative_balance_is_invalid() {
        assertFalse(commandValidator.isValid("create cd 12345678 2.5 -1000"));
    }

    @Test
    void create_cd_with_balance_below_min_is_invalid() {
        assertFalse(commandValidator.isValid("create cd 12345678 2.5 999"));
    }

    @Test
    void create_cd_with_balance_at_min_is_valid() {
        assertTrue(commandValidator.isValid("create cd 12345678 2.5 1000"));
    }

    @Test
    void create_cd_with_normal_balance_is_valid() {
        assertTrue(commandValidator.isValid("create cd 12345678 2.5 5000"));
    }

    @Test
    void create_cd_with_balance_at_max_is_valid() {
        assertTrue(commandValidator.isValid("create cd 12345678 2.5 10000"));
    }

    @Test
    void create_cd_with_balance_above_max_is_invalid() {
        assertFalse(commandValidator.isValid("create cd 12345678 2.5 10001"));
    }

    @Test
    void savings_initiated_with_balance_is_invalid() {
        assertFalse(commandValidator.isValid("create savings 87654321 0.5 300"));
    }

    @Test
    void checking_initiated_with_balance_is_invalid() {
        assertFalse(commandValidator.isValid("create checking 12345678 0.5 500"));
    }

    @Test
    void create_with_missing_command_is_invalid() {
        assertFalse(commandValidator.isValid("create"));
    }

    @Test
    void create_with_missing_account_type_is_invalid() {
        assertFalse(commandValidator.isValid("create 12345678 2.5"));
    }

    @Test
    void create_with_missing_apr_is_invalid() {
        assertFalse(commandValidator.isValid("create checking 12345678"));
    }

    @Test
    void create_with_invalid_account_type_is_invalid() {
        assertFalse(commandValidator.isValid("create unknown 12345678 2.5"));
    }

    @Test
    void create_command_is_case_insensitive() {
        assertTrue(commandValidator.isValid("create SaVinGs 12345678 2.5"));
    }

    @Test
    void create_with_no_id_is_invalid() {
        assertFalse(commandValidator.isValid("create cd 7 1100"));
    }

    @Test
    void create_with_text_id_is_invalid() {
        assertFalse(commandValidator.isValid("create cd Efcecell 7 1100"));
    }

    @Test
    void create_with_id_too_long_is_invalid() {
        assertFalse(commandValidator.isValid("create cd 123456789 7 1100"));
    }

    @Test
    void create_with_id_too_short_is_invalid() {
        assertFalse(commandValidator.isValid("create cd 123456 7 1100"));
    }

    @Test
    void create_with_valid_id_length_is_valid() {
        assertTrue(commandValidator.isValid("create cd 12345678 7 1100"));
    }

    @Test
    void create_with_invalid_id_format_dash_is_invalid() {
        assertFalse(commandValidator.isValid("create cd 12-345678 7 1100"));
    }

    @Test
    void create_with_invalid_id_format_slash_is_invalid() {
        assertFalse(commandValidator.isValid("create cd 12/345678 7 1100"));
    }

    @Test
    void create_with_slash_at_start_is_invalid() {
        assertFalse(commandValidator.isValid("create cd /2345678 7 1100"));
    }

    @Test
    void create_with_duplicate_id_is_invalid() {
        bank.addAccount(new SavingsAccount(12345678, 0.5));
        assertFalse(commandValidator.isValid("create savings 12345678 0.5"));
    }
}
