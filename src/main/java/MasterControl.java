import java.util.ArrayList;
import java.util.List;

public class MasterControl {
    private final CommandValidator validator;
    private final CommandProcessor processor;
    private final CommandStorage storage;

    public MasterControl(CommandValidator validator, CommandProcessor processor, CommandStorage storage) {
        this.validator = validator;
        this.processor = processor;
        this.storage = storage;
    }

    public List<String> start(List<String> inputCommands) {
        for (String command : inputCommands) {
            if (validator.isValid(command)) {
                processor.process(command);
            } else {
                storage.addCommand(command);
            }
        }

        return storage.getInvalidCommands();
    }
}

