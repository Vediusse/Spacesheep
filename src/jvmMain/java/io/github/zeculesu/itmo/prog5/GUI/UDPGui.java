package io.github.zeculesu.itmo.prog5.GUI;


import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.models.SendedCommandResponse;

import java.io.*;
import java.net.*;


public class UDPGui {
    DatagramSocket clientSocket;
    InetAddress serverIPAddress;
    String host;
    int port;

    public UDPGui(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public SendedCommandResponse sendMeCommand() throws IOException, ClassNotFoundException, SocketTimeoutException {
        createSocket();

        // Отправка сообщения серверу
        Request request = new Request();
        request.setCommand("send_command");

        byte[] sendData = castToByte(request);

        sendPacket(sendData);
        SendedCommandResponse response = (SendedCommandResponse) getResponse();

        // Закрываем сокет
        clientSocket.close();

        return response;
    }

    public Response sendMeLoginCoords(String login, String password) throws IOException, ClassNotFoundException, SocketTimeoutException {
        createSocket();

        // Отправка сообщения серверу
        Request request = new Request();
        request.setCommand("getlogincoords");
        request.setLogin(login);
        request.setPassword(password);
        byte[] sendData = castToByte(request);

        sendPacket(sendData);
        Response response = (Response) getResponse();
        // Закрываем сокет
        clientSocket.close();
        return response;
    }

    public void createSocket() throws SocketException, UnknownHostException {
        // Создаем сокет для отправки данных
        this.clientSocket = new DatagramSocket();
        // Получаем IP адрес сервера
        this.serverIPAddress = InetAddress.getByName(this.host);
    }

    public void sendPacket(byte[] sendData) throws IOException {
        // Создаем пакет для отправки данных серверу
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIPAddress, this.port);
        // Отправляем пакет серверу
        this.clientSocket.send(sendPacket);
    }
    public Response getResponse() throws IOException, ClassNotFoundException, SocketTimeoutException {
        // Получение ответа от сервера
        // Создаем буфер для приема данных от сервера
        //todo изменить
        byte[] receiveData = new byte[65507];
        // Создаем пакет для приема данных от сервера
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        // Получаем данные от сервера
        this.clientSocket.receive(receivePacket);
        byte[] data = receivePacket.getData();

        return castToObjectFromByte(data);
    }

    public static byte[] castToByte(Serializable data) throws IOException {
        // Преобразуем сообщение в массив байтов
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(data);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    public Response castToObjectFromByte(byte[] data) throws IOException, ClassNotFoundException {
        // Преобразуем массив байт обратно в объект
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (Response) objectInputStream.readObject();
    }

    public Response sendRequest(Request request) throws SocketException, UnknownHostException, IOException, ClassNotFoundException {
        createSocket();
        byte[] sendData = castToByte(request);
        sendPacket(sendData);
        Response response = getResponse();
        clientSocket.close();
        return response;
    }

    public void closeClientSocket(){
        this.clientSocket.close();
    }
}

