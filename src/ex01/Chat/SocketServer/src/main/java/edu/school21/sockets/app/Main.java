package edu.school21.sockets.app;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.server.Server;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;


@Parameters(separators = "=")
public class Main {
    @Parameter(names = "--port")
    private static int port;

    public static void main(String[] args) {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.ERROR);
        try {
            if (args.length == 1) {
                JCommander.newBuilder().addObject(new Main()).build().parse(args);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("The server starts with the <--port=...> argument");
            return;
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        try {
            context.getBean("server", Server.class).run(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        context.close();
    }
}
