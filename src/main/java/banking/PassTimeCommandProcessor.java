package banking;

import java.util.Iterator;
import java.util.Map;

public class PassTimeCommandProcessor {

    private final Bank bank;

    public PassTimeCommandProcessor(Bank bank) {
        this.bank = bank;
    }

    public void process(String[] commandSeparated) {
        int months = Integer.parseInt(commandSeparated[1]);

        for (int i = 0; i < months; i++) {
            processOneMonthPass();
        }
    }

    private void processOneMonthPass() {
        Iterator<Map.Entry<Integer, Account>> iterator = bank.retrieveAllAccounts().entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, Account> entry = iterator.next();
            Account account = entry.getValue();

            account.incrementAge(1);

            String accountType = account.accountType();

            if (accountType.equals("Savings")) {
                ((SavingsAccount) account).resetStatus();
            }

            if (account.getBalance() == 0) {
                iterator.remove();
                continue;
            }
            if (account.getBalance() < 100) {
                account.withdraw(25);
                continue;
            }
            if (accountType.equals("Cd")) {
                for (int i = 0; i < 4; i++) {
                    account.addApr();
                }
            } else {
                account.addApr();
            }
        }
    }
}

