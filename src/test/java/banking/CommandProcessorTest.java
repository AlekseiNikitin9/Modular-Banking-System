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
    @Test
    public void testCreateCdAccountWithBalance() {
        processor.process("create cd 87654321 2.5 3000");
        Account acc = bank.retrieveAccount(87654321);
        assertNotNull(acc);
        assertEquals("cd", acc.accountType());
        assertEquals(2.5, acc.getApr());
        assertEquals(3000, acc.getBalance());
    }

    @Test
    public void testDepositToAccount() {
        processor.process("create checking 12345678 1.0");
        processor.process("deposit 12345678 500");
        Account acc = bank.retrieveAccount(12345678);
        assertEquals(500, acc.getBalance());
    }

    @Test
    public void testMultipleDeposits() {
        processor.process("create checking 12345678 1.0");
        processor.process("deposit 12345678 200");
        processor.process("deposit 12345678 300");
        Account acc = bank.retrieveAccount(12345678);
        assertEquals(500, acc.getBalance());
    }
}