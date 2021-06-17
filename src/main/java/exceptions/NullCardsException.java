package exceptions;

/**
 * Custom Exception will occur if client does not have any cards
 */
public class NullCardsException extends Exception {
    public NullCardsException(String message) {
        super(message);
    }
}
