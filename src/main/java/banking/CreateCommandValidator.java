package banking;

import java.util.Arrays;
import java.util.List;

public class CreateCommandValidator {
    private final Bank bank;

    public CreateCommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean isValid(String[] commandParsed) {
        boolean valid = true;
        List<String> accountTypes = Arrays.asList("cd", "checking", "savings");

        // 1) Must have at least 4 parts
        if (commandParsed.length < 4) {
            valid = false;
        } else {
            String accountType     = commandParsed[1];
            String id              = commandParsed[2];
            String apr             = commandParsed[3];

            // 2) Must be a recognized type
            if (!accountTypes.contains(accountType)) {
                valid = false;
            }
            // 3) ID must be 8 digits
            else if (!id.matches("\\d{8}")) {
                valid = false;
            } else {
                int numericId = Integer.parseInt(id);
                // 4) ID must not already exist
                if (bank.retrieveAccount(numericId) != null) {
                    valid = false;
                }
                // 5) APR must be decimal
                else if (!isValidDecimal(apr)) {
                    valid = false;
                } else {
                    double aprValue = Double.parseDouble(apr);
                    // 6) APR range check
                    if (aprValue < 0 || aprValue > 10) {
                        valid = false;
                    }
                    // 7) CD has extra deposit amount argument
                    else if (accountType.equals("cd")) {
                        // must have exactly 5 parts
                        if (commandParsed.length != 5) {
                            valid = false;
                        } else {
                            String depositAmountStr = commandParsed[4];
                            // deposit must be decimal
                            if (!isValidDecimal(depositAmountStr)) {
                                valid = false;
                            } else {
                                double depositAmount = Double.parseDouble(depositAmountStr);
                                // deposit range
                                if (depositAmount < 1000 || depositAmount > 10000) {
                                    valid = false;
                                }
                            }
                        }
                    }
                    // non-CD must have exactly 4 parts
                    else {
                        if (commandParsed.length != 4) {
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
