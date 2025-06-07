package banking;

public class CommandProcessor {
    private final CreateCommandProcessor createProcessor;
    private final DepositCommandProcessor depositProcessor;
    private final PassTimeCommandProcessor passProcessor;
    private final WithdrawCommandProcessor withdrawProcessor;
    private final TransferCommandProcessor transferProcessor;

    public CommandProcessor(Bank bank) {
        this.createProcessor = new CreateCommandProcessor(bank);
        this.depositProcessor = new DepositCommandProcessor(bank);
        this.passProcessor = new PassTimeCommandProcessor(bank);
        this.withdrawProcessor = new WithdrawCommandProcessor(bank);
        this.transferProcessor = new TransferCommandProcessor(bank);
    }

    public void process(String command) {
        if (command == null || command.isEmpty()) return;
        command = command.toLowerCase();
        String[] parts = command.split(" ");
        if (parts.length < 1) return;

        switch (parts[0].toLowerCase()) {
            case "create":
                createProcessor.process(parts);
                break;
            case "deposit":
                depositProcessor.process(parts);
                break;
            case "pass":
                passProcessor.process(parts);
                break;
            case "withdraw":
                withdrawProcessor.process(parts);
                break;
            case "transfer":
                transferProcessor.process(parts);
                break;
        }
    }
}
