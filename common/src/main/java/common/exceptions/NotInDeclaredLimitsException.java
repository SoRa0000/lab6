package common.exceptions;

/**
 * Throw, when number is bigger than limit
 */
public class NotInDeclaredLimitsException extends RuntimeException{
    private final String errorMessage;
    public NotInDeclaredLimitsException(String errorMessage){
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "NotInDeclaredLimitsException{" +
                "errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
