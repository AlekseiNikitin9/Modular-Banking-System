package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassTimeCommandProcessorTest {
    public static final int id = 01234567;
    private static final double apr = 1.2;
    private static final double balance = 1000;

    private Bank bank;
    private CommandProcessor commandProcessor;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        commandProcessor = new CommandProcessor(bank);
    }

    @Test
    public void resets_savings_withdraw_flag_each_month() {
        bank.addAccount(new SavingsAccount(id, apr));

        SavingsAccount account = (SavingsAccount) bank.retrieveAccount(id);
        account.flipStatus();

        commandProcessor.process("pass 1");
        assertFalse(account.getWithdrawedThisMonth());
    }

    @Test
    public void checking_balance_increases_with_apr_after_month_passes() {
        bank.addAccount(new CheckingAccount(id, apr));
        bank.retrieveAccount(id).deposit(balance);

        commandProcessor.process("pass 1");
        assertEquals(1001.00, bank.retrieveAccount(id).getBalance());
    }

    @Test
    public void pass_time_removes_savings_account_with_zero_balance() {
        bank.addAccount(new SavingsAccount(id, apr));
        commandProcessor.process("pass 1");

        assertFalse(bank.retrieveAllAccounts().containsKey(id));
    }

    @Test
    public void minimum_balance_fee_applied_to_low_savings_balance() {
        bank.addAccount(new SavingsAccount(id, apr));
        bank.retrieveAccount(id).deposit(50);

        commandProcessor.process("pass 1");
        assertEquals(25.0, bank.retrieveAccount(id).getBalance());
    }

    @Test
    public void savings_balance_grows_correctly_with_apr() {
        bank.addAccount(new SavingsAccount(id, apr));
        bank.retrieveAccount(id).deposit(balance);

        commandProcessor.process("pass 1");
        assertEquals(1001.0, bank.retrieveAccount(id).getBalance());
    }

    @Test
    public void cd_balance_apr_applied_four_times_per_month() {
        bank.addAccount(new CDAccount(id, apr, balance));

        commandProcessor.process("pass 1");
        assertEquals(1004.0, bank.retrieveAccount(id).getBalance());
    }

    @Test
    public void checking_with_low_balance_receives_minimum_fee() {
        bank.addAccount(new CheckingAccount(id, apr));
        bank.retrieveAccount(id).deposit(50);

        commandProcessor.process("pass 1");
        assertEquals(25.0, bank.retrieveAccount(id).getBalance());
    }

    @Test
    public void zero_balance_checking_account_is_closed_on_pass() {
        bank.addAccount(new CheckingAccount(id, apr));
        commandProcessor.process("pass 1");

        assertFalse(bank.retrieveAllAccounts().containsKey(id));
    }

    @Test
    public void savings_with_enough_balance_avoids_fees_on_pass() {
        bank.addAccount(new SavingsAccount(id, 0));
        bank.retrieveAccount(id).deposit(balance);

        commandProcessor.process("pass 1");
        assertEquals(balance, bank.retrieveAccount(id).getBalance());
    }

    @Test
    public void pass_one_month_increases_cd_age_by_one() {
        bank.addAccount(new CDAccount(id, apr, balance));
        commandProcessor.process("pass 1");
        assertEquals(1, bank.retrieveAccount(id).getAccountAge());
    }

    @Test
    public void cd_age_increments_by_full_year_on_pass_twelve() {
        bank.addAccount(new CDAccount(id, apr, balance));
        commandProcessor.process("pass 12");
        assertEquals(12, bank.retrieveAccount(id).getAccountAge());
    }

    @Test
    public void pass_maximum_months_updates_cd_age_correctly() {
        bank.addAccount(new CDAccount(id, apr, balance));
        commandProcessor.process("pass 60");
        assertEquals(60, bank.retrieveAccount(id).getAccountAge());
    }

    @Test
    public void checking_with_sufficient_balance_does_not_get_fee() {
        bank.addAccount(new CheckingAccount(id, 0));
        bank.retrieveAccount(id).deposit(balance);

        commandProcessor.process("pass 1");
        assertEquals(balance, bank.retrieveAccount(id).getBalance());
    }
}
