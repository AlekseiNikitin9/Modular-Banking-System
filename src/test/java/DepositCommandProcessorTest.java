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
}
