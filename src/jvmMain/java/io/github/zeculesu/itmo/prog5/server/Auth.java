package io.github.zeculesu.itmo.prog5.server;

import io.github.zeculesu.itmo.prog5.client.UDPClient;
import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.models.Response;

import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Auth {

    public static Response sendAuth(String login, String password, UDPClient udpClient) throws SocketTimeoutException, IOException, ClassNotFoundException {
        udpClient.createSocket();

        // Отправка сообщения серверу
        Request request = new Request();
        request.setCommand("auth");
        request.setArg(login + " " + password);
        request.setLogin(login);

        byte[] sendData = UDPClient.castToByte(request);

        udpClient.sendPacket(sendData);
        Response response = udpClient.getResponse();
        // Закрываем сокет
        udpClient.closeClientSocket();

        return response;
    }

    public static Response checkUniqLogin(String login, UDPClient udpClient) throws IOException, ClassNotFoundException {
        udpClient.createSocket();

        // Отправка сообщения серверу
        Request request = new Request();
        request.setCommand("check_uniq_login");
        request.setArg(login);
        request.setLogin(login);

        byte[] sendData = UDPClient.castToByte(request);

        udpClient.sendPacket(sendData);
        Response response = udpClient.getResponse();
        // Закрываем сокет
        udpClient.closeClientSocket();

        return response;
    }

    public static Response sendReg(String login, String password, UDPClient udpClient) throws IOException, ClassNotFoundException {
        udpClient.createSocket();

        // Отправка сообщения серверу
        Request request = new Request();
        request.setCommand("register");
        request.setArg(login + " " + password);

        byte[] sendData = UDPClient.castToByte(request);

        udpClient.sendPacket(sendData);
        Response response = udpClient.getResponse();
        // Закрываем сокет
        udpClient.closeClientSocket();

        return response;
    }

    public static String hash_password(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");

            byte[] messageDigest = md.digest((password + "slavaloh").getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
