package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DepositCommandProcessorTest {
    private Bank bank;
    private CommandProcessor processor;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        bank.addAccount(new CheckingAccount(13345678, 0.01));
        bank.addAccount(new SavingsAccount(97654321, 0.01));
        bank.addAccount(new CDAccount(18345679, 2.5, 1000));
        processor = new CommandProcessor(bank);
    }

    @Test
    public void deposit_into_checking_account_updates_balance() {
        processor.process("deposit 13345678 500");
        assertEquals(500, bank.retrieveAccount(13345678).getBalance());
    }

    @Test
    public void deposit_into_savings_account_updates_balance() {
        processor.process("deposit 97654321 250");
        assertEquals(250, bank.retrieveAccount(97654321).getBalance());
    }

    @Test
    void deposit_with_decimal_cents_is_applied_correctly() {
        bank.addAccount(new CheckingAccount(12345678, 1.0));
        processor.process("deposit 12345678 250.75");
        assertEquals(250.75, bank.retrieveAccount(12345678).getBalance());
    }

    @Test
    void deposit_with_zero_amount_does_not_change_balance() {
        bank.addAccount(new CheckingAccount(12345678, 1.0));
        processor.process("deposit 12345678 0");
        assertEquals(0, bank.retrieveAccount(12345678).getBalance());
    }

}
