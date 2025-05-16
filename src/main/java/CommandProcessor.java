public class CommandProcessor {
    private Bank bank;

    public CommandProcessor(Bank bank) {
        this.bank = bank;
    }

    public void process(String command) {
        String[] parts = command.toLowerCase().split(" ");
        String action = parts[0];

        if (action.equals("create")) {
            String type = parts[1];
            int id = Integer.parseInt(parts[2]);
            double apr = Double.parseDouble(parts[3]);

            switch (type) {
                case "checking":
                    bank.addAccount(new CheckingAccount(id, apr));
                    break;
                case "savings":
                    bank.addAccount(new SavingsAccount(id, apr));
                    break;
                case "cd":
                    double balance = Double.parseDouble(parts[4]);
                    bank.addAccount(new CDAccount(id, apr, balance));
                    break;
            }

        } else if (action.equals("deposit")) {
            int id = Integer.parseInt(parts[1]);
            double amount = Double.parseDouble(parts[2]);
            bank.depositById(id, amount);
        }
    }
}