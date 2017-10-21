import java.util.*;

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
		playlist.playlistId = UUID.randomUUID().toString();
		playlists.add(playlist);
	}
	
	public void deletePlaylist(String playlistId){
		playlists.removeIf((Playlist playlist) -> playlist.playlistId.equals(playlistId);
	}

}