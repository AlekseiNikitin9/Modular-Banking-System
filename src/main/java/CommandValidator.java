import java.util.Arrays;
import java.util.List;

public class CommandValidator {
    private Bank bank;

    public CommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean isValid(String command) {
        if (command == null || command.isEmpty()) return false;

        String[] commandParsed = command.toLowerCase().split(" ");
        if (commandParsed.length == 0) return false;

        String function = commandParsed[0];
        switch (function) {
            case "create":
                return createIsValid(commandParsed);
            case "deposit":
                return depositIsValid(commandParsed);
            default:
                return false;
        }
    }

    private boolean createIsValid(String[] commandParsed) {
        List<String> accountTypes = Arrays.asList("cd", "checking", "savings");

        if (commandParsed.length < 4) return false;

        String accountType = commandParsed[1];
        String id = commandParsed[2];
        String apr = commandParsed[3];

        if (!accountTypes.contains(accountType)) return false;
        if (!id.matches("\\d{8}")) return false;

        int numericId = Integer.parseInt(id);
        if (bank.retrieveAccount(numericId) != null) return false;

        if (!isValidDecimal(apr)) return false;

        double aprValue = Double.parseDouble(apr);
        if (aprValue < 0 || aprValue > 10) return false;

        if (accountType.equals("cd")) {
            if (commandParsed.length != 5) return false;

            String depositAmountStr = commandParsed[4];
            if (!isValidDecimal(depositAmountStr)) return false;

            double depositAmount = Double.parseDouble(depositAmountStr);
            if (depositAmount < 1000 || depositAmount > 10000) return false;
        } else {
            if (commandParsed.length != 4) return false;
        }

        return true;
    }

    private boolean depositIsValid(String[] commandParsed) {
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

        if (accountType.equals("cd")) return false;
        if (accountType.equals("checking") && amount > 1000) return false;
        if (accountType.equals("savings") && amount > 2500) return false;

        return true;
    }

    // Only allows up to 2 decimal places, no scientific notation, no symbols
    private boolean isValidDecimal(String input) {
        return input.matches("\\d+(\\.\\d{1,2})?");
    }
}
