package edu.school21.sockets.server;

import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//@RestController
//@RequestMapping("/")
@Component
public class Server {
    private final UsersService usersService;

    @Autowired
    public Server(UsersService usersService) {
        this.usersService = usersService;
    }

    public void send(String msg, BufferedWriter out) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }

    public void run(int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Сервер запущен!");
            try (Socket clientSocket = server.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                send("Hello from Server!", out);
                while(true) {
                    String word = in.readLine();
                    if ("signUp".equals(word)) {
                        send("Enter username:", out);
                        String username;
                        while((username = in.readLine()).isEmpty()) {
                            send("You need to enter a username...", out);
                        }
                        send("Enter password:", out);
                        String password;
                        while((password = in.readLine()).isEmpty()) {
                            send("You need to enter a password...", out);
                        }
                        send("Please wait! Registration is progress...", out);
                        int codeService = usersService.signUp(username, password);
                        if (codeService == 0) {
                            send("Successful!", out);
                            break;
                        } else if (codeService == 1) {
                            send("Such a user already exists!\nTry again...", out);
                        }
                    } else {
                        send("There is no such action\nTry again...", out);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        System.out.println("Сервер закрыт!");
    }





//
//
//    @GetMapping("/")
//    public String ping() {
//        return "Hello from Server!";
//    }
//
//    public static class SignUpForm {
//        public String username;
//        public String password;
//    }
//
//    public static class SignUpDto {
//        public Integer status;
//        public String message;
//    }
//
//    protected static ResponseEntity<SignUpDto> response(int status, String message) {
//        SignUpDto dto = new SignUpDto();
//        dto.status = status;
//        dto.message = message;
//        return ResponseEntity.ok().body(dto);
//    }
//
//    @PostMapping("/signUp")
//    public ResponseEntity<SignUpDto> signUp(@RequestBody(required = false) SignUpForm form) {
//        if (form == null) {
//            return response(1, "Request body must not me empty!");
//        }
//        if (form.username == null) {
//            return response(2, "Username must not me empty!");
//        }
//        if (form.password == null) {
//            return response(3, "Password must not me empty!");
//        }
//
//        int result = usersService.signUp(form.username, form.password);
//
//        if (result == 1) {
//            return response(4, "User already exists!");
//        }
//
//        if (result == 2) {
//            return response(5, "Unknown error!");
//        }
//
//        return response(0, "User created!");
//    }

}
