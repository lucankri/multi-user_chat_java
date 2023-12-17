package edu.school21.sockets.connect;

import java.io.BufferedReader;
import java.io.IOException;


public class MessageReceiver implements Runnable {
    private final BufferedReader in;
    boolean connect = true;

    public MessageReceiver(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }
            connect = false;
        } catch (IOException ignored) {
            connect = false;
        }
    }
}
