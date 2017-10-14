import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class webSocket {

public static void main(String[] args) throws Exception{
  Server server = new Server(8080);
  ServletContextHandler ctx = new ServletContextHandler();
  ctx.addServlet(sLet.class, "");
  server.setHandler(ctx);
  server.start();
  server.join();
}

public static class sLet extends WebSocketServlet{

  @Override
  public void configure(WebSocketServletFactory arg0) {
    arg0.register(handler.class);
  }
}
}
