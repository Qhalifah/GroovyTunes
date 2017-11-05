package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import java.util.*;

public class WebSocket {

    public static void main(String[] args) throws Exception{

        Server server = new Server(8080);
        ServletContextHandler ctx = new ServletContextHandler();
        ctx.addServlet(PlayerServlet.class, "");
        server.setHandler(ctx);
        server.start();
        server.join();

    // Adding Admin User
    //User admin = new User("admin", "password", "Bob", "Loblaw", new Date(69, 0, 1));
    //System.out.println(admin.userDetails.toString());

    }

    public static class PlayerServlet extends WebSocketServlet{

        @Override
        public void configure(WebSocketServletFactory arg0) {
            arg0.register(Handler.class);
        }
    }
}
