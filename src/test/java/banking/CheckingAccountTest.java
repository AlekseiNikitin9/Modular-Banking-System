package banking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckingAccountTest {

    @Test
    public void when_checking_created_balance_is_0() {
        CheckingAccount checkingAccount = new CheckingAccount(11111111, 9.9);
        assertEquals(checkingAccount.getBalance(), 0);
    }

}
