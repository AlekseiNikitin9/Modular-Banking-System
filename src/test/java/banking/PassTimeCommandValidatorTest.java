package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassTimeCommandValidatorTest {
    private CommandValidator commandValidator;

    @BeforeEach
    void setUp() {
        commandValidator = new CommandValidator(new Bank());
    }

    @Test
    void command_with_exact_maximum_months_is_valid() {
        assertTrue(commandValidator.isValid("pass 60"));
    }

    @Test
    void command_with_valid_input_format_is_accepted() {
        boolean actual = commandValidator.isValid("pass 2");
        assertTrue(actual);
    }

    @Test
    void command_with_negative_month_value_fails() {
        assertFalse(commandValidator.isValid("pass -1"));
    }

    @Test
    void command_missing_argument_is_rejected() {
        assertFalse(commandValidator.isValid("pass"));
    }

    @Test
    void command_with_one_more_than_allowed_months_is_invalid() {
        assertFalse(commandValidator.isValid("pass 61"));
    }

    @Test
    void command_with_text_as_months_fails_validation() {
        assertFalse(commandValidator.isValid("pass two"));
    }

    @Test
    void command_with_one_month_passes_validation() {
        assertTrue(commandValidator.isValid("pass 1"));
    }

    @Test
    void command_with_unexpected_text_argument_fails() {
        assertFalse(commandValidator.isValid("pass 1 month"));
    }

    @Test
    void command_with_additional_number_argument_is_not_valid() {
        assertFalse(commandValidator.isValid("pass 1 11"));
    }

    @Test
    void command_with_zero_as_month_is_invalid() {
        assertFalse(commandValidator.isValid("pass 0"));
    }

    @Test
    void command_with_one_less_than_max_limit_is_valid() {
        assertTrue(commandValidator.isValid("pass 59"));
    }
}
