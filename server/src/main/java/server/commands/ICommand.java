package server.commands;

import common.interaction.StudyGroupRaw;

import java.io.IOException;

/**
 * Interface for all server.commands
 */
public interface ICommand {
    String getDescription();

    String getName();

    void execute(String[] arg) throws IOException;
    void execute(String[] arg, StudyGroupRaw studyGroupRaw);
}
