package io.github.zeculesu.itmo.prog5.GUI.Windows;

import io.github.zeculesu.itmo.prog5.GUI.UDPGui;
import javafx.application.Application;

public abstract class ApplicationAbsract extends Application {
    UDPGui udpGui = new UDPGui("localhost",45003);
}

