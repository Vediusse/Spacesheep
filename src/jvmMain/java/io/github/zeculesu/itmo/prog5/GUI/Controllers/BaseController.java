
package io.github.zeculesu.itmo.prog5.GUI.Controllers;

import io.github.zeculesu.itmo.prog5.GUI.UDPGui;
import io.github.zeculesu.itmo.prog5.client.UDPClient;

public abstract class BaseController {
    protected UDPGui udpGui;

    public void setUdpClient(UDPGui udpClient) {
        this.udpGui = udpClient;
    }
}

