package server.commands;

import common.exceptions.IncorrectCommandInputException;
import server.utilities.CollectionManager;
import server.utilities.CommandManager;

/**
 * Concrete command, Show collection
 */
public class ShowCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("show", "show : Вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg) {
        try {
            if (!(arg.length == 1)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.show();
        } catch (IncorrectCommandInputException err) {
            System.out.println(err.getMessage());
            System.out.println("Использование: " + getDescription());
        }
    }
}
