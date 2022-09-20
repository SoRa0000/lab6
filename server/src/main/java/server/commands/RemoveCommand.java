package server.commands;

import common.exceptions.IncorrectCommandInputException;
import server.utilities.CollectionManager;
import server.utilities.CommandManager;
import server.utilities.ResponseOutputer;

/**
 * Concrete command, Remove by key
 */
public class RemoveCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public RemoveCommand(CollectionManager collectionManager) {
        super("remove_key", "remove_key null : удалить элемент из коллекции по его ключу");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg) {
        try {
            if (!(arg.length == 2)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.remove(arg[1]);
        } catch (IncorrectCommandInputException err) {
            ResponseOutputer.append("Использование: " + getDescription());
        }
    }
}
