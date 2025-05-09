public class CommandValidator {
    private Bank bank;

    public CommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean isValid(String command) {
        return true;
    }
}
