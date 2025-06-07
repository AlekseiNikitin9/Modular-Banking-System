package banking;

import java.util.ArrayList;
import java.util.List;

public class MasterControl {
    private final CommandValidator validator;
    private final CommandProcessor processor;
    private final CommandStorage storage;
    private final Bank bank;

    public MasterControl(CommandValidator validator, CommandProcessor processor, CommandStorage storage, Bank bank) {
        this.validator = validator;
        this.processor = processor;
        this.storage = storage;
        this.bank = bank;
    }

    public List<String> start(List<String> inputCommands) {
        for (String command : inputCommands) {
            if (validator.isValid(command)) {
                processor.process(command);
            } else {
                storage.addInvalidCommand(command);
            }
        }
        List<String> output = new ArrayList<>();
        output.addAll(bank.formattedAccDetails());
        output.addAll(storage.getInvalidCommands());
        return output;
    }
}

