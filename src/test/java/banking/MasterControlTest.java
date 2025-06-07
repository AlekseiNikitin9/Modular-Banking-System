package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MasterControlTest {
    private Bank bank;
    private CommandValidator validator;
    private CommandProcessor processor;
    private CommandStorage storage;
    private MasterControl masterControl;
    private List<String> input;
    private List<String> expected;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        validator = new CommandValidator(bank);
        processor = new CommandProcessor(bank);
        storage = new CommandStorage();
        masterControl = new MasterControl(validator, processor, storage, bank);
        input = new ArrayList<>();
        expected = new ArrayList<>();
    }

    @Test
    void typo_in_create_command_is_invalid() {
        input.add("creat checking 12345678 1.0");

        List<String> actual = masterControl.start(input);
        assertEquals(1, actual.size());
        assertSingleCommand("creat checking 12345678 1.0", actual);
    }

    @Test
    void typo_in_deposit_command_is_invalid() {
        input.add("depositt 12345678 100");

        List<String> actual = masterControl.start(input);

        assertSingleCommand("depositt 12345678 100", actual);
    }

    @Test
    void two_typo_commands_both_invalid() {
        input.add("creat checking 12345678 1.0");
        input.add("depositt 12345678 100");
        List<String> actual = masterControl.start(input);

        assertEquals(2, actual.size());
        assertEquals("creat checking 12345678 1.0", actual.get(0));
        assertEquals("depositt 12345678 100", actual.get(1));
    }

    @Test
    void invalid_to_create_accounts_with_same_ID() {
        input.add("create checking 12345678 1.0");
        input.add("create checking 12345678 1.0");
        List<String> actual = masterControl.start(input);

        expected.add("Checking 12345678 0.00 1.00");
        expected.add("create checking 12345678 1.0");
        assertEquals(expected, actual);
    }

    private void assertSingleCommand(String command, List<String> actual) {
        assertEquals(1, actual.size());
        assertEquals(command, actual.get(0));
    }

    // withdraw
    @Test
    void typo_in_withdraw_command_is_invalid() {
        input.add("withdaw 12345678 100");

        List<String> actual = masterControl.start(input);
        assertEquals(1, actual.size());
        assertSingleCommand("withdaw 12345678 100", actual);
    }

    @Test
    void withdraw_over_checking_limit_is_invalid() {
        input.add("withdraw 12345678 401");

        List<String> actual = masterControl.start(input);
        assertEquals(1, actual.size());
        assertSingleCommand("withdraw 12345678 401", actual);
    }

    @Test
    void withdraw_balance_from_cd_after_13_mo_is_valid() {
        input.add("create cd 12345678 1.0 2000");
        input.add("pass 13");
        input.add("withdraw 12345678 2088.53");
        List<String> actual = masterControl.start(input);

        List<String> expected = Arrays.asList("Cd 12345678 0.00 1.00", "Withdraw 12345678 2088.53");
        assertEquals(expected, actual);

    }

    @Test
    void one_month_after_withdrawing_balance_from_cd_account_closes_valid() {
        input.add("create cd 12345678 1.0 2000");
        input.add("pass 13");
        input.add("withdraw 12345678 2088.53");
        input.add("pass 1");
        List<String> actual = masterControl.start(input);
        assertEquals(0, actual.size());

    }

    @Test
    void withdraw_less_than_balance_from_cd_after_13_mo_is_invalid() {
        input.add("create cd 12345678 1.0 2000");
        input.add("pass 13");
        input.add("withdraw 12345678 200");
        List<String> actual = masterControl.start(input);

        List<String> expected = Arrays.asList("Cd 12345678 2088.53 1.00", "withdraw 12345678 200");
        assertEquals(expected, actual);

    }

    @Test
    void withdraw_from_cd_before_12_mo_is_valid() {
        input.add("create cd 12345678 1.0 2000");
        input.add("pass 11");
        input.add("withdraw 12345678 2074.66");
        List<String> actual = masterControl.start(input);

        List<String> expected = Arrays.asList("Cd 12345678 2074.66 1.00", "withdraw 12345678 2074.66");
        assertEquals(expected, actual);

    }

    @Test
    void multiple_commands_with_invalid_and_valid_tests() {
        input.add("create savings 12345678 0.6");
        input.add("deposit 12345678 700");
        input.add("deposit 12345678 5000");
        input.add("creAte cHecKing 98765432 0.01");
        input.add("deposit 98765432 300");
        input.add("transfer 98765432 12345678 300");
        input.add("pass 1");
        input.add("doesnt work"); // Invalid
        input.add("create cd 23456789 1.2 2000");
        input.add("withdraw 12345678 200");
        List<String> actual = masterControl.start(input);

        List<String> expected = Arrays.asList("Savings 12345678 800.50 0.60", "Deposit 12345678 700",
                "Transfer 98765432 12345678 300", "Withdraw 12345678 200", "Cd 23456789 2000.00 1.20",
                "deposit 12345678 5000", "doesnt work");

        assertEquals(expected, actual);
    }

    @Test
    void transfer_from_checking_to_savings() {
        List<String> input = Arrays.asList("create checking 12345678 1.0", "create savings 98765432 1.5",
                "deposit 12345678 1000", "transfer 12345678 98765432 200");

        List<String> actual = masterControl.start(input);
        List<String> expected = Arrays.asList("Checking 12345678 800.00 1.00", "Deposit 12345678 1000",
                "Transfer 12345678 98765432 200", "Savings 98765432 200.00 1.50", "Transfer 12345678 98765432 200");
        assertEquals(expected, actual);
    }

    @Test
    void transfer_from_checking_to_cd_is_invalid() {
        input.add("create checking 12345678 1.0");
        input.add("create cd 98765432 2.0 5000");
        input.add("deposit 12345678 1000");
        input.add("transfer 12345678 98765432 500");

        List<String> actual = masterControl.start(input);
        expected.add("Checking 12345678 1000.00 1.00");
        expected.add("Deposit 12345678 1000");
        expected.add("Cd 98765432 5000.00 2.00");
        expected.add("transfer 12345678 98765432 500");

        assertEquals(expected, actual);
    }

    @Test
    void sample_make_sure_this_passes_unchanged_or_you_will_fail() {
        input.add("Create savings 12345678 0.6");
        input.add("Deposit 12345678 700");
        input.add("Deposit 12345678 5000");
        input.add("creAte cHecKing 98765432 0.01");
        input.add("Deposit 98765432 300");
        input.add("Transfer 98765432 12345678 300");
        input.add("Pass 1");
        input.add("Create cd 23456789 1.2 2000");
        List<String> actual = masterControl.start(input);

        assertEquals(5, actual.size());
        assertEquals("Savings 12345678 1000.50 0.60", actual.get(0));
        assertEquals("Deposit 12345678 700", actual.get(1));
        assertEquals("Transfer 98765432 12345678 300", actual.get(2));
        assertEquals("Cd 23456789 2000.00 1.20", actual.get(3));
        assertEquals("Deposit 12345678 5000", actual.get(4));
    }

    @Test
    void space_after_command_is_valid() {
        input.add("Create savings 12345678 0.6");
        input.add("withdraw 12345678 399 ");
        input.add("creAte cHecKing 98765432 0.01");

        List<String> actual = masterControl.start(input);
        assertEquals("Withdraw 12345678 399", actual.get(1));
    }

    @Test
    void space_between_command_is_invalid() {
        input.add("Create savings 12345678 0.6");
        input.add("deposit 12345678 20");
        input.add("withdraw    12345678 399 ");

        List<String> actual = masterControl.start(input);
        assertEquals("Savings 12345678 20.00 0.60", actual.get(0));
    }

    @Test
    void invalid_account_id_should_not_be_processed() {
        input.add("Create savings 1234567a 0.6"); // Invalid ID
        input.add("Deposit 1234567a 700");
        input.add("Create checking 12345678 1.0");
        input.add("Deposit 12345678 1000");
        input.add("Transfer 12345678 12345679 300");

        List<String> actual = masterControl.start(input);
        assertEquals(5, actual.size());
        assertEquals("Checking 12345678 1000.00 1.00", actual.get(0));
        assertEquals("Deposit 12345678 1000", actual.get(1));
        assertEquals("Create savings 1234567a 0.6", actual.get(2));
    }

    @Test
    void create_and_deposit_should_work_correctly() {
        input.add("Create savings 12345678 0.6");
        input.add("Deposit 12345678 500");
        input.add("Deposit 12345678 700");
        input.add("Create checking 98765432 1.5");

        List<String> actual = masterControl.start(input);

        assertEquals(4, actual.size());
        System.out.println();
        assertEquals("Savings 12345678 1200.00 0.60", actual.get(0)); // Balance after deposit
        assertEquals("Deposit 12345678 500", actual.get(1));
        assertEquals("Deposit 12345678 700", actual.get(2));
        assertEquals("Checking 98765432 0.00 1.50", actual.get(3));
    }

    @Test
    void multiple_commands_with_mixed_valid_and_invalid_input() {
        input.add("Create checking 12345678 0.5");
        input.add("Deposit 12345678 500");
        input.add("Create savings 98765432 1.2");
        input.add("Transfer 12345678 98765432 300");
        input.add("Create cd 23456789 2.5 5000");
        input.add("Deposit 98765432 1500");
        input.add("invalidCommand xyz");
        input.add("Withdraw 12345678 200");

        List<String> actual = masterControl.start(input);
        assertEquals(9, actual.size());
        assertEquals("Checking 12345678 0.00 0.50", actual.get(0));
        assertEquals("Deposit 12345678 500", actual.get(1));
        assertEquals("Transfer 12345678 98765432 300", actual.get(2));
        assertEquals("Withdraw 12345678 200", actual.get(3));
        assertEquals("Savings 98765432 1800.00 1.20", actual.get(4));
        assertEquals("Transfer 12345678 98765432 300", actual.get(5));
        assertEquals("Deposit 98765432 1500", actual.get(6));
        assertEquals("Cd 23456789 5000.00 2.50", actual.get(7));
        assertEquals("invalidCommand xyz", actual.get(8));

    }
}

