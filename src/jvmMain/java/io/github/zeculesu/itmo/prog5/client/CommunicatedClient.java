package io.github.zeculesu.itmo.prog5.client;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Возможность общения с клиентом
 */
public interface CommunicatedClient {
    void start();
    void run();
}
