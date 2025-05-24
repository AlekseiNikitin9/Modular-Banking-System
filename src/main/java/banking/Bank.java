import java.util.HashMap;
import java.util.Map;

public class Bank {

    private Map<Integer, Account> accounts = new HashMap<>();

    public int getNumberOfAccounts() {
        return accounts.size();
    }

    public void addAccount(Account account) {
        accounts.put(account.getId(), account);
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
}
