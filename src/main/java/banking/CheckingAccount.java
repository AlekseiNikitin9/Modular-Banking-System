package banking;

public class CheckingAccount extends Account{
    public CheckingAccount(int id, double apr) {
        super(id, apr);
    }

    @Override
    public String accountType() {
        return "checking";
    }
}
