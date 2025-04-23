import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SavingsAccountTest {

    @Test
    public void when_checking_created_balance_is_0() {
        SavingsAccount savingsAccount = new SavingsAccount(22222222, 7.4);
        assertEquals(savingsAccount.getBalance(), 0);
    }

}