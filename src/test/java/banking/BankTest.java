import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankTest {

    private Bank bank;
    private Account cdAccount;
    private Account checkingAccount;
    private Account savingsAccount;
    private static final int ID_CD = 11111111;
    private static final int ID_CHECKING = 22222222;
    private static final int ID_SAVINGS = 33333333;
    private static final double APR = 7.7;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        cdAccount = new CDAccount(ID_CD, APR, 1000);
        checkingAccount = new CheckingAccount(ID_CHECKING, APR);
        savingsAccount = new SavingsAccount(ID_SAVINGS, APR);
    }

    @Test
    public void when_created_has_no_accounts() {
        assertEquals(0, bank.getNumberOfAccounts());
    }

    @Test
    public void when_an_account_is_added_bank_has_1_account() {
        bank.addAccount(checkingAccount);
        assertEquals(1, bank.getNumberOfAccounts());
    }

    @Test
    public void when_2_accounts_added_bank_has_2_accounts() {
        bank.addAccount(cdAccount);
        bank.addAccount(savingsAccount);
        assertEquals(2, bank.getNumberOfAccounts());
    }

    @Test
    public void correct_account_retrieved() {
        bank.addAccount(checkingAccount);
        assertEquals(APR, bank.retrieveAccount(ID_CHECKING).getApr());
    }

    @Test
    public void correct_account_receives_deposit() {
        bank.addAccount(cdAccount);
        bank.addAccount(savingsAccount);
        double beforeCd = cdAccount.getBalance();
        double beforeSavings = savingsAccount.getBalance();
        bank.depositById(ID_CD, 300);
        double afterCd = cdAccount.getBalance();
        double afterSavings = savingsAccount.getBalance();
        assertEquals(300, afterCd - beforeCd);           // cdAccount recieves the deposit
        assertEquals(beforeSavings, afterSavings);       // savingsAccount stays the same
    }

    @Test
    public void correct_account_loses_withdrawal() {
        bank.addAccount(cdAccount);
        bank.addAccount(savingsAccount);
        double beforeCd = cdAccount.getBalance();
        double beforeSavings = savingsAccount.getBalance();
        bank.withdrawById(ID_CD, 500);
        double afterCd = cdAccount.getBalance();
        double afterSavings = savingsAccount.getBalance();
        assertEquals(500, beforeCd - afterCd);            // cdAccount loses the money
        assertEquals(beforeSavings, afterSavings);        // savingsAccount stays the same
    }

    @Test
    public void depositing_twice() {
        bank.addAccount(checkingAccount);
        double before = checkingAccount.getBalance();
        bank.depositById(ID_CHECKING, 200);
        bank.depositById(ID_CHECKING, 300);
        double after = checkingAccount.getBalance();
        assertEquals(500, after - before);
    }

    @Test
    public void withdrawing_twice() {
        bank.addAccount(cdAccount);
        double before = cdAccount.getBalance();
        bank.withdrawById(ID_CD, 500);
        bank.withdrawById(ID_CD, 500);
        double after = cdAccount.getBalance();
        assertEquals(0, after);  // balance shouldn’t go below 0
    }
}
