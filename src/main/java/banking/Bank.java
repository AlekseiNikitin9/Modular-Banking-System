package banking;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {

    private Map<Integer, Account> accounts;
    private final List<Integer> sequence;

    public Bank() {
        this.sequence = new ArrayList<>();
        this.accounts = new HashMap<>();
    }

    public int getNumberOfAccounts() {
        return accounts.size();
    }

    public void addAccount(Account account) {
        accounts.put(account.getId(), account);
        sequence.add(account.getId());
    }

    public Account retrieveAccount(int id) {
        return accounts.get(id);
    }

    public void depositById(int id, double amount) {
        retrieveAccount(id).deposit(amount);
    }

    public void withdrawById(int id, double amount) {
        retrieveAccount(id).withdraw(amount);
    }

    public Map<Integer, Account> retrieveAllAccounts() {
        return accounts;
    }

    public void removeAccount(int id) {
        accounts.remove(id);
    }

    public List<String> formattedAccDetails() {
        List<String> formattedDetails = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);

        // Iterate over creation order to maintain the order of creation
        for (Integer accountId : sequence) {
            Account account = accounts.get(accountId);

            // Skip if account doesn't exist (i.e., account is null)
            if (account == null) {
                continue;
            }

            String accountType = account.accountType();
            int id = account.getId();
            String balance = decimalFormat.format(account.getBalance());
            String apr = decimalFormat.format(account.getApr());
            String accountState = accountType + " " + id + " " + balance + " " + apr;
            formattedDetails.add(accountState);

            // Capitalize the first letter of each item in the transaction history
            List<String> transactionHistory = account.getTransactions();
            for (int i = 0; i < transactionHistory.size(); i++) {
                String transaction = transactionHistory.get(i);
                if (transaction != null && !transaction.isEmpty()) {
                    // Capitalize the first letter of each transaction
                    transactionHistory.set(i, transaction.substring(0, 1).toUpperCase() + transaction.substring(1));
                }
            }

            // Add the formatted transaction history
            formattedDetails.addAll(transactionHistory);
        }

        return formattedDetails;
    }

}
