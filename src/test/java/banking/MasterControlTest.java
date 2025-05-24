import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MasterControlTest {
    private Bank bank;
    private CommandValidator validator;
    private CommandProcessor processor;
    private CommandStorage storage;
    private MasterControl masterControl;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        validator = new CommandValidator(bank);
        processor = new CommandProcessor(bank);
        storage = new CommandStorage();
        masterControl = new MasterControl(validator, processor, storage);
    }

    @Test
    public void testSingleInvalidCommand() {
        List<String> input = List.of("creat checkng 12345678 1.0"); // typo
        List<String> result = masterControl.start(input);

        assertEquals(1, result.size());
        assertEquals("creat checkng 12345678 1.0", result.get(0));
    }

    @Test
    public void testMultipleInvalidCommands() {
        List<String> input = Arrays.asList(
                "creat savings 12345678 0.5",
                "deposit 99999999 2000"
        );
        List<String> result = masterControl.start(input);

        assertEquals(2, result.size());
        assertEquals("creat savings 12345678 0.5", result.get(0));
        assertEquals("deposit 99999999 2000", result.get(1));
    }

    @Test
    public void testValidThenInvalidCommand() {
        List<String> input = Arrays.asList(
                "create checking 12345678 1.0",  // valid
                "create checking 12345678 1.0"   // invalid, duplicate ID
        );
        List<String> result = masterControl.start(input);

        assertEquals(1, result.size());
        assertEquals("create checking 12345678 1.0", result.get(0));
    }

    @Test
    public void testValidCommandDoesNotAppearInOutput() {
        List<String> input = List.of("create savings 11112222 2.0");
        List<String> result = masterControl.start(input);

        assertTrue(result.isEmpty());
        assertNotNull(bank.retrieveAccount(11112222));
    }
}

