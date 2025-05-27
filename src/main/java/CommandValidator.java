public class CommandValidator {
    private final CreateCommandValidator createValidator;
    private final DepositCommandValidator depositValidator;

    public CommandValidator(Bank bank) {
        this.createValidator = new CreateCommandValidator(bank);
        this.depositValidator = new DepositCommandValidator(bank);
    }

    public boolean isValid(String command) {
        if (command == null || command.isEmpty()) return false;
        String[] parts = command.trim().toLowerCase().split("\\s+");
        if (parts.length == 0) return false;

        switch (parts[0]) {
            case "create":
                return createValidator.isValid(parts);
            case "deposit":
                return depositValidator.isValid(parts);
            default:
                return false;
        }
    }
}
