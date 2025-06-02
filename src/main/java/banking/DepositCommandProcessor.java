package banking;

public class DepositCommandProcessor {
    private final Bank bank;

    public DepositCommandProcessor(Bank bank) {
        this.bank = bank;
    }

    public void process(String[] parts) {
        int id = Integer.parseInt(parts[1]);
        double amount = Double.parseDouble(parts[2]);

        Account account = bank.retrieveAccount(id);

        if (account instanceof CheckingAccount || account instanceof SavingsAccount) {
            account.deposit(amount);
        }
    }
}
