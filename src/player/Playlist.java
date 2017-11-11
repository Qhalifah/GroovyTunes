package player;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import utils.PlaylistOperations;

public class Playlist implements Playable {

	private static final String PLAYLISTS_DATABASE = "./databases/playlists.csv"; // Location
																					// of
																					// playlists.csv
																					// file
	private String name;
	private int ID;
	private String createDate;
	private ArrayList<String> songList;
	private List<Playable> songs;

	public Playlist() {
		this.songList = new ArrayList<String>();
		this.createDate = new Date().toString();
		this.updatePlaylist();
	}

	public Playlist(String ID, boolean justForUsingOtherCTOR) {
		this.songList = new ArrayList<String>(); // Must read in songs from old
													// playlist
		this.songList = getSongsInPlaylist(this.songList);
		this.name = getPlaylistNameandDate()[0];
		this.createDate = getPlaylistNameandDate()[1];
	}

	public Playlist(int ID, String name) {
		this.ID = ID;
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject play() {
		JSONObject playlist = new JSONObject();
		JSONArray arr = new JSONArray();
		for (Playable p : songs) {
			arr.add(p.play());
		}
		playlist.put("songs", arr);
		return playlist;
	}

	public boolean addSong(String songId) {
		// this.songs.add(songID);
		this.updatePlaylist();
		return false;
	}
	
	public boolean addSong(Song s) throws ClassNotFoundException, SQLException, IOException {
		songs.add(s);
		return PlaylistOperations.addSong(s.getSongID(), this.ID);
	}
	
	public boolean removeSong(int songID) {
		boolean removed = false;
		for(int i = 0; i < songs.size(); ++i) {
			Song s = (Song) songs.get(i);
			if(s.getSongID() == songID) {
				songs.remove(s);
				removed = true;
			}
		}
		return removed;
	}

	public void populate(List<Playable> songs) {
		this.songs = songs;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject playlist = new JSONObject();
		playlist.put("playlistName", this.name);
		// playlist.put("playlistId", this.ID);
		playlist.put("createDate", this.createDate);
		JSONArray jSongs = new JSONArray();
		for (String s : songList) {
			jSongs.add(s);
		}
		playlist.put("songs", jSongs);
		return playlist;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
		this.updatePlaylist();
	}

	public String share() {
		return "";
	}

	public void updatePlaylist() {
		// Removed existing playlist from database
		// removePlaylist(ID);

		try {
			CSVWriter writer = new CSVWriter(new FileWriter(PLAYLISTS_DATABASE, true)); // true
																						// flag
																						// for
																						// appending
																						// to
																						// end
																						// of
																						// file
			// String songList = arrayListParser(songList);
			// record: ["Playlist ID:", playlistId, "Playlist Name:",
			// playlistName, "Date Created:", createdDate, "Song ID's:",
			// songList]
			// String[] record = ("Playlist ID:" + "," + ID + "," + "Playlist
			// Name:" + "," + name + "," + "Date Created:" + "," + createDate +
			// "," + "Song ID's:" + "," + songList).split(",");
			// writer.writeNext(record);
			writer.flush();
			writer.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Reads in all playlists from playlists.csv
	// Removes the playlist based on playlistId
	// Rewrites playlists back into playlists.csv
	@SuppressWarnings({ "resource", "deprecation" })
	public static void removePlaylist(String ID) {
		try {
			// Build reader instance
			// Read playlists.csv
			// Default seperator is comma
			// Default quote character is double quote
			// Start reading from line number 1 (line numbers start from zero)
			CSVReader reader = new CSVReader(new FileReader(PLAYLISTS_DATABASE), ',', '"', 0);
			List<String[]> allRows = reader.readAll();

			// Iterator created for List of records
			// For each String[] record, if 0th element (playlistId) is the one
			// to be deleted, it is removed from List
			for (Iterator<String[]> iterator = allRows.listIterator(); iterator.hasNext();) {
				String[] record = iterator.next();
				if (record[1].equals(ID)) {
					iterator.remove();
				}
			}

			// Writes List of playlists back into playlists.csv
			CSVWriter writer = new CSVWriter(new FileWriter(PLAYLISTS_DATABASE));
			writer.writeAll(allRows);
			writer.flush();
			writer.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Converts ArrayList of Strings into a single comma seperated string
	// of all songs in playlist
	@SuppressWarnings("unused")
	private String arrayListParser(ArrayList<String> songs) {
		String collect = songList.stream().collect(Collectors.joining(","));
		return collect;
	}

	@SuppressWarnings({ "deprecation", "resource" })
	public ArrayList<String> getSongsInPlaylist(ArrayList<String> songs) {
		try {
			CSVReader reader = new CSVReader(new FileReader(PLAYLISTS_DATABASE), ',', '"', 0);
			List<String[]> allRows = reader.readAll();

			for (Iterator<String[]> iterator = allRows.listIterator(); iterator.hasNext();) {
				String[] record = iterator.next();
				// if (record[1].equals(ID)) {
				// Get songs and add to ArrayList
				for (int i = 7; i < record.length; i++) {
					songList.add(record[i]);
				}
				// }
			}
			return songList;
		} catch (Exception e) {
		}
		return songList;
	}

	@SuppressWarnings({ "deprecation", "resource" })
	private String[] getPlaylistNameandDate() {
		String[] nameAndDate = new String[2];
		try {
			CSVReader reader = new CSVReader(new FileReader(PLAYLISTS_DATABASE), ',', '"', 0);
			List<String[]> allRows = reader.readAll();
			for (Iterator<String[]> iterator = allRows.listIterator(); iterator.hasNext();) {
				String[] record = iterator.next();
				// if (record[1].equals(ID)) {
				nameAndDate[0] = record[3];
				nameAndDate[1] = record[5];
				// }
			}
			return nameAndDate;
		} catch (Exception e) {
		}

		return nameAndDate;
	}

	@SuppressWarnings("unchecked")
	public JSONObject getNameAsJSON() {
		JSONObject playlistJSON = new JSONObject();
		playlistJSON.put("name", name);
		return playlistJSON;
	}

	public void rename(String newName) {
		this.name = newName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getAsJSON() {
		JSONArray allSongsAsJSON = new JSONArray();
		for (Playable p : songs) {
			allSongsAsJSON.add(p.getAsJSON());
		}
		JSONObject obj = new JSONObject();
		obj.put("name", name);
		obj.put("songs", allSongsAsJSON);
		return obj;
	}
	
	public int getID() {
		return this.ID;
	}
}
