package io.github.zeculesu.itmo.prog5.server.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ConnectionReception {
    public static DatagramPacket reception(DatagramSocket serverSocket, byte[] receiveData) throws IOException {
        // Создаем пакет для приема данных от клиента
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        // Получаем данные от клиента
        serverSocket.receive(receivePacket);
        // Отдаем пакет нашему серверу
        return receivePacket;
    }
}
