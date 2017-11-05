import org.json.simple.*;
import java.util.*;
import com.opencsv.*;
import java.util.stream.Collectors;
import java.io.*;

public class User{
	private String username;
	private String password;
        private static final String USERS_DATABASE = "./databases/users.csv";
	private ArrayList<Playlist> playlists = new ArrayList<Playlist>();
	private UserAccountDetails userDetails;
	Utility u = new Utility();

	public User(String username, String password, String firstName, String lastName, Date dob){
		this.username = username;
		this.password = password;
		this.userDetails = new UserAccountDetails(firstName, lastName, dob, new Date());

		Playlist playlist = new Playlist();

		playlist.setName("All Songs");
		try{
			ArrayList<Song> songs = u.getAllSongs();
			for(Song s : songs){
				playlist.addSong(s.songId);
			}
		}catch(Exception e){

		}
		playlists.add(playlist);

	}


	public void createPlaylist(){
		Playlist playlist = new Playlist();
		playlists.add(playlist);
	}

	public void deletePlaylist(String playlistId){
		playlists.removeIf((Playlist playlist) -> playlist.playlistId.equals(playlistId));
		Playlist.removePlaylist(playlistId);
	}

	public JSONArray getPlaylists(){
		JSONArray playlistsIds = new JSONArray();
		for(Playlist p : playlists){
			JSONObject playlist = new JSONObject();
			p = new Playlist(p.playlistId);
			playlist.put("playlistId", p.playlistId);
			playlist.put("playlistName", p.getName());
			playlistsIds.add(playlist);
		}
		return playlistsIds;
	}

	public JSONObject getUserInfo(){
		return this.userDetails.getUserDetails();
	}

	public static void updateUserInfo(String username, String firstName, String lastName, Date dob, Date dateJoined) throws IOException{
		removeUser(username);

		try{
			boolean status = false;
			CSVWriter writer = new CSVWriter(new FileWriter(USERS_DATABASE, true)); // true flag for appending to end of file
			String[] record = ("Id," + username + ",fname," + firstName + ",lname," + lastName + ",dob," + dob + ",datejoin," + dateJoined + ",membershipStatus," + status ).split(",");
			writer.writeNext(record);
			writer.flush();
			writer.close();
		}

		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void removeUser(String username){
		try{

			CSVReader reader = new CSVReader(new FileReader(USERS_DATABASE), ',', '"', 0);
			List<String[]> allRows = reader.readAll();

				// Iterator created for List of records
				// For each String[] record, if 0th element (username) is the one to be deleted, it is removed from List
			for (Iterator<String[]> iterator = allRows.listIterator(); iterator.hasNext(); ){
				String[] record = iterator.next();
				if (record[1].equals(username)) {
					iterator.remove();
				}
			}

			CSVWriter writer = new CSVWriter(new FileWriter(USERS_DATABASE));
			writer.writeAll(allRows);
			writer.flush();
            writer.close();
		}

		catch(Exception e){
			e.printStackTrace();
		}
	}

}
