package common.exceptions;

/**
 * Throw, when incorrect command
 */
public class IncorrectCommandInputException extends RuntimeException{
    public IncorrectCommandInputException(){
        super("Ввод команды не правильно");

    }
    @Override
    public String toString() {
        String errorMessage = "Ввод команды не правильно";
        return "NotInDeclaredLimitsException{" +
                "errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
