package banking;

import java.util.Arrays;
import java.util.List;

public class CreateCommandValidator {
    private final Bank bank;

    public CreateCommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean isValid(String[] commandParsed) {
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

    private boolean isValidDecimal(String input) {
        return input.matches("\\d+(\\.\\d{1,2})?");
    }
}

