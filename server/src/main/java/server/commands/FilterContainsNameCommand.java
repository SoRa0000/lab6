package server.commands;

import common.exceptions.IncorrectCommandInputException;
import server.utilities.CollectionManager;
import server.utilities.CommandManager;
import server.utilities.ResponseOutputer;

import java.io.IOException;

/**
 * Concrete command, Filter by name
 */
public class FilterContainsNameCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public FilterContainsNameCommand(CollectionManager collectionManager) {
        super("filter_contains_name", "filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg) throws IOException {
        try {
            if (!(arg.length == 2)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.filter_contains_name(arg[1]);
        } catch (IncorrectCommandInputException err) {
            ResponseOutputer.append("Использование: " + getDescription());
        }
    }
}
