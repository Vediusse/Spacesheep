package io.github.zeculesu.itmo.prog5.server.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ConnectionReception {
    public static DatagramPacket reception(DatagramSocket serverSocket, byte[] receiveData) throws IOException {
        // ������� ����� ��� ������ ������ �� �������
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        // �������� ������ �� �������
        serverSocket.receive(receivePacket);
        // ������ ����� ������ �������
        return receivePacket;
    }
}
