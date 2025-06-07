package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WithdrawCommandProcessorTest {
    private static final int ACCOUNT_ID = 12345678;
    private static final double APR = 1.0;
    private static final double INITIAL_BALANCE = 1000;

    private Bank bank;
    private CommandProcessor processor;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        processor = new CommandProcessor(bank);
    }

    @Test
    public void withdraw_zero_from_checking_account_leaves_balance_unchanged() {
        bank.addAccount(new CheckingAccount(ACCOUNT_ID, APR));
        bank.depositById(ACCOUNT_ID, INITIAL_BALANCE);

        processor.process("withdraw 12345678 0");
        assertEquals(INITIAL_BALANCE, bank.retrieveAccount(ACCOUNT_ID).getBalance());
    }

    @Test
    public void withdraw_from_checking_account_reduces_balance_correctly() {
        bank.addAccount(new CheckingAccount(ACCOUNT_ID, APR));
        bank.depositById(ACCOUNT_ID, INITIAL_BALANCE);

        processor.process("withdraw 12345678 400");
        assertEquals(600, bank.retrieveAccount(ACCOUNT_ID).getBalance());
    }

    @Test
    public void withdraw_more_than_balance_from_checking_sets_balance_to_zero() {
        bank.addAccount(new CheckingAccount(ACCOUNT_ID, APR));
        bank.depositById(ACCOUNT_ID, 300);
        processor.process("withdraw 12345678 400");
        assertEquals(0, bank.retrieveAccount(ACCOUNT_ID).getBalance());
    }

    @Test
    public void multiple_withdrawals_from_checking_account_reduce_balance_accurately() {
        bank.addAccount(new CheckingAccount(ACCOUNT_ID, APR));
        bank.depositById(ACCOUNT_ID, INITIAL_BALANCE);

        processor.process("withdraw 12345678 100");
        processor.process("withdraw 12345678 100");
        processor.process("withdraw 12345678 100");

        assertEquals(700, bank.retrieveAccount(ACCOUNT_ID).getBalance());
    }

    @Test
    public void withdraw_zero_from_savings_account_leaves_balance_unchanged() {
        bank.addAccount(new SavingsAccount(ACCOUNT_ID, APR));
        bank.depositById(ACCOUNT_ID, INITIAL_BALANCE);

        processor.process("withdraw 12345678 0");
        assertEquals(INITIAL_BALANCE, bank.retrieveAccount(ACCOUNT_ID).getBalance());
    }

    @Test
    public void withdraw_from_savings_account_reduces_balance_correctly() {
        bank.addAccount(new SavingsAccount(ACCOUNT_ID, APR));
        bank.depositById(ACCOUNT_ID, INITIAL_BALANCE);

        processor.process("withdraw 12345678 1000");
        assertEquals(0, bank.retrieveAccount(ACCOUNT_ID).getBalance());
    }

    @Test
    public void withdraw_less_than_max_limit_from_savings_account_is_valid() {
        bank.addAccount(new SavingsAccount(ACCOUNT_ID, APR));
        bank.depositById(ACCOUNT_ID, INITIAL_BALANCE);

        processor.process("withdraw 12345678 999");
        assertEquals(1, bank.retrieveAccount(ACCOUNT_ID).getBalance());
    }

    @Test
    public void withdraw_more_than_balance_from_savings_sets_balance_to_zero() {
        bank.addAccount(new SavingsAccount(ACCOUNT_ID, APR));
        bank.depositById(ACCOUNT_ID, 300);

        processor.process("withdraw 12345678 400");
        assertEquals(0, bank.retrieveAccount(ACCOUNT_ID).getBalance());
    }

    @Test
    public void withdraw_full_balance_from_cd_account_after_12_months_sets_balance_to_zero() {
        bank.addAccount(new CDAccount(ACCOUNT_ID, APR, INITIAL_BALANCE));
        bank.retrieveAccount(ACCOUNT_ID).incrementAge(13);

        processor.process("withdraw 12345678 1000");
        assertEquals(0, bank.retrieveAccount(ACCOUNT_ID).getBalance());
    }

    @Test
    void withdraw_entire_balance_sets_balance_to_zero() {
        bank.addAccount(new CheckingAccount(12345678, 1.0));
        bank.depositById(12345678, 100);
        processor.process("withdraw 12345678 100");
        assertEquals(0, bank.retrieveAccount(12345678).getBalance());
    }


    @Test
    void withdraw_from_savings_after_limit_does_nothing() {
        SavingsAccount acc = new SavingsAccount(12345678, 1.0);
        bank.addAccount(acc);
        acc.flipStatus();
        processor.process("withdraw 12345678 100");
        assertEquals(0, bank.retrieveAccount(12345678).getBalance());
    }

    @Test
    void savings_withdrawal_flag_resets_after_pass_time() {
        bank.addAccount(new SavingsAccount(12345678, 1.0));
        SavingsAccount acc = (SavingsAccount) bank.retrieveAccount(12345678);

        acc.flipStatus(); // simulate a withdrawal this month
        assertTrue(acc.getWithdrawedThisMonth());

        processor.process("pass 1");

        assertFalse(acc.getWithdrawedThisMonth());
    }

}
