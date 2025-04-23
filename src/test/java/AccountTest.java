import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountTest {

    private static final int ID = 12345678;
    private static final double APR = 5.5;
    private Account account;

    @BeforeEach
    public void setUp() {
        account = new CheckingAccount(ID, APR);
    }

    @Test
    public void apr_is_set_correctly() {
        assertEquals(APR, account.getApr());
    }

    @Test
    public void deposit_increases_balance() {
        double toDeposit = 300.5;
        double before = account.getBalance();
        account.deposit(toDeposit);
        double after = account.getBalance();
        assertEquals(before + toDeposit, after);
    }

    @Test
    public void withdraw_decreases_balance() {
        account.setBalance(1000);
        double toWithdraw = 300.5;
        double before = account.getBalance();
        account.withdraw(toWithdraw);
        double after = account.getBalance();
        assertEquals(before - toWithdraw, after);
    }

    @Test
    public void withdraw_more_than_balance_sets_balance_to_zero() {
        account.setBalance(100);
        account.withdraw(500);
        assertEquals(0, account.getBalance());
    }

    @Test
    public void deposit_twice_accumulates_balance() {
        account.deposit(100);
        account.deposit(100);
        assertEquals(200, account.getBalance());
    }

    @Test
    public void withdraw_twice_removes_correct_amount() {
        account.setBalance(200);
        account.withdraw(100);
        account.withdraw(100);
        assertEquals(0, account.getBalance());
    }
}
