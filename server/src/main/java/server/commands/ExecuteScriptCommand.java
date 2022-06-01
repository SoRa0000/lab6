package server.commands;

import common.exceptions.IncorrectCommandInputException;
import server.utilities.CollectionManager;
import server.utilities.CommandManager;

/**
 * Concrete command, Execute script
 */
public class ExecuteScriptCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public ExecuteScriptCommand(CollectionManager collectionManager) {
        super("execute_script", "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg) {
        try {
            if (!(arg.length == 2)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.executeScript(arg[1]);
        } catch (IncorrectCommandInputException err) {
            System.out.println(err.getMessage());
            System.out.println("Использование: " + getDescription());
        }
    }
}
