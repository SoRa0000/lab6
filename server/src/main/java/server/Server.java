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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.utilities.CollectionManager;
import server.utilities.RequestHandler;

public class Server {
    private final int port;
    private final int soTimeout;
    private final RequestHandler requestHandler;
    private ServerSocket serverSocket;
    private boolean clientSocketStatus = false;
    private boolean serverExitStatus = false;
    private Scanner scanner = new Scanner(System.in);
    CollectionManager collectionManager;
    String command ="";
    public final Logger LOG
            = LoggerFactory.getLogger(Server.class);
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

                        if(userCommand.length == 1 && userCommand[0].equals("exit_server")){
                            try {
                                    stop();
                                    break;
                            } catch (IOException e) { e.printStackTrace(); }
                        }
                        else if(userCommand[0].equals("save"))  collectionManager.save();
                        else LOG.error("Нет таких команд!\nДля завершения сервера - exit_server\nДля сохранение коллекцию в файл - save");
                    }catch (NoSuchElementException err){
                        LOG.error("Непредвиденная ошибка!");
                        System.exit(0);
                    }

                }
            };
            Thread thread = new Thread(userInput);
            thread.start();
            boolean processingStatus = true;
            while(processingStatus){
                try(Socket clientSocket = connectToClient();){

                    clientSocketStatus = true;
                    processingStatus = processClientRequest(clientSocket);


                } catch (ConnectionErrorException | SocketTimeoutException exception) {
                    clientSocketStatus = false;
                    break;
                } catch (IOException err){
                    clientSocketStatus = false;
                    LOG.error("Произошла ошибка при попытке завершить соединение с клиентом!");
                }
            }
            stop();
        } catch(OpeningServerSocketException err){
            LOG.error("Сервер не может быть запущен!");
        }

    }

    public void openServerSocket() throws IOException {
        try{
            LOG.info("Запуск сервера...");
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(soTimeout);
            LOG.info("Сервер успешно запущен.");
        } catch(IllegalArgumentException err){
            LOG.error("Порт '" + port + "' находится за пределами возможных значений!");
            throw new OpeningServerSocketException();
        }
        catch (IOException err){
            LOG.error("Произошла ошибка при попытке использовать порт '" + port + "'!");
            throw new OpeningServerSocketException();
        }
    }

    public Socket connectToClient() throws IOException {
        try{
            LOG.info("Прослушивание порта '" + port + "'...");
            Socket clientSocket = serverSocket.accept();
            LOG.info("Соединение с клиентом успешно установлено.");
            return clientSocket;
        } catch(SocketTimeoutException err){
            LOG.error("Превышено время ожидания подключения!");
            throw new SocketTimeoutException();
        }
        catch (IOException err) {
            LOG.error("Произошла ошибка при соединении с клиентом!");
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
                LOG.info("Запрос '" + Arrays.toString(userRequest.getCommandName()) + "'обработан.");
                clientWriter.writeObject(responseToUser);
                clientWriter.flush();

            }while(responseToUser.getResponseCode() != ResponseCode.SERVER_EXIT);
            return false;
        } catch (ClassNotFoundException err) {
            LOG.error("Произошла ошибка при чтении полученных данных!");
        } catch(InvalidClassException err){
            LOG.error("Произошла ошибка при отправке данных на клиент!");
        } catch (IOException err){
            if(userRequest == null){
                LOG.error("Непредвиденный разрыв соединения с клиентом!");
            }
            else{
                LOG.info("Клиент успешно отключен от сервера!");
            }
        }
        return true;
    }
    public void stop() throws IOException {
        try{
            LOG.info("Завершение работы сервера...");
            if(serverSocket == null) throw new ClosingSocketException();
            serverSocket.close();
            LOG.info("Работа сервера успешно завершена.");
            System.exit(0);
        }catch (ClosingSocketException err){
            LOG.error("Невозможно завершить работу еще не запущенного сервера!");
        } catch (IOException err){
            LOG.error("Произошла ошибка при завершении работы сервера!");
        }
    }

}
