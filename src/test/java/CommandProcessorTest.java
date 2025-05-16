import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandProcessorTest {
    private Bank bank;
    private CommandProcessor processor;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        processor = new CommandProcessor(bank);
    }

    @Test
    public void testCreateCheckingAccount() {
        processor.process("create checking 12345678 1.0");
        Account acc = bank.retrieveAccount(12345678);
        assertNotNull(acc);
        assertEquals("checking", acc.accountType());
        assertEquals(1.0, acc.getApr());
        assertEquals(0, acc.getBalance());
    }

}