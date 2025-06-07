package banking;

public class TransferCommandValidator {
    private final Bank bank;

    public TransferCommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean isValid(String[] commandParsed) {
        if (commandParsed.length != 4) return false;

        String senderIdStr = commandParsed[1];
        String receiverIdStr = commandParsed[2];
        String amountStr = commandParsed[3];

        if (!isValidId(senderIdStr) || !isValidId(receiverIdStr)) return false;
        if (!isValidDecimal(amountStr)) return false;

        int senderId = Integer.parseInt(senderIdStr);
        int receiverId = Integer.parseInt(receiverIdStr);
        double requested = Double.parseDouble(amountStr);

        if (requested < 0) return false;
        if (senderId == receiverId) return false;

        Account sender = bank.retrieveAccount(senderId);
        Account receiver = bank.retrieveAccount(receiverId);

        if (sender.accountType().equals("Cd") || receiver.accountType().equals("Cd")) return false;
        if (sender.accountType().equals("Savings") && requested > 1000) return false;
        // Determine the actual amount transferable based on current balance
        double actual = Math.min(requested, sender.getBalance());

        if (sender instanceof SavingsAccount) {
            return validateSavingsTransfer((SavingsAccount) sender, actual);
        } else if (sender instanceof CheckingAccount) {
            return actual <= 400;
        }

        return false;
    }

    private boolean validateSavingsTransfer(SavingsAccount account, double amount) {
        return amount <= 1000 && !account.getWithdrawedThisMonth();
    }

    private boolean isValidId(String id) {
        return id.matches("\\d{8}") && bank.retrieveAllAccounts().containsKey(Integer.parseInt(id));
    }

    private boolean isValidDecimal(String input) {
        return input.matches("\\d+(\\.\\d{1,2})?");
    }
}
