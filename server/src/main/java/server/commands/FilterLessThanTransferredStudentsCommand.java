package server.commands;

import common.exceptions.IncorrectCommandInputException;
import server.utilities.CollectionManager;
import server.utilities.CommandManager;
import server.utilities.ResponseOutputer;

import java.io.IOException;
/**
 * Concrete command, Number of transferred students
 */
public class FilterLessThanTransferredStudentsCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    public FilterLessThanTransferredStudentsCommand(CollectionManager collectionManager){
        super("filter_less_than_transferred_students","filter_less_than_transferred_students transferredStudents : вывести элементы, значение поля transferredStudents которых меньше заданного");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg) throws IOException {
        try{
            if(!(arg.length == 2)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.filter_less_than_transferred_students(arg[1]);
        }
        catch (IncorrectCommandInputException err){
            ResponseOutputer.append("Использование: " + getDescription());
        }
    }
}
