package server.commands;

import common.exceptions.IncorrectCommandInputException;
import common.interaction.StudyGroupRaw;
import server.utilities.CollectionManager;
import server.utilities.CommandManager;


/**
 * Concrete command, Update collection
 */
public class UpdateCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager) {
        super("update", "update id {element} : обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = collectionManager;
    }

    public void execute(String[] arg, StudyGroupRaw studyGroupRaw) {
        try {
            if (!(arg.length == 2)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.update(arg[1],studyGroupRaw);
        } catch (IncorrectCommandInputException err) {
            System.out.println(err.getMessage());
            System.out.println("Использование: " + getDescription());
        }
    }
}
