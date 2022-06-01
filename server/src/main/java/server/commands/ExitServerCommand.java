package server.commands;

import server.utilities.CollectionManager;

public class ExitServerCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    public ExitServerCommand(CollectionManager collectionManager) {
        super("server_exit", "exit_server : завершить программу сервер (без сохранения в файл)\"");
        this.collectionManager = collectionManager;
    }
}
