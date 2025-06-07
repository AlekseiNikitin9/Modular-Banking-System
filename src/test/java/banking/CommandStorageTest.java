package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommandStorageTest {
    private CommandStorage storage;

    @BeforeEach
    public void setUp() {
        storage = new CommandStorage();
    }

    @Test
    public void testAddSingleCommand() {
        storage.addInvalidCommand("invalid command 1");
        List<String> result = storage.getInvalidCommands();

        assertEquals(1, result.size());
        assertEquals("invalid command 1", result.get(0));
    }

    @Test
    public void testAddMultipleCommandsMaintainsOrder() {
        storage.addInvalidCommand("invalid command 1");
        storage.addInvalidCommand("invalid command 2");
        storage.addInvalidCommand("invalid command 3");

        List<String> result = storage.getInvalidCommands();

        assertEquals(3, result.size());
        assertEquals("invalid command 1", result.get(0));
        assertEquals("invalid command 2", result.get(1));
        assertEquals("invalid command 3", result.get(2));
    }

    @Test
    public void testListIsUnmodifiable() {
        storage.addInvalidCommand("invalid command");
        List<String> result = storage.getInvalidCommands();
        assertThrows(UnsupportedOperationException.class, () -> result.add("another"));
    }
}

