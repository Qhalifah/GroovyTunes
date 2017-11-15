package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import utils.Utility;
import utils.dbmgr.GroovyConnection;

public class WebSocket {

    public static void main(String[] args) throws Exception{

        if(args.length == 2) {
        	if(args[0].equals("add"))
        		Utility.updateSongDB(args[1], false);
        	else if(args[0].equals("update"))
        		Utility.updateSongDB(args[1], true);
        	System.exit(0);
        }
        GroovyConnection.getConnection();

        Server server = new Server(8080);
        ServletContextHandler ctx = new ServletContextHandler();
        ctx.addServlet(PlayerServlet.class, "");
        server.setHandler(ctx);
        server.start();
        server.join();
    }

    public static class PlayerServlet extends WebSocketServlet {

		private static final long serialVersionUID = -1583081137446022014L;

		@Override
        public void configure(WebSocketServletFactory factory) {
            factory.register(Handler.class);
        }
    }
}
