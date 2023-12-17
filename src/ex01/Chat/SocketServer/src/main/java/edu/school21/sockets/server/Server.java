package edu.school21.sockets.server;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.repositories.CrudRepository;
import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

@Component
public class Server {
    private final UsersService usersService;
    private final CrudRepository<Message> messageRepository;
    public final static List<ServerClient> serverList = new LinkedList<>();



    @Autowired
    public Server(UsersService usersService, CrudRepository<Message> messageRepository) {
        this.messageRepository = messageRepository;
        this.usersService = usersService;
    }

    public void run(int port) throws IOException {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Сервер запущен!");
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerClient(socket, usersService, messageRepository));
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }
}
