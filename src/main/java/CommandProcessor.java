public class CommandProcessor {
    private final CreateCommandProcessor createProcessor;
    private final DepositCommandProcessor depositProcessor;

    public CommandProcessor(Bank bank) {
        this.createProcessor = new CreateCommandProcessor(bank);
        this.depositProcessor = new DepositCommandProcessor(bank);
    }

    public void process(String command) {
        if (command == null || command.isEmpty()) return;
        String[] parts = command.split(" ");
        if (parts.length < 1) return;

        switch (parts[0].toLowerCase()) {
            case "create":
                createProcessor.process(parts);
                break;
            case "deposit":
                depositProcessor.process(parts);
                break;
        }
    }
}
