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
    void valid_command() {
        boolean actual = commandValidator.isValid("pass 2");
        assertTrue(actual);
    }

    @Test
    void pass_time_with_no_parameters_given_is_invalid() {
        assertFalse(commandValidator.isValid("pass"));
    }

    @Test
    void pass_time_with_extra_int_parameter_given_is_invalid() {
        assertFalse(commandValidator.isValid("pass 1 11"));
    }

    @Test
    void pass_time_with_extra_text_parameter_given_is_invalid() {
        assertFalse(commandValidator.isValid("pass 1 month"));
    }

    @Test
    void pass_time_with_moths_as_text_is_invalid() {
        assertFalse(commandValidator.isValid("pass two"));
    }

    @Test
    void pass_time_with_negative_months_is_invalid() {
        assertFalse(commandValidator.isValid("pass -1"));
    }

    @Test
    void pass_time_with_zero_months_is_invalid() {
        assertFalse(commandValidator.isValid("pass 0"));
    }

    @Test
    void pass_time_one_month_is_valid() {
        assertTrue(commandValidator.isValid("pass 1"));
    }

    @Test
    void pass_time_one_less_than_max_months_is_valid() {
        assertTrue(commandValidator.isValid("pass 59"));
    }

    @Test
    void pass_time_with_max_months_is_valid() {
        assertTrue(commandValidator.isValid("pass 60"));
    }

    @Test
    void pass_time_with_one_more_than_max_months_is_invalid() {
        assertFalse(commandValidator.isValid("pass 61"));
    }
}
