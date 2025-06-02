package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateCommandProcessorTest {
    private Bank bank;
    private CommandProcessor processor;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        processor = new CommandProcessor(bank);
    }

    @Test
    public void valid_checking_account_is_created() {
        processor.process("create checking 12345676 0.5");
        assertNotNull(bank.retrieveAccount(12345676));
        assertEquals("checking", bank.retrieveAccount(12345676).accountType());
    }

    @Test
    public void valid_savings_account_is_created() {
        processor.process("create savings 12345677 0.5");
        assertNotNull(bank.retrieveAccount(12345677));
        assertEquals("savings", bank.retrieveAccount(12345677).accountType());
    }

    @Test
    public void valid_cd_account_is_created_with_balance() {
        processor.process("create cd 12345675 2.5 1500");
        assertNotNull(bank.retrieveAccount(12345675));
        assertEquals("cd", bank.retrieveAccount(12345675).accountType());
        assertEquals(1500, bank.retrieveAccount(12345675).getBalance());
    }
}
