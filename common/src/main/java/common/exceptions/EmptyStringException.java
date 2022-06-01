package common.exceptions;

/**
 * Throw, when input is empty
 */
public class EmptyStringException extends RuntimeException{
    private final String errorMessage;
    public EmptyStringException(String errorMessage){
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "EmptyStringException{" +
                "errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
