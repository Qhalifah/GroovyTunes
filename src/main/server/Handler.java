package server;

import java.io.IOException;
import java.sql.SQLException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import exceptions.IllegalOperationException;
import exceptions.PlaylistNotCreatedException;
import player.Song;
import user.User;
import utils.Constants;
import utils.Reason;
import utils.Utility;
import utils.dbmgr.GroovyConnection;

@WebSocket
public class Handler {
	private GroovySession session;
	Utility u = new Utility();
	JSONParser parser;
	UserRequestHelper userHelper;

	public Handler() {
		parser = new JSONParser();
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) throws Exception {
		System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
		GroovyConnection.closeConnection();
	}

	@OnWebSocketError
	public void onError(Throwable t) {
		System.out.println("Error: " + t.getMessage());
	}

	@OnWebSocketConnect
	public void onConnect(Session session) throws Exception {
		System.out.println("Connect: " + session.getRemoteAddress().getAddress());
		this.session = new GroovySession(session);
		userHelper = new UserRequestHelper(this.session);
		// I am adding a user object in the session for now.
		// After auth flow, this will be added later
		//AuthResult u = Authenticate.authUser("admin", "password");
		//this.session.add(Constants.USER_SESSION_KEY, u.getUser());
	}

	@SuppressWarnings("unchecked")
	@OnWebSocketMessage
	public void onMessage(String message) throws IOException, ParseException, ClassNotFoundException, SQLException {
		JSONObject msgJSON = (JSONObject) parser.parse(message);
		String level = (String) msgJSON.get("level");
		System.out.println("Message: " + msgJSON);
		JSONObject response;
		if (level.equals("playlist-level")) {
			User admin = (User) session.get(Constants.USER_SESSION_KEY);
			String type = (String) msgJSON.get("type");
			switch (type) {
			case "get-playlists": // GET
				JSONObject msg = new JSONObject();
				msg.put("type", "ret-get-playlists");
				JSONArray playlistsAsJSON = admin.getPlayer().getPlaylistsAsJSON();
				msg.put("playlists", playlistsAsJSON);
				session.getRemote().sendString(msg.toJSONString());
				break;

			case "create-playlist": // POST
				String name = (String) msgJSON.get("name");
				response = new JSONObject();
				response.put("type", "ret-create-playlist");
				try {
					admin.getPlayer().createPlaylist(name);
					response.put("status", "success");
				} catch (PlaylistNotCreatedException ex) {
					response.put("status", "error");
					message = "Unknown error";
					if (ex.getReason() == Reason.ALREADY_EXISTS) {
						message = "Playlist with name \'" + name + "\' already exists";
					} else if (ex.getReason() == Reason.MAX_COUNT_REACHED) {
						message = "Maximum number of playlist reached";
					}
					response.put("message", message);
				}
				session.getRemote().sendString(response.toJSONString());
				break;

			case "rename-playlist": // PUT
				name = (String) msgJSON.get("name");
				String newName = (String) msgJSON.get("new-name");
				response = new JSONObject();
				response.put("type", "ret-rename-playlist");
				response.put("name", name);
				try {
					if (admin.getPlayer().renamePlaylist(name, newName)) {
						response.put("status", "success");
						response.put("name", newName);
					} else {
						response.put("status", "error");
						response.put("message", "Unable to rename the playlist");
					}
				} catch (PlaylistNotCreatedException ex) {
					response.put("status", "error");
					response.put("status", "Playlist with name \'" + name + "\' already exists");
				}
				session.getRemote().sendString(response.toJSONString());
				break;

			case "add-song": // PUT
				name = (String) msgJSON.get("name");
				int ID = Integer.parseInt((String)msgJSON.get("id"));
				response = new JSONObject();
				response.put("type", "ret-add-song");
				response.put("name", name);
				try {
					Song song = new Song(ID);
					if (admin.getPlayer().addSongToPlaylist(name, song)) {
						response.put("status", "success");
					} else {
						response.put("status", "error");
						response.put("message", "Unable to add the song to the playlist");
					}
				} catch (IllegalOperationException ex) {
					response.put("status", "error");
					response.put("message", "Playlist is full");

				}
				session.getRemote().sendString(response.toJSONString());
				break;

			case "remove-song": // PUT
				name = (String) msgJSON.get("name");
				ID = Integer.parseInt((String) msgJSON.get("id"));
				response = new JSONObject();
				response.put("type", "ret-remove-song");
				response.put("name", name);
				Song song = new Song(ID);
				if (admin.getPlayer().removeSongFromPlaylist(name, song)) {
					response.put("status", "success");
				} else {
					response.put("status", "error");
					response.put("message", "Unable to remove the song from playlist");
				}
				session.getRemote().sendString(response.toJSONString());
				break;

			case "remove-playlist": // DELETE
				name = (String) msgJSON.get("name");
				response = new JSONObject();
				if (admin.getPlayer().removePlaylist(name)) {
					response.put("status", "success");
				} else {
					response.put("status", "error");
					response.put("message", "Unable to remove playlist \'" + name + "\'");
				}
				session.getRemote().sendString(response.toJSONString());
				break;

			case "get-all-songs":
				msg = new JSONObject();
				msg.put("type", "get-all-songs");
				JSONArray allSongsAsJSON = admin.getPlayer().getAllSongsAsJSON();
				msg.put("songs", allSongsAsJSON);
				session.getRemote().sendString(msg.toJSONString());
				break;

			case "get-songs":
				name = (String) msgJSON.get("name");
				JSONObject playlistAsJSON = admin.getPlayer().getSongsInPlaylistAsJSON(name);
				playlistAsJSON.put("type", "ret-get-songs");
				session.getRemote().sendString(playlistAsJSON.toJSONString());
				break;

			case "play":
				String typeOfPlayble = (String) msgJSON.get("type-of-playble");
				JSONObject obj = new JSONObject();
				if(typeOfPlayble.equals("playlist")) {
					name = (String) msgJSON.get("name");
					obj = admin.getPlayer().getPlaylist(name).play();
					obj.put("type-of-playable", "playlist");

				} else {
					int id = Integer.parseInt((String) msgJSON.get("id"));
					obj = admin.getPlayer().getSong(new Song(id)).play();
					obj.put("type-of-playable", "song");
				}
				obj.put("type", "ret-play");
				session.getRemote().sendString(obj.toJSONString());
				break;

			case "get-sharable-link":
				name = (String) msgJSON.get("name");
				response = new JSONObject();
				String sharableLink = admin.getPlayer().getSharableLink(name);
				response.put("link", sharableLink);
				session.getRemote().sendString(response.toJSONString());
				break;

			case "add-shared-playlist":
				String shareID = (String) msgJSON.get("id");
				response = new JSONObject();
				if(admin.getPlayer().addSharedPlaylist(shareID)) {
					response.put("status", "success");
				} else {
					response.put("status", "error");
					response.put("message", "unable to add playlist");
				}
				session.getRemote().sendString(response.toJSONString());
				break;
			}
		} else {
			userHelper.onMessage(msgJSON);
		}
	}
}
