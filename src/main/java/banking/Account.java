package banking;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {

    private final int id;
    private final double apr;
    private double balance;
    private int accountAge;
    private List<String> transactions;

    protected Account(int id, double apr, double balance) {
        this.id = id;
        this.apr = apr;
        this.balance = balance;
        this.accountAge = 0;
        this.transactions = new ArrayList<>();
    }

    protected Account(int id, double apr) {
        this.id = id;
        this.apr = apr;
        this.balance = 0;
        this.accountAge = 0;
        this.transactions = new ArrayList<>();
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
        if (amount >= balance) {
            balance = 0;
        } else {
            balance -= amount;
        }
    }

    public int getAccountAge() {
        return accountAge;
    }

    public void incrementAge(int months) {
        accountAge += months;
    }

    public void addApr() {
        double monthlyRate = (apr / 100) / 12;
        balance += balance * monthlyRate;
        balance = Double.parseDouble(new DecimalFormat("0.00").format(balance));

    }

    public void logTransaction(String transaction) {
        transactions.add(transaction);
    }


    public List<String> getTransactions() {
        return transactions;
    }
    public abstract String accountType();
}
