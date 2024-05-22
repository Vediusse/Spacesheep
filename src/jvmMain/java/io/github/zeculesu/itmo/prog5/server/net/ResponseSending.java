package io.github.zeculesu.itmo.prog5.server.net;

import io.github.zeculesu.itmo.prog5.models.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ResponseSending {
    public static void responseSend(DatagramSocket serverSocket, DatagramPacket receivePacket, Response response) throws IOException {
        // Отправка ответа клиенту (в данном случае просто эхо)
        // Получаем IP адрес и порт клиента, чтобы отправить ответ по тому же каналу
        InetAddress clientIPAddress = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();

        // Преобразуем объект в массив байт
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(response);
        objectOutputStream.flush();
        byte[] sendData = byteArrayOutputStream.toByteArray();

        // Создаем пакет для отправки данных клиенту
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientIPAddress, clientPort);
        // Отправляем пакет клиенту
        serverSocket.send(sendPacket);
    }
}
