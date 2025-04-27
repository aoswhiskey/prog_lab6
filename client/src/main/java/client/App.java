package client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import client.runner.Runner;
import client.network.UDPClient;
import client.utility.StandardConsole;

import java.io.IOException;
import java.net.InetAddress;

public class App {
    private static final int PORT = 23456;
    public static final Logger logger = LogManager.getLogger("ClientLogger");

    public static void main(String[] args) {
        var console = new StandardConsole();
        try {
            var client = new UDPClient(InetAddress.getLocalHost(), PORT);
            var runner = new Runner(client, console);

            runner.interactiveMode();
        } catch (IOException e) {
            logger.info("Невозможно подключиться к серверу.", e);
            System.out.println("Невозможно подключиться к серверу.");
        }
    }
}
