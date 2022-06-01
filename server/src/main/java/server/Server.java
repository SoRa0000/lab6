package server;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

import common.exceptions.ClosingSocketException;
import common.exceptions.ConnectionErrorException;
import common.exceptions.OpeningServerSocketException;
import common.interaction.*;
import server.utilities.CollectionManager;
import server.utilities.RequestHandler;

public class Server {
    private final int port;
    private final int soTimeout;
    private final RequestHandler requestHandler;
    private ServerSocket serverSocket;
    private Scanner scanner = new Scanner(System.in);
    CollectionManager collectionManager;
    String command ="";
    public Server(int port, int soTimeout, RequestHandler requestHandler, CollectionManager collectionManager){
        this.port = port;
        this.soTimeout = soTimeout;
        this.requestHandler = requestHandler;
        this.collectionManager = collectionManager;
    }

    public void run() throws IOException {
        try{
            openServerSocket();
            Runnable userInput = () ->{
                while(true){
                    try{
                        String[] userCommand = scanner.nextLine().trim().split(" ");
                        if(userCommand[0].equals("exit_server")){
                            try { stop();break;
                            } catch (IOException e) { e.printStackTrace(); }
                        }
                        else if(userCommand[0].equals("save"))  collectionManager.save();
                        else AppServer.logger.info("Нет таких команд!\nДля завершения сервера - exit_server\nДля сохранение коллекцию в файл - save");
                    }catch (NoSuchElementException err){
                        AppServer.logger.info("Непредвиденная ошибка!");
                        System.exit(0);
                    }

                }
            };
            Thread thread = new Thread(userInput);
            thread.start();
            boolean processingStatus = true;
            while(processingStatus){
                try(Socket clientSocket = connectToClient();){
                    processingStatus = processClientRequest(clientSocket);
                } catch (ConnectionErrorException | SocketTimeoutException exception) {
                    break;
                } catch (IOException err){
                    AppServer.logger.info("Произошла ошибка при попытке завершить соединение с клиентом!");
                }
            }
            stop();
        } catch(OpeningServerSocketException err){
            AppServer.logger.info("Сервер не может быть запущен!");
        }
    }

    public void openServerSocket() throws IOException {
        try{
            AppServer.logger.info("Запуск сервера...");
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(soTimeout);
            AppServer.logger.info("Сервер успешно запущен.");
        } catch(IllegalArgumentException err){
            AppServer.logger.info("Порт '" + port + "' находится за пределами возможных значений!");
            throw new OpeningServerSocketException();
        }
        catch (IOException err){
            AppServer.logger.info("Произошла ошибка при попытке использовать порт '" + port + "'!");
            throw new OpeningServerSocketException();
        }
    }

    public Socket connectToClient() throws IOException {
        try{
            AppServer.logger.info("Прослушивание порта '" + port + "'...");
            Socket clientSocket = serverSocket.accept();
            AppServer.logger.info("Соединение с клиентом успешно установлено.");
            return clientSocket;
        } catch(SocketTimeoutException err){
            AppServer.logger.info("Превышено время ожидания подключения!");
            throw new SocketTimeoutException();
        }
        catch (IOException err) {
            AppServer.logger.info("Произошла ошибка при соединении с клиентом!");
            throw new ConnectionErrorException();
        }
    }

    public boolean processClientRequest(Socket clientSocket) throws IOException {
        Request userRequest = null;
        Response responseToUser = null;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())){
            do{
                userRequest = (Request) clientReader.readObject();
                responseToUser = requestHandler.handle(userRequest);
                AppServer.logger.info("Запрос '" + Arrays.toString(userRequest.getCommandName()) + "'обработан.");
                clientWriter.writeObject(responseToUser);
                clientWriter.flush();
            }while(responseToUser.getResponseCode() != ResponseCode.SERVER_EXIT);
            return false;
        } catch (ClassNotFoundException err) {
            AppServer.logger.info("Произошла ошибка при чтении полученных данных!");
        } catch(InvalidClassException err){
            AppServer.logger.info("Произошла ошибка при отправке данных на клиент!");
        } catch (IOException err){
            if(userRequest == null){
                AppServer.logger.info("Непредвиденный разрыв соединения с клиентом!");
            }
            else{
                AppServer.logger.info("Клиент успешно отключен от сервера!");
            }
        }
        return true;
    }
    public void stop() throws IOException {
        try{
            AppServer.logger.info("Завершение работы сервера...");
            if(serverSocket == null) throw new ClosingSocketException();
            serverSocket.close();
            AppServer.logger.info("Работа сервера успешно завершена.");
            System.exit(0);
        }catch (ClosingSocketException err){
            AppServer.logger.info("Невозможно завершить работу еще не запущенного сервера!");
        } catch (IOException err){
            AppServer.logger.info("Произошла ошибка при завершении работы сервера!");
        }
    }

}
