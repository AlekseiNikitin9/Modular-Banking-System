package banking;

public class TransferCommandValidator {
    private final Bank bank;

    public TransferCommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean isValid(String[] commandParsed) {
        boolean valid = true;

        // 1) Must have exactly 4 parts
        if (commandParsed.length != 4) {
            valid = false;
        } else {
            String senderIdStr   = commandParsed[1];
            String receiverIdStr = commandParsed[2];
            String amountStr     = commandParsed[3];

            // 2) IDs must be 8-digit and exist, amount must be decimal
            if (!isValidId(senderIdStr)
                    || !isValidId(receiverIdStr)
                    || !isValidDecimal(amountStr)) {
                valid = false;
            } else {
                int senderId   = Integer.parseInt(senderIdStr);
                int receiverId = Integer.parseInt(receiverIdStr);
                double requested = Double.parseDouble(amountStr);

                // 3) Non‐negative and not to self
                if (requested < 0 || senderId == receiverId) {
                    valid = false;
                } else {
                    Account sender   = bank.retrieveAccount(senderId);
                    Account receiver = bank.retrieveAccount(receiverId);

                    // 4) Neither can be a CD account
                    if (sender.accountType().equals("Cd")
                            || receiver.accountType().equals("Cd")) {
                        valid = false;
                    }
                    // 5) Savings sender cap of $1000
                    else if (sender.accountType().equals("Savings")
                            && requested > 1000) {
                        valid = false;
                    } else {
                        // 6) Only transfer up to balance
                        double actual = Math.min(requested, sender.getBalance());

                        // 7) Further per‐type checks
                        if (sender instanceof SavingsAccount) {
                            valid = validateSavingsTransfer((SavingsAccount) sender, actual);
                        } else if (sender instanceof CheckingAccount) {
                            valid = (actual <= 400);
                        } else {
                            valid = false;
                        }
                    }
                }
            }
        }

        return valid;
    }

    private boolean validateSavingsTransfer(SavingsAccount account, double amount) {
        return amount <= 1000 && !account.getWithdrawedThisMonth();
    }

    private boolean isValidId(String id) {
        return id.matches("\\d{8}")
                && bank.retrieveAllAccounts().containsKey(Integer.parseInt(id));
    }

    private boolean isValidDecimal(String input) {
        return input.matches("\\d+(\\.\\d{1,2})?");
    }
}
