import java.util.*;
import org.json.simple.*;

public class User{
	private String username;
	private String password;

	private ArrayList<Playlist> playlists = new ArrayList<Playlist>();
	private UserAccountDetails userDetails;

	public User(String username, String password, String firstName, String lastName, Date dob){
		this.username = username;
		this.password = password;
		this.userDetails = new UserAccountDetails(firstName, lastName, dob, new Date());
	}


	public void createPlaylist(){
		Playlist playlist = new Playlist();
		playlists.add(playlist);
	}

	public void deletePlaylist(String playlistId){
		playlists.removeIf((Playlist playlist) -> playlist.playlistId.equals(playlistId));
	}

	public JSONArray getPlaylists(){
		JSONArray playlistsIds = new JSONArray();
		for(Playlist p : playlists){
			JSONObject playlist = new JSONObject();
			playlist.put("playlistId", p.playlistId);
			playlist.put("playlistName", p.getName());
			playlistsIds.add(playlist);
		}
		return playlistsIds;
	}

	public JSONObject getUserInfo(){
		return this.userDetails.getUserDetails();
	}

}
