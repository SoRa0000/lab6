package common.exceptions;

/**
 * Throw, when length of Passport ID is out of limit
 **/
public class IncorrectPassportID extends RuntimeException{
    private final String errorMessage;
    public IncorrectPassportID(String errorMessage){
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "PassportIdLengthOutOfLimitException{" +
                "errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
