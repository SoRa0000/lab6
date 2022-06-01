package client;

import client.utilities.UserHandler;
import common.exceptions.ConnectionErrorException;
import common.exceptions.NotInDeclaredLimitsException;
import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class Client {
    private String host;
    private int port;
    private int reconnectionTimeout;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts;
    private UserHandler userHandler;
    private SocketChannel socketChannel;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;

    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, UserHandler userHandler){
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.userHandler = userHandler;
    }

    public void run(){
        try{
            boolean processingStatus = true;
            while(processingStatus){
                try {
                    connectToServer();
                    processingStatus = processRequestToServer();
                } catch (ConnectionErrorException err){
                    if(reconnectionAttempts >= maxReconnectionAttempts) {
                        System.out.println("Превышено количество попыток подключения!");
                        break;
                    }
                    try{
                        Thread.sleep(reconnectionTimeout);
                    } catch (IllegalArgumentException timeoutException){
                        System.out.println("Время ожидания подключения '" + reconnectionTimeout + "'находится за пределами возможных значений!");
                    }
                    catch (Exception timeoutException){
                        System.out.println("Произошла ошибка при попытке ожидания подключения!");
                        System.out.println("Повторное подключение будет произведено немедленно.");
                    }
                }
                reconnectionAttempts++;
            }
            if(socketChannel != null) socketChannel.close();
            System.out.println("Работа клиента успешно завершена.");
        }catch (IOException | ClassNotFoundException err){
            System.out.println("Произошла ошибка при попытке завершить соединение с сервером!");
        } catch (NotInDeclaredLimitsException err){
            System.out.println("Клиент не может быть запущен!");
        }
    }
    public void connectToServer(){
        try{
            if(reconnectionAttempts >= 1) System.out.println("Повторное соединение с сервером...");
            socketChannel = SocketChannel.open(new InetSocketAddress(host,port));
            System.out.println("Соединение с сервером успешно установлено");
            System.out.println("Ожидание разрешения на обмен данными...");
            serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            serverReader = new ObjectInputStream(socketChannel.socket().getInputStream());
            System.out.println("Разрешение на обмен данными получено");
        } catch (IOException err) {
            System.out.println("Произошла ошибка при соединении с сервером!");
            throw new ConnectionErrorException();
        } catch (IllegalArgumentException err){
            System.out.println("Адрес сервера введен некорректно!");
        }
    }

    public boolean processRequestToServer() throws IOException, ClassNotFoundException {
        Request requestToServer = null;
        Response serverRespone = null;
        do{
            try{

                requestToServer = serverRespone != null ? userHandler.handle(serverRespone.getResponseCode()):userHandler.handle(null);
                if(requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverRespone = (Response) serverReader.readObject();
                System.out.println(serverRespone.getResponseBody());
            } catch(NullPointerException err){
                continue;
            }catch (InvalidClassException err){
                System.out.println("Произошла ошибка при отправке данных на сервер!");
            } catch (NotSerializableException err){
                System.out.println("Произошла ошибка при отправке данных на сервер!" + err.getMessage());
            } catch (ClassNotFoundException err){
                System.out.println("Произошла ошибка при чтении полученных данных!");
            } catch (IOException err){
                System.out.println("Соединение с сервером разорвано!");
                try{
                    reconnectionAttempts++;
                    connectToServer();
                } catch (ConnectionErrorException | NotInDeclaredLimitsException e){
                    if(requestToServer.getCommandName()[0].equals("exit"))
                        System.out.println("Команда не будет зарегистрирована на сервере");
                    else System.out.println("Попробуйте повторить команду позднее");
                }
            }
        } while(!requestToServer.getCommandName()[0].equals("exit"));
        return false;
    }
}
