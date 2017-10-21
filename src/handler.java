import java.io.IOException;
import java.util.*;
import org.json.simple.*;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class handler{
	private Session session;
	utility u = new utility();


    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception{
        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
        this.session = session;
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        System.out.println("Message: " + message);
		if(message.equals("getMusic")){
			try {
				JSONObject songs = u.getMusic("./songs");
				session.getRemote().sendString(songs.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
}
