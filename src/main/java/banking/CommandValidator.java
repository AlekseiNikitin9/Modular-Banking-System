package banking;

import banking.CreateCommandValidator;
import banking.DepositCommandValidator;

public class CommandValidator {
    private final CreateCommandValidator createValidator;
    private final DepositCommandValidator depositValidator;
    private final PassTimeCommandValidator passValidator;
    private final WithdrawCommandValidator withdrawValidator;
    private final TransferCommandValidator transferValidator;

    public CommandValidator(Bank bank) {
        this.createValidator = new CreateCommandValidator(bank);
        this.depositValidator = new DepositCommandValidator(bank);
        this.passValidator = new PassTimeCommandValidator();
        this.withdrawValidator = new WithdrawCommandValidator(bank);
        this.transferValidator = new TransferCommandValidator(bank);
    }

    public boolean isValid(String command) {
        if (command == null || command.isEmpty()) return false;
        String[] parts = command.toLowerCase().split(" ");
        if (parts.length > 5 || parts.length < 2) return false;

        switch (parts[0]) {
            case "create":
                return createValidator.isValid(parts);
            case "deposit":
                return depositValidator.isValid(parts);
            case "pass":
                return passValidator.isValid(parts);
            case "withdraw":
                return withdrawValidator.isValid(parts);
            case "transfer":
                return transferValidator.isValid(parts);
            default:
                return false;
        }
    }
}
