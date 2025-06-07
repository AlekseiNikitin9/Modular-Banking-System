package banking;

public class DepositCommandValidator {
    private final Bank bank;

    public DepositCommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean isValid(String[] commandParsed) {
        boolean valid = true;

        // 1) Must have exactly 3 parts
        if (commandParsed.length != 3) {
            valid = false;
        } else {
            String id        = commandParsed[1];
            String amountStr = commandParsed[2];

            // 2) ID must be 8 digits
            if (!id.matches("\\d{8}")) {
                valid = false;
            } else {
                int numericId = Integer.parseInt(id);
                Account account = bank.retrieveAccount(numericId);

                // 3) Account must exist
                if (account == null) {
                    valid = false;
                } else if (!isValidDecimal(amountStr)) {
                    // 4) Amount must be decimal
                    valid = false;
                } else {
                    double amount = Double.parseDouble(amountStr);

                    // 5) Non-negative
                    if (amount < 0) {
                        valid = false;
                    } else {
                        String accountType = account.accountType();

                        // 6) Type-specific caps and restrictions
                        if (accountType.equals("Cd")) {
                            valid = false;
                        } else if (accountType.equals("Checking") && amount > 1000) {
                            valid = false;
                        } else if (accountType.equals("Savings") && amount > 2500) {
                            valid = false;
                        }
                    }
                }
            }
        }

        return valid;
    }

    private boolean isValidDecimal(String input) {
        return input.matches("\\d+(\\.\\d{1,2})?");
    }
}
