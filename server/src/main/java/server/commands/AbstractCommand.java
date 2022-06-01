package server.commands;

import common.interaction.StudyGroupRaw;

import java.io.IOException;

/**
 * Abstract Command class contains Object methods, name and description.
 */
public abstract class AbstractCommand implements ICommand {
    private String name;
    private String description;

    public AbstractCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public void execute(String[] arg) throws IOException {
    }
    public void execute(String[] arg, StudyGroupRaw studyGroupRaw){
    }
}
