package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import java.util.*;

import utils.Utility;

public class WebSocket {

    public static void main(String[] args) throws Exception{
        Utility.readAllSongs();

        Server server = new Server(8080);
        ServletContextHandler ctx = new ServletContextHandler();
        ctx.addServlet(PlayerServlet.class, "");
        server.setHandler(ctx);
        server.start();
        server.join();
    }

    public static class PlayerServlet extends WebSocketServlet {

        @Override
        public void configure(WebSocketServletFactory factory) {
            factory.register(Handler.class);
        }
    }
}
