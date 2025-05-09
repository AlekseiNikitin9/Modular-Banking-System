public abstract class Account {

    private final int id;
    private final double apr;
    private double balance;

    protected Account(int id, double apr, double balance) {
        this.id = id;
        this.apr = apr;
        this.balance = balance;
    }

    protected Account(int id, double apr) {
        this.id = id;
        this.apr = apr;
        this.balance = 0;
    }

    public int getId() {
        return id;
    }

    public double getApr() {
        return apr;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount > balance) {
            balance = 0;
        } else {
            balance -= amount;
        }
    }

    public abstract String accountType();
}
