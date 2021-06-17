package exceptions;

/**
 * Sending negative value of money to deposit method in Client.class
 * will throw the NegativeDepositException
 * @author Nickolay Nickolskiy
 */
public class NegativeDepositAmount extends Exception {
    public NegativeDepositAmount(String message) {
        super(message);
    }
}
