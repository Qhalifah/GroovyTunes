import java.util.*;

public class User{
	private String username;
	private String password;
	
	private ArrayList<Playlist> playlists = new ArrayList<Playlist>();
	private UserAccountDetails userDetails;
	
	public User(String username, String password, String firstName, String lastName, Date dob){
		this.username = username;
		this.password = password;
		 // Add method for date joined
		
		this.userDetails = new UserAccountDetails(String firstName, String lastName, Date dob, Date dateJoined);
	}
		
	
	public void createPlaylist(){
		Playlist playlist = new Playlist();
		playlist.playlistId = 
		playlists.add(playlist);
	}
	
	public void deletePlaylist(String playlistId){
		playlists.remove()
	}

}