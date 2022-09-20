package server.commands;

import common.exceptions.IncorrectCommandInputException;
import common.interaction.StudyGroupRaw;
import server.utilities.CollectionManager;
import server.utilities.CommandManager;
import server.utilities.ResponseOutputer;

/**
 * Concrete command, Replace if greater
 */
public class ReplaceIfGreaterCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public ReplaceIfGreaterCommand(CollectionManager collectionManager) {
        super("replace_if_greater", "replace_if_greater null {element} : заменить значение по ключу, если новое значение больше старого");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg, StudyGroupRaw studyGroupRaw) {
        try {
            if (!(arg.length == 2)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.replace_if_greater(arg[1],studyGroupRaw);
        } catch (IncorrectCommandInputException err) {
            ResponseOutputer.append("Использование: " + getDescription());
        }
    }
}
