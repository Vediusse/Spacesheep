package io.github.zeculesu.itmo.prog5.server.net;

import io.github.zeculesu.itmo.prog5.data.AuthCheckSpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.data.CachedSpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.Request;
import io.github.zeculesu.itmo.prog5.client.ConsoleCommandEnvironment;
import io.github.zeculesu.itmo.prog5.data.SpaceMarineCollection;
import io.github.zeculesu.itmo.prog5.models.Response;
import io.github.zeculesu.itmo.prog5.models.SpaceMarine;
import io.github.zeculesu.itmo.prog5.server.command.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.Map;

public class RequestReading {
    public static Request requestRead(DatagramPacket receivePacket) throws IOException, ClassNotFoundException {
        // Преобразуем данные в массив байт
        byte[] data = receivePacket.getData();
        // Преобразуем массив байт обратно в объект
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (Request) objectInputStream.readObject();
    }
}
