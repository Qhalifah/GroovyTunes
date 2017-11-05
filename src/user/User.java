package user;

import org.json.simple.*;
import java.util.*;
import com.opencsv.*;
import java.util.stream.Collectors;
import java.io.*;

import player.Playable;
import player.Playlist;
import player.Song;
import utils.Utility;
import utils.Constants;

/**
 * This class contains a list of playlist available to a user. It also maps to UserAccountDetails.
 */
public class User {
	private List<Playable> playlists;
	private UserAccountDetails details;

	/**
	 * Created one instance of User using given values.
	 * This instance is created from Authenticate class after letting the user log in
	 */
	public User(String username, String password, String firstName, String lastName,
		Date dob, Date dateJoined) {
		this.details = new UserAccountDetails(username, password, firstName, lastName,
			dob, dateJoined);
		playlists = new ArrayList<>(Constants.MAX_PLAYLISTS + 1);
		Playlist allSongs = new Playlist();
		allSongs.setName("All songs");
		try {
			List<Playable> songs = Utility.getAllSongs();
			for(Playable s : songs){
				allSongs.addSong(s);
			}
		} catch(Exception e) {

		}
		playlists.add(allSongs);
	}


	/**
	 * Adds a new playlist to previous collection only if it is permitted
	 * It is permitted only if user is a PREMIUM member or the number of playlists
	 * is below the threshold.
	 * @return true if a new playlist is created, false otherwise
	 */
	public boolean createPlaylist() {
		boolean created = false;
		if (details.isPremiumMember() || playlists.size() < Constants.MAX_PLAYLISTS) {
			Playlist playlist = new Playlist();
			created = true;
			playlists.add(playlist);
			// TODO: update the database
		}
		return created;
	}

	/**
	 * Deletes a playlist if present
	 * @return true if playlist is found and deleted, false otherwise.
	 * If playlist is found, we make sure to delete it
	 */
	public boolean deletePlaylist(String playlistId) {
		// Removing use of lambdas to make it compatible with Java7
		for(int i = 0; i < playlists.size(); ++i) {
			boolean found = false;
			Playlist playlist = (Playlist) playlists.get(i);
			if(playlist.getID().equals(playlistId)) {
				found = true;
				playlists.remove(playlist);
			}
		}
		// TODO: update the database
		return found;
	}

	/**
	 * Gives all playlists present for a user.
	 * Return values include name and playlist ID only
	 * @return list of playlists in JSON form
	 */
	public JSONArray getPlaylists() {
		JSONArray playlistsJSON = new JSONArray();
		for(Playable p : playlists) {
			Playlist playlist = (Playlist) p;
			JSONObject playlistJSON = new JSONObject();
			playlistJSON.put("playlistId", playlist.getID());
			playlistJSON.put("playlistName", playlist.getName());
			playlistsJSON.add(playlistJSON);
		}
		return playlistsJSON;
	}

	/**
	 * Returns user account details
	 * @return account details in JSON form
	 */
	public JSONObject getUserInfo() {
		return this.details.getUserDetails();
	}

	/**
	 * Updates the user details.
	 * @return true if the details are updated, false otherwise
	 */
	public boolean updateUserInfo(String username, String firstName, String lastName, 
		Date dob, Date dateJoined) throws IOException {
		return details.updateUserDetails(username, firstName, lastName, dob, dateJoined);
	}


	/**
	 * commenting out this method. Do we need to provide this functionality as well?
	 */
	/*
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
	*/

}
