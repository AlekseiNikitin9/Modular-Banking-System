package banking;

public class DepositCommandValidator {
    private final Bank bank;

    public DepositCommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean isValid(String[] commandParsed) {
        if (commandParsed.length != 3) return false;

        String id = commandParsed[1];
        String amountStr = commandParsed[2];

        if (!id.matches("\\d{8}")) return false;

        int numericId = Integer.parseInt(id);
        Account account = bank.retrieveAccount(numericId);
        if (account == null) return false;

        if (!isValidDecimal(amountStr)) return false;

        double amount = Double.parseDouble(amountStr);
        if (amount < 0) return false;

        String accountType = account.accountType();

        if (accountType.equals("Cd")) return false;
        if (accountType.equals("Checking") && amount > 1000) return false;
        if (accountType.equals("Savings") && amount > 2500) return false;

        return true;
    }

    private boolean isValidDecimal(String input) {
        return input.matches("\\d+(\\.\\d{1,2})?");
    }
}
