package edu.school21.sockets.connect;

import java.io.BufferedReader;
import java.io.IOException;

import static edu.school21.sockets.connect.InteractionServer.connectedServer;

public class MessageReceiver implements Runnable {
    private final BufferedReader in;

    public MessageReceiver(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
                if ("Successful!".equals(response)) {
                    connectedServer[0] = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
