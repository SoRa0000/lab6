package client;

import client.utilities.*;
import common.exceptions.NotInDeclaredLimitsException;
import common.exceptions.WrongAmountOfElementsException;
import java.util.HashMap;
import java.util.Scanner;

public class AppClient {
    public static final int RECONNECTION_TIMEOUT = 5 * 1000;
    public static final int MAX_RECONNECTION_ATTEMPTS = 5;
    public static int port;
    private static boolean initializeConnectionAddress(String[] hostAndPortArgs) {
        try {
            if (hostAndPortArgs.length != 1) throw new WrongAmountOfElementsException();
            port = Integer.parseInt(hostAndPortArgs[0]);
            if (port < 0 || port > 65535) throw new NotInDeclaredLimitsException("порт не находится в диапазоне предела");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            String jarName = new java.io.File(AppClient.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            System.out.println("Использование: 'java -jar " + jarName + "<port>'");
        } catch (NumberFormatException exception) {
            System.out.println("Порт должен быть представлен числом!");
        } catch (NotInDeclaredLimitsException exception) {
            System.out.println("порт не находится в диапазоне предел");
        }
        return false;
    }
    public static void main(String[] args) {
        if (!initializeConnectionAddress(args)) return;
        HashMap<String,Commands> commands = new HashMap<>();
        commands.put("show",new Commands("show",1, ProcessingCode.OK));
        commands.put("insert", new Commands("insert",2,ProcessingCode.OBJECT));
        commands.put("help",new Commands("help",1,ProcessingCode.OK));
        commands.put("history", new Commands("history",1,ProcessingCode.OK));
        commands.put("clear", new Commands("clear",1,ProcessingCode.OK));
        commands.put("filter_contains_name",new Commands("filter_contains_name",2,ProcessingCode.OK));
        commands.put("filter_greater_than_form_of_education", new Commands("filter_greater_than_form_of_education",2,ProcessingCode.OK));
        commands.put("filter_less_than_transferred_students", new Commands("filter_less_than_transferred_students",2,ProcessingCode.OK));
        commands.put("info", new Commands("info",1,ProcessingCode.OK));
        commands.put("remove_key",new Commands("remove_key",2,ProcessingCode.OK));
        commands.put("update",new Commands("update",2,ProcessingCode.OBJECT));
        commands.put("remove_lower", new Commands("remove_lower",1,ProcessingCode.OBJECT));
        commands.put("replace_if_greater", new Commands("replace_if_greater",2,ProcessingCode.OBJECT));
        commands.put("execute_script",new Commands("execute_script",2,ProcessingCode.SCRIPT));
        commands.put("exit",new Commands("exit",1,ProcessingCode.OK));
    //    commands.put("exit_server",new Commands("exit_server",1,ProcessingCode.OK));
        Scanner userScanner = new Scanner(System.in);
        QueryManager queryManager = new QueryManager(userScanner);
        UserHandler userHandler = new UserHandler(userScanner,commands,queryManager);
        Client client = new Client("localhost",port,RECONNECTION_TIMEOUT,MAX_RECONNECTION_ATTEMPTS,userHandler);
        client.run();
        userScanner.close();
    }
}
