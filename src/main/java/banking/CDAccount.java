public class CDAccount extends Account {
    public CDAccount(int id, double apr, double balance) {
        super(id, apr, balance);
    }

    @Override
    public String accountType() {
        return "cd";
    }
}
