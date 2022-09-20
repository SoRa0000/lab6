package client.utilities;

import common.exceptions.CommandUsageException;
import common.exceptions.IncorrectInputScriptException;
import common.exceptions.ScriptRecursionException;
import common.interaction.Request;
import common.interaction.ResponseCode;
import common.interaction.StudyGroupRaw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class UserHandler {
    private final int maxRewriteAttempts = 5;
    Scanner userScanner;
    private Stack<File> scriptStack = new Stack<>();
    private Stack<Scanner> scannerStack = new Stack<>();
    QueryManager queryManager;
    HashMap<String,Commands> commands;
    public UserHandler(Scanner scanner, HashMap commands,QueryManager queryManager){
        this.userScanner = scanner;
        this.commands = commands;
        this.queryManager = queryManager;
    }

    public Request handle(ResponseCode serverResponseCode) throws FileNotFoundException {
        String userInput;
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try{
            do{
                try{
                    if(fileMode() && (serverResponseCode == ResponseCode.ERROR || serverResponseCode == ResponseCode.SERVER_EXIT))
                        throw new IncorrectInputScriptException();
                    while(fileMode() && !userScanner.hasNextLine()){
                        userScanner.close();
                        userScanner = scannerStack.pop();
                    }
                    if(fileMode()){
                        userInput = userScanner.nextLine();
                        if(!userInput.isEmpty()){
                            System.out.println("$ " + userInput);
                        }
                    }else{
                        System.out.print("& ");
                        userInput = userScanner.nextLine();
                    }
                    userCommand = userInput.trim().split(" ");
                }catch (NoSuchElementException | IllegalArgumentException err){
                    System.out.println("Произошла ошибка при вводе команды!");
                    userCommand = new String[]{""};
                    rewriteAttempts++;
                    if(rewriteAttempts >= maxRewriteAttempts){
                        System.out.println("Превышено количество попыток ввода!");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userCommand);
            } while(processingCode == ProcessingCode.ERROR);
            try{
                switch (processingCode){
                    case OBJECT:
                        StudyGroupRaw studyGroupRaw = queryManager.createQueryOfStudyGroup();
                        return new Request(userCommand,studyGroupRaw);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if(!scriptFile.exists()) throw new FileNotFoundException();
                        if(!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1) throw new ScriptRecursionException();
                        scannerStack.push(userScanner);
                        scriptStack.push(scriptFile);
                        userScanner = new Scanner(scriptFile);
                        queryManager.setScanner(userScanner);
                        queryManager.setScriptMode(true);
                        System.out.println("Выполняю скрипт " + scriptFile.getName() + " ...");
                }
            }catch (FileNotFoundException err){
                System.out.println("Файл со скриптом не найден!");
            }catch (ScriptRecursionException err){
                System.out.println("Скрипты не могут вызываться рекурсивно!");
                throw new IncorrectInputScriptException();
            }
        }catch (IncorrectInputScriptException err){
            System.out.println("Выполнение скрипта прервано!");
            while (!scannerStack.isEmpty()){
                userScanner.close();
                userScanner = scannerStack.pop();
                queryManager.setScanner(userScanner);
                queryManager.setScriptMode(false);
            }
            scriptStack.clear();
            return new Request();
        }
        return new Request(userCommand);
    }

    private ProcessingCode processCommand(String[] command){
        try{
            if(commands.containsKey(command[0]) && commands.get(command[0]).getCommandLength() == command.length){
                return commands.get(command[0]).getProcessingCode();
            }
            else {
                throw new CommandUsageException();
            }
        }catch (CommandUsageException err){
            System.out.println("Неверный комманд! help - вывести справку по доступным командам");
            return ProcessingCode.ERROR;
        }
    }
    private boolean fileMode() {
        return !scannerStack.isEmpty();
    }

}
