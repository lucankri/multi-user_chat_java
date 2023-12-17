package edu.school21.sockets.app;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import edu.school21.sockets.connect.InteractionServer;
import edu.school21.sockets.connect.MessageReceiver;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Parameters(separators = "=")
public class Client {
    @Parameter(names = "--server-port")
    private static Integer port;



    public static void main(String[] args) {
        try {
            if (args.length == 1) {
                JCommander.newBuilder().addObject(new Client()).build().parse(args);
                new InteractionServer().run(port);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("The client starts with the <--service-port=...> argument");
        }

    }
}
