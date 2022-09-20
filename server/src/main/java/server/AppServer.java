package server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import common.exceptions.CannotReadFileException;
import server.commands.*;
import server.utilities.CollectionManager;
import server.utilities.CommandManager;
import server.utilities.FileManager;
import server.utilities.RequestHandler;

import java.io.IOException;

public class AppServer {
    public static final int PORT = 4004;
    public static final int CONNECTION_TIMEOUT = 60 * 1000;
    public final Logger LOG  = LoggerFactory.getLogger(AppServer.class);

    public static void main(String[] args) throws IOException, CannotReadFileException {

        if(args.length == 1){
            final String PATH_FILE = args[0];
            FileManager fileManager = new FileManager(PATH_FILE);
            CommandManager commandManager = new CommandManager();
            CollectionManager collectionManager = new CollectionManager(commandManager,fileManager);
            commandManager.addCommand(new ShowCommand(collectionManager));
            commandManager.addCommand(new InsertCommand(collectionManager));
            commandManager.addCommand(new HelpCommand(collectionManager));
            commandManager.addCommand(new HistoryCommand(collectionManager));
            commandManager.addCommand(new ClearCommand(collectionManager));
            commandManager.addCommand(new FilterContainsNameCommand(collectionManager));
            commandManager.addCommand(new FilterGreaterThanFormOfEducationCommand(collectionManager));
            commandManager.addCommand(new FilterLessThanTransferredStudentsCommand(collectionManager));
            commandManager.addCommand(new InfoCommand(collectionManager));
            commandManager.addCommand(new RemoveCommand(collectionManager));
            commandManager.addCommand(new UpdateCommand(collectionManager));
            commandManager.addCommand(new RemoveLowerCommand(collectionManager));
            commandManager.addCommand(new ReplaceIfGreaterCommand(collectionManager));
            commandManager.addCommand(new ExecuteScriptCommand(collectionManager));
            commandManager.addCommand(new ExitCommand(collectionManager));
            RequestHandler requestHandler = new RequestHandler(commandManager);
            Server server = new Server(PORT,CONNECTION_TIMEOUT,requestHandler,collectionManager);
            server.run();

        }
        else if(args.length == 0) System.out.println("Должно вводыть имя файла!");

        else System.out.println("Имя файла неправильно!");


    }
}
