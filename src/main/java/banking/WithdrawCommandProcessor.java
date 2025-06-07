package banking;

public class WithdrawCommandProcessor {
    private final Bank bank;

    public WithdrawCommandProcessor(Bank bank) {
        this.bank = bank;
    }

    public void process(String[] commandSeparated) {
        int id = Integer.parseInt(commandSeparated[1]);
        double amount = Double.parseDouble(commandSeparated[2]);
        bank.retrieveAccount(id).withdraw(amount);
        bank.retrieveAccount(id).logTransaction(String.join(" ", commandSeparated));
    }
}