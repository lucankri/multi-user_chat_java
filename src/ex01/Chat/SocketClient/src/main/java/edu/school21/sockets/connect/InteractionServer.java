package edu.school21.sockets.connect;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InteractionServer {
    public void run(int port) {
        try (Socket clientSocket = new Socket("localhost", port);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            MessageReceiver messageReceiver = new MessageReceiver(in);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(messageReceiver);
            while (messageReceiver.connect) {
                String word = consoleReader.readLine();
                out.write(word + "\n");
                out.flush();
                if ("exit".equalsIgnoreCase(word))
                    break;
            }
            executorService.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
