import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class CDAccountTest {
    @Test
    public void apr_is_whatever_was_supplied() {
        double balance = 346.99;
        CDAccount cdAccount = new CDAccount(33333333, 3.4, balance);
        assertEquals(balance, cdAccount.getBalance());
    }
}
