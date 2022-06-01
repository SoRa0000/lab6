package server.commands;

import common.exceptions.IncorrectCommandInputException;
import server.utilities.CollectionManager;
import server.utilities.CommandManager;
import java.io.IOException;

/**
 * Concrete command, Exit program without saving
 */
public class ExitCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public ExitCommand(CollectionManager collectionManager) {
        super("exit", "exit : завершить программу клиента (без сохранения в файл)");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg) throws IOException {
        try {
            if (!(arg.length == 1)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.exit();
        } catch (IncorrectCommandInputException err) {
            System.out.println(err.getMessage());
            System.out.println("Использование: " + getDescription());
        }
    }
}