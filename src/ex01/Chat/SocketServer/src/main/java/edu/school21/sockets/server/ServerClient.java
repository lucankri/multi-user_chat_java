package edu.school21.sockets.server;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.CrudRepository;
import edu.school21.sockets.services.UsersService;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ServerClient extends Thread {
    private Socket socket;
    private UsersService usersService;
    private CrudRepository<Message> messageRepository;
    private BufferedReader in;
    private BufferedWriter out;


    private Boolean authorization = false;
    private User user;


    public ServerClient(Socket socket, UsersService usersService, CrudRepository<Message> messageRepository) throws IOException {
        this.socket = socket;
        this.usersService = usersService;
        this.messageRepository = messageRepository;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }


    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }


    @Override
    public void run() {
        send("Hello from Server!");
        menu();
        if (authorization) {
            startMessaging();
        }
        downService();
    }

    private void startMessaging() {
        try {
            String word;
            send("Start messaging");
            while (true) {
                word = in.readLine();
                if ("exit".equalsIgnoreCase(word) || word == null) {
                    send("You have left the chat.");
                    break;
                }
                for (ServerClient serverClient : Server.serverList) {
                    if (!serverClient.equals(this) && serverClient.getAuthorization()) {
                        serverClient.send(user.getUsername() + ": " + word);
                    }
                }
                Message message = new Message(this.user, word,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                messageRepository.save(message);
            }
        } catch (IOException ignored) {}
    }

    private void menu() {
        String word;
        try {
            while(true) {
                word = in.readLine();
                if ("signIn".equals(word)) {
                    signIn();
                    break;
                } else if ("signUp".equals(word)) {
                    signUp();
                } else if ("exit".equalsIgnoreCase(word) || word == null) {
                    break;
                } else {
                    send("There is no such action\nTry again...");
                }
            }
        } catch (IOException ignored) {}
    }

    private String[] getUsernamePasswordClient() {
        String[] result = new String[2];
        send("Enter username:");
        if ((result[0] = getInfoClient()) == null)
            return null;
        send("Enter password:");
        if ((result[1] = getInfoClient()) == null)
            return null;
        return result;
    }

    private void signIn() {
        String[] usernamePassword = getUsernamePasswordClient();
        send("Please wait! Authorization is progress...");
        if (usernamePassword != null) {
            user = usersService.signIn(usernamePassword[0], usernamePassword[1]);
            if (user != null) {
                authorization = true;
            } else {
                send("The user with this username and password has not been found!");
            }
        }
    }

    private String getInfoClient() {
        String information = null;
        try {
            while (true) {
                information = in.readLine();
                if (information == null || !information.isEmpty()) {
                    break;
                }
                send("You cannot send an empty string...");
            }
        } catch (IOException ignored) {}
        return information;
    }

    private void signUp() {
        String[] usernamePassword = getUsernamePasswordClient();
        send("Please wait! Registration is progress...");
        if (usernamePassword != null) {
            int codeService = usersService.signUp(usernamePassword[0], usernamePassword[1]);
            if (codeService == 0) {
                send("Successful!");
            } else if (codeService == 1) {
                send("Such a user already exists!\nTry again...");
            } else {
                send("Unknown error!");
            }
        }
    }

    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                this.interrupt();
                Server.serverList.remove(this);
            }
        } catch (IOException ignored) {}
    }

    public Boolean getAuthorization() {
        return authorization;
    }
}
