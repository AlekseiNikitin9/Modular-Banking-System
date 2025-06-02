package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandProcessorTest {
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
    public void empty_command_does_nothing() {
        processor.process("");
        assertEquals(3, bank.getNumberOfAccounts());
    }

    @Test
    public void unknown_command_does_nothing() {
        processor.process("fly 12345678");
        assertEquals(3, bank.getNumberOfAccounts());
    }

    @Test
    public void command_with_no_action_does_nothing() {
        processor.process("12345678 500");
        assertEquals(3, bank.getNumberOfAccounts());
    }
}
