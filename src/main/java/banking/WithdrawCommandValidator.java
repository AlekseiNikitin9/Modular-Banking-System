package banking;

public class WithdrawCommandValidator {
    private final Bank bank;

    public WithdrawCommandValidator(Bank bank) {
        this.bank = bank;
    }

    //
    public boolean isValid(String[] command) {
        if (command.length != 3) {
            return false;
        }

        String idStr = command[1];
        String amountStr = command[2];
        int id;
        double amount;

        try {
            id = Integer.parseInt(idStr);
            amount = Double.parseDouble(amountStr);
            if (amount < 0) { return false; }
        } catch (NumberFormatException e) {
            return false;
        }

        Account account = bank.retrieveAccount(id);
        if (account == null) {
            return false;
        } else {
            String accountType = account.accountType();
            switch (accountType) {
                case "Savings":
                    return validateSavings((SavingsAccount) account, amount);
                case "Checking":
                    return validateChecking(amount);
                default:
                    return validateCD(account, amount);
            }
        }
    }

    private boolean validateSavings(SavingsAccount account, double amount) {
        return amount <= 1000 && !account.getWithdrawedThisMonth();
    }

    private boolean validateChecking(double amount) {
        return amount <= 400;
    }

    private boolean validateCD(Account account, double amount) {
        if (account.getAccountAge() < 12) {
            return false;
        }
        return amount >= account.getBalance();
    }
}
