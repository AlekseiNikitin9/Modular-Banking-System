public class SavingsAccount extends Account {
    public SavingsAccount(int id, double apr) {
        super(id, apr);
    }

    @Override
    public String accountType() {
        return "savings";
    }
}
