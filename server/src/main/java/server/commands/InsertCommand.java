package server.commands;

import common.exceptions.IncorrectCommandInputException;
import common.exceptions.IncorrectInputScriptException;
import common.interaction.StudyGroupRaw;
import server.utilities.CollectionManager;
import server.utilities.CommandManager;
import server.utilities.ResponseOutputer;

import java.io.IOException;

/**
 * Concrete command, Insert new element
 */
public class InsertCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public InsertCommand(CollectionManager collectionManager) {
        super("insert", "insert null {element} : Добавить новый элемент с заданным ключом");
        this.collectionManager = collectionManager;

    }

    public void execute(String[] arg, StudyGroupRaw studyGroupRaw) {
        try {
            if (!(arg.length == 2)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.insert(arg[1],studyGroupRaw);
        } catch (IncorrectCommandInputException err) {
            ResponseOutputer.append("Использование: " + getDescription());
        } catch (IncorrectInputScriptException err) {
            throw new IncorrectInputScriptException();
        }
    }
}
