import java.io.IOException;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

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
	JSONParser parser = new JSONParser();
	User admin = new User("admin", "password", "Bob", "Loblaw", new Date(69, 0, 1));

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
		try {
			JSONObject msgJSON = (JSONObject) parser.parse(message);
			String type = (String) msgJSON.get("type");
        	System.out.println("Message: " + msgJSON);
			JSONObject toSend = new JSONObject();
			switch (type){
				case "getMusic":
					toSend.put("type", "retGetMusic");
					toSend.put("message", u.getMusic());
					session.getRemote().sendString(toSend.toString());
					break;
				case "getUser":
					toSend.put("type", "retGetUser");
					toSend.put("message", admin.getUserInfo());
					session.getRemote().sendString(toSend.toString());
					break;
				case "getPlaylists":
					toSend.put("type", "retGetPlaylists");
					toSend.put("message", admin.getPlaylists());
					session.getRemote().sendString(toSend.toString());
					break;
				case "createPlaylist":
					admin.createPlaylist();
					toSend.put("type", "retGetPlaylists");
					toSend.put("message", admin.getPlaylists());
					session.getRemote().sendString(toSend.toString());
					break;
				case "dispPlaylist":
					String id = (String) msgJSON.get("id");
					Playlist playlistToDisp = new Playlist(id);
					toSend.put("type", "retDispPlaylist");
					toSend.put("message", playlistToDisp.toJSON());
					session.getRemote().sendString(toSend.toString());
					break;
				case "addSong":
					String playlistId = (String) msgJSON.get("playlistId");
					String songId = (String) msgJSON.get("songId");
					Playlist playlistToAdd = new Playlist(playlistId);
					playlistToAdd.addSong(songId);
					break;
				case "removeSong":
					String playlistIdRemove = (String) msgJSON.get("playlistId");
					String songIdRemove = (String) msgJSON.get("songId");
					Playlist playlistToRemove = new Playlist(playlistIdRemove);
					playlistToRemove.removeSong(songIdRemove);
					toSend.put("type", "retDispPlaylist");
					toSend.put("message", playlistToRemove.toJSON());
					session.getRemote().sendString(toSend.toString());
					break;
				case "changeName":
					String playlistIdName = (String) msgJSON.get("id");
					String newName = (String) msgJSON.get("name");
					Playlist playlistToName = new Playlist(playlistIdName);
					playlistToName.setName(newName);
					toSend.put("type", "retDispPlaylist");
					toSend.put("message", playlistToName.toJSON());
					session.getRemote().sendString(toSend.toString());
					toSend.put("type", "retGetPlaylists");
					toSend.put("message", admin.getPlaylists());
					session.getRemote().sendString(toSend.toString());
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
