package banking;

public class TransferCommandProcessor {
    private final Bank bank;

    public TransferCommandProcessor(Bank bank) {
        this.bank = bank;
    }

    public void process(String[] commandParts) {
        int senderId = Integer.parseInt(commandParts[1]);
        int receiverId = Integer.parseInt(commandParts[2]);
        double requestedAmount = Double.parseDouble(commandParts[3]);

        Account sender = bank.retrieveAccount(senderId);
        Account receiver = bank.retrieveAccount(receiverId);

        // Determine the actual amount that can be withdrawn
        double available = sender.getBalance();
        double actualAmount = Math.min(requestedAmount, available);

        // Withdraw the requested amount (over-withdrawal zeros the sender safely)
        sender.withdraw(requestedAmount);
        // Deposit only the amount actually withdrawn
        receiver.deposit(actualAmount);

        // Log the transfer under both accounts
        sender.logTransaction(String.join(" ", commandParts));
        receiver.logTransaction(String.join(" ", commandParts));
    }
}