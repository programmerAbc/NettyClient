package com.practice.nettyclient;

import com.practice.protocol.EchoClient;
import org.junit.Test;

public class AppTest {
    @Test
    public void runClient() {
        EchoClient echoClient = new EchoClient();
        echoClient.startClient("192.168.1.78", 8080);
    }
}
