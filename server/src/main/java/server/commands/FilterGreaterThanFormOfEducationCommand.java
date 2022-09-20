package server.commands;

import common.exceptions.IncorrectCommandInputException;
import server.utilities.CollectionManager;
import server.utilities.CommandManager;
import server.utilities.ResponseOutputer;

import java.io.IOException;

/**
 * Concrete command, Filter by form of education
 */
public class FilterGreaterThanFormOfEducationCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public FilterGreaterThanFormOfEducationCommand(CollectionManager collectionManager) {
        super("filter_greater_than_form_of_education", "filter_greater_than_form_of_education formOfEducation : вывести элементы, значение поля formOfEducation которых больше заданного");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arg) throws IOException {
        try {
            if (!(arg.length == 2)) throw new IncorrectCommandInputException();
            CommandManager.addToHistory(getName());
            collectionManager.filter_greater_than_form_of_education(arg[1]);
        } catch (IncorrectCommandInputException err) {
            ResponseOutputer.append("Использование: " + getDescription());
        }
    }
}
