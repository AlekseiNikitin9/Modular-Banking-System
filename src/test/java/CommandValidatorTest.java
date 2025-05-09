import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandValidatorTest {
    private Bank bank;
    private CommandValidator validator;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        bank.addAccount(new CheckingAccount(12345678, 0.5));
        bank.addAccount(new SavingsAccount(87654321, 2.0));
        bank.addAccount(new CDAccount(11111111, 3.0, 2000));
        validator = new CommandValidator(bank);
    }

    @Test
    void validCheckingCreate() {
        assertTrue(validator.isValid("Create checking 99999999 0.01"));
    }

    @Test
    void validSavingsCreate() {
        assertTrue(validator.isValid("Create savings 88888888 0.6"));
    }

    @Test
    void validCDCreate() {
        assertTrue(validator.isValid("Create cd 77777777 1.5 2000"));
    }

    @Test
    void invalidAccountType() {
        assertFalse(validator.isValid("Create investment 12345678 0.01"));
    }

    @Test
    void duplicateId() {
        assertFalse(validator.isValid("Create checking 12345678 0.5"));
    }

    @Test
    void idTooShort() {
        assertFalse(validator.isValid("Create checking 1234567 0.01"));
    }

    @Test
    void idTooLong() {
        assertFalse(validator.isValid("Create checking 123456789 0.01"));
    }

    @Test
    void negativeApr() {
        assertFalse(validator.isValid("Create savings 87654321 -0.5"));
    }

    @Test
    void aprOverTen() {
        assertFalse(validator.isValid("Create savings 87654321 10.5"));
    }

    @Test
    void cdBelowMinimum() {
        assertFalse(validator.isValid("Create cd 22223333 1.2 500"));
    }

    @Test
    void cdAboveMaximum() {
        assertFalse(validator.isValid("Create cd 22223333 1.2 20000"));
    }

    @Test
    void missingApr() {
        assertFalse(validator.isValid("Create checking 12345678"));
    }

    @Test
    void cdMissingAmount() {
        assertFalse(validator.isValid("Create cd 12345678 1.2"));
    }

    @Test
    void extraArgument() {
        assertFalse(validator.isValid("Create checking 12345678 0.01 extra"));
    }

    @Test
    void caseInsensitiveCommand() {
        assertTrue(validator.isValid("cReaTe sAvInGs 11224433 0.4"));
    }

    @Test
    void nonNumericId1() {
        assertFalse(validator.isValid("Create checking 1234abcd 0.01"));
    }

    @Test
    void nonNumericId2() {
        assertFalse(validator.isValid("Create savings abcd1234 0.6"));
    }

    @Test
    void missingAccountType() {
        assertFalse(validator.isValid("Create 12345678 0.6"));
    }

    @Test
    void missingId() {
        assertFalse(validator.isValid("Create savings 0.6"));
    }

    @Test
    void aprNotANumber() {
        assertFalse(validator.isValid("Create checking 12345678 APR"));
    }

    @Test
    void amountNotANumberForCD() {
        assertFalse(validator.isValid("Create cd 12345678 1.5 amount"));
    }

    @Test
    void multipleDecimalPointsInApr() {
        assertFalse(validator.isValid("Create checking 12345678 0.5.3"));
    }

    @Test
    void specialCharactersInCDAmount() {
        assertFalse(validator.isValid("Create cd 12345678 1.5 $2000"));
    }

    @Test
    void cdMinValidAprAndAmount() {
        assertTrue(validator.isValid("Create cd 55555555 0.01 1000"));
    }

    @Test
    void cdMaxValidAprAndAmount() {
        assertTrue(validator.isValid("Create cd 66666666 10 10000"));
    }

    @Test
    void noSpacesCommand() {
        assertFalse(validator.isValid("createchecking123456780.5"));
    }

    @Test
    void floatingPointAccountId() {
        assertFalse(validator.isValid("create checking 1234.5678 1.7"));
    }

    @Test
    void scientificNotationApr() {
        assertFalse(validator.isValid("Create checking 12345678 1e-2"));
    }

    @Test
    void extraSpacesInMiddle() {
        assertFalse(validator.isValid("Create  checking 55555555 0.6")); // double space
    }

    @Test
    void extraSpaceAtEnd() {
        assertTrue(validator.isValid("Create checking 56565656 0.6 ")); // trailing space
    }

    @Test
    void leadingSpaces() {
        assertFalse(validator.isValid(" Create checking 57575757 0.6")); // leading space
    }

    @Test
    void extraArgumentForCD() {
        assertFalse(validator.isValid("Create cd 58585858 1.2 2000 extra")); // too many args
    }

    @Test
    void negativeCDDepositAmount() {
        assertFalse(validator.isValid("Create cd 59595959 1.2 -2000")); // negative deposit
    }

    @Test
    void zeroApr() {
        assertTrue(validator.isValid("Create checking 60606060 0")); // zero APR is valid
    }

    @Test
    void wholeNumberApr() {
        assertTrue(validator.isValid("Create savings 61616161 5")); // whole number APR valid
    }
    @Test
    void validDepositToChecking() {
        assertTrue(validator.isValid("Deposit 12345678 500")); // checking ≤ 1000
    }

    @Test
    void validDepositToSavings() {
        assertTrue(validator.isValid("Deposit 87654321 2500")); // savings ≤ 2500
    }

    @Test
    void depositAmountZero() {
        assertTrue(validator.isValid("Deposit 12345678 0")); // zero is allowed
    }

    @Test
    void depositNegativeAmount() {
        assertFalse(validator.isValid("Deposit 12345678 -500")); // negative not allowed
    }

    @Test
    void depositAboveLimitChecking() {
        assertFalse(validator.isValid("Deposit 12345678 1500")); // >1000 for checking
    }

    @Test
    void depositAboveLimitSavings() {
        assertFalse(validator.isValid("Deposit 87654321 3000")); // >2500 for savings
    }

    @Test
    void depositToCDAccount() {
        assertFalse(validator.isValid("Deposit 11111111 500")); // CDs cannot receive deposits
    }

    @Test
    void depositToNonexistentAccount() {
        assertFalse(validator.isValid("Deposit 99999999 500")); // no such account
    }

    @Test
    void depositInvalidIdFormat() {
        assertFalse(validator.isValid("Deposit abc12345 500")); // non-numeric ID
    }

    @Test
    void depositInvalidAmountFormat() {
        assertFalse(validator.isValid("Deposit 12345678 fivehundred")); // amount not numeric
    }

    @Test
    void depositTooManyArguments() {
        assertFalse(validator.isValid("Deposit 12345678 500 extra")); // too many parts
    }

    @Test
    void depositTooFewArguments() {
        assertFalse(validator.isValid("Deposit 12345678")); // missing amount
    }

    @Test
    void depositWithDecimalAmountChecking() {
        assertTrue(validator.isValid("Deposit 12345678 999.99")); // valid decimal under limit
    }

    @Test
    void depositWithDecimalAmountSavings() {
        assertTrue(validator.isValid("Deposit 87654321 2499.99")); // valid decimal under limit
    }

    @Test
    void depositOverDecimalLimitChecking() {
        assertFalse(validator.isValid("Deposit 12345678 1000.01")); // just over limit
    }

    @Test
    void depositOverDecimalLimitSavings() {
        assertFalse(validator.isValid("Deposit 87654321 2500.01")); // just over limit
    }

    @Test
    void depositWithScientificNotation() {
        assertFalse(validator.isValid("Deposit 12345678 1e2")); // scientific notation not allowed
    }


}
