package banking;

public class SavingsAccount extends Account {
    private boolean withdrawedThisMonth;

    public SavingsAccount(int id, double apr) {
        super(id, apr);
        this.withdrawedThisMonth = false;
    }

    public boolean getWithdrawedThisMonth() {
        return withdrawedThisMonth;
    };

    public void flipStatus() {
        withdrawedThisMonth = !withdrawedThisMonth;
    }

    public void resetStatus() {
        withdrawedThisMonth = false;
    }

    @Override
    public String accountType() {
        return "Savings";
    }

}
