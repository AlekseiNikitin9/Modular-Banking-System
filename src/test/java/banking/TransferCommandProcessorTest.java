package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransferCommandProcessorTest {
    private static final int SENDER_ID = 11111111;
    private static final int RECEIVER_ID = 22222222;
    private static final double APR = 1.0;
    private static final double INITIAL_BALANCE = 1000;

    private Bank bank;
    private CommandProcessor processor;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        processor = new CommandProcessor(bank);
    }

    @Test
    void transfer_from_checking_to_checking_reduces_and_increases_balances_correctly() {
        bank.addAccount(new CheckingAccount(SENDER_ID, APR));
        bank.depositById(SENDER_ID, INITIAL_BALANCE);
        bank.addAccount(new CheckingAccount(RECEIVER_ID, APR));

        processor.process("transfer 11111111 22222222 400");

        assertEquals(600, bank.retrieveAccount(SENDER_ID).getBalance());
        assertEquals(400, bank.retrieveAccount(RECEIVER_ID).getBalance());
    }

    @Test
    void transfer_from_checking_exceeding_balance_transfers_all_available_funds() {
        bank.addAccount(new CheckingAccount(SENDER_ID, APR));
        bank.depositById(SENDER_ID, 250);
        bank.addAccount(new CheckingAccount(RECEIVER_ID, APR));

        processor.process("transfer 11111111 22222222 400");

        assertEquals(0, bank.retrieveAccount(SENDER_ID).getBalance());
        assertEquals(250, bank.retrieveAccount(RECEIVER_ID).getBalance());
    }

    @Test
    void transfer_from_savings_to_checking_reduces_sender_balance_and_updates_receiver() {
        bank.addAccount(new SavingsAccount(SENDER_ID, APR));
        bank.depositById(SENDER_ID, INITIAL_BALANCE);
        bank.addAccount(new CheckingAccount(RECEIVER_ID, APR));

        processor.process("transfer 11111111 22222222 1000");

        assertEquals(0, bank.retrieveAccount(SENDER_ID).getBalance());
        assertEquals(1000, bank.retrieveAccount(RECEIVER_ID).getBalance());
    }

    @Test
    void transfer_from_savings_to_savings_is_processed_correctly() {
        bank.addAccount(new SavingsAccount(SENDER_ID, APR));
        bank.depositById(SENDER_ID, 500);
        bank.addAccount(new SavingsAccount(RECEIVER_ID, APR));

        processor.process("transfer 11111111 22222222 500");

        assertEquals(0, bank.retrieveAccount(SENDER_ID).getBalance());
        assertEquals(500, bank.retrieveAccount(RECEIVER_ID).getBalance());
    }

    @Test
    void multiple_transfers_from_checking_to_checking_are_cumulative() {
        bank.addAccount(new CheckingAccount(SENDER_ID, APR));
        bank.depositById(SENDER_ID, 1000);
        bank.addAccount(new CheckingAccount(RECEIVER_ID, APR));

        processor.process("transfer 11111111 22222222 100");
        processor.process("transfer 11111111 22222222 200");
        processor.process("transfer 11111111 22222222 100");

        assertEquals(600, bank.retrieveAccount(SENDER_ID).getBalance());
        assertEquals(400, bank.retrieveAccount(RECEIVER_ID).getBalance());
    }

    @Test
    void transfer_from_zero_balance_account_sends_nothing() {
        bank.addAccount(new CheckingAccount(11111111, 1.0));
        bank.addAccount(new CheckingAccount(22222222, 1.0));
        processor.process("transfer 11111111 22222222 500");
        assertEquals(0, bank.retrieveAccount(22222222).getBalance());
    }

}
