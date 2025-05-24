import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandStorage {
    private final List<String> invalidCommands = new ArrayList<>();

    public void addCommand(String command) {
        invalidCommands.add(command);
    }

    public List<String> getInvalidCommands() {
        return Collections.unmodifiableList(invalidCommands);
    }
}
