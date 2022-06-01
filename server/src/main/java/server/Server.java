package server;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
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
                        else System.out.println("Нет таких команд!\nДля завершения сервера - exit_server\nДля сохранение коллекцию в файл - save");
                    }catch (NoSuchElementException err){
                        System.out.println("Непредвиденная ошибка!");
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
                    System.out.println("Произошла ошибка при попытке завершить соединение с клиентом!");
                }
            }
            stop();
        } catch(OpeningServerSocketException err){
            System.out.println("Сервер не может быть запущен!");
        }
    }

    public void openServerSocket() throws IOException {
        try{
            System.out.println("Запуск сервера...");
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(soTimeout);
            System.out.println("Сервер успешно запущен.");
        } catch(IllegalArgumentException err){
            System.out.println("Порт '" + port + "' находится за пределами возможных значений!");
            throw new OpeningServerSocketException();
        }
        catch (IOException err){
            System.out.println("Произошла ошибка при попытке использовать порт '" + port + "'!");
            throw new OpeningServerSocketException();
        }
    }

    public Socket connectToClient() throws IOException {
        try{
            System.out.println("Прослушивание порта '" + port + "'...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Соединение с клиентом успешно установлено.");
            return clientSocket;
        } catch(SocketTimeoutException err){
            System.out.println("Превышено время ожидания подключения!");
            throw new SocketTimeoutException();
        }
        catch (IOException err) {
            System.out.println("Произошла ошибка при соединении с клиентом!");
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
                System.out.println("Запрос '" + Arrays.toString(userRequest.getCommandName()) + "'обработан.");
                clientWriter.writeObject(responseToUser);
                clientWriter.flush();
            }while(responseToUser.getResponseCode() != ResponseCode.SERVER_EXIT);
            return false;
        } catch (ClassNotFoundException err) {
            System.out.println("Произошла ошибка при чтении полученных данных!");
        } catch(InvalidClassException err){
            System.out.println("Произошла ошибка при отправке данных на клиент!");
        } catch (IOException err){
            if(userRequest == null){
                System.out.println("Непредвиденный разрыв соединения с клиентом!");
            }
            else{
                System.out.println("Клиент успешно отключен от сервера!");
            }
        }
        return true;
    }
    public void stop() throws IOException {
        try{
            System.out.println("Завершение работы сервера...");
            if(serverSocket == null) throw new ClosingSocketException();
            serverSocket.close();
            System.out.println("Работа сервера успешно завершена.");
            System.exit(0);
        }catch (ClosingSocketException err){
            System.out.println("Невозможно завершить работу еще не запущенного сервера!");
        } catch (IOException err){
            System.out.println("Произошла ошибка при завершении работы сервера!");
        }
    }

}
