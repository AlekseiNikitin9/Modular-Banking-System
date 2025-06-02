package banking;

public class CreateCommandProcessor {
    private final Bank bank;

    public CreateCommandProcessor(Bank bank) {
        this.bank = bank;
    }

    public void process(String[] parts) {
        String id = parts[2];
        double apr = Double.parseDouble(parts[3]);

        switch (parts[1]) {
            case "checking":
                bank.addAccount(new CheckingAccount(Integer.parseInt(id), apr));
                break;
            case "savings":
                bank.addAccount(new SavingsAccount(Integer.parseInt(id), apr));
                break;
            case "cd":
                double balance = Double.parseDouble(parts[4]);
                bank.addAccount(new CDAccount(Integer.parseInt(id), apr, balance));
                break;
        }
    }
}
