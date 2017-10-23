import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import com.opencsv.*;
import org.json.simple.*;

public class Playlist{

	private static final String PLAYLISTS_DATABASE = "./databases/playlists.csv"; // Location of playlists.csv file
	private String playlistName;
	public String playlistId;
	public String createDate;
	private ArrayList<String> songs;

	public Playlist(){
		this.playlistId = UUID.randomUUID().toString();
		this.songs = new ArrayList<String>();
		this.createDate = new Date().toString();
		this.updatePlaylist();
	}

	public Playlist(String playlistId){
		this.playlistId = playlistId;
		this.songs = new ArrayList<String>(); // Must read in songs from old playlist
		this.songs = getSongsInPlaylist(this.songs);
		this.playlistName = getPlaylistNameandDate()[0];
		this.createDate = getPlaylistNameandDate()[1];
	}

	public void addSong(String songId){
		this.songs.add(songId);
		this.updatePlaylist();
	}

	public JSONObject toJSON(){
		JSONObject playlist = new JSONObject();
		playlist.put("playlistName", this.playlistName);
		playlist.put("playlistId", this.playlistId);
		playlist.put("createDate", this.createDate);
		JSONArray jSongs = new JSONArray();
		for(String s : songs){
			jSongs.add(s);
		}
		playlist.put("songs", jSongs);
		return playlist;
	}

	public void removeSong(String songId){
		this.songs.remove(songId);
		this.updatePlaylist();
	}

	public String getName(){
		return this.playlistName;
	}

	public void setName(String name){
		this.playlistName = name;
		this.updatePlaylist();
	}

	public String share(){
		return "";
	}


	public void updatePlaylist(){
			// Removed existing playlist from database
		removePlaylist(playlistId);

		try{
			CSVWriter writer = new CSVWriter(new FileWriter(PLAYLISTS_DATABASE, true)); // true flag for appending to end of file
			String songList = arrayListParser(songs);
				// record: ["Playlist ID:", playlistId, "Playlist Name:", playlistName, "Date Created:", createdDate, "Song ID's:", songList]
			String[] record = ("Playlist ID:" + "," + playlistId + "," + "Playlist Name:" + "," + playlistName + "," + "Date Created:" + "," + createDate + "," + "Song ID's:" + "," + songList).split(",");
			writer.writeNext(record);
			writer.flush();
			writer.close();
		}

		catch(Exception e){
			e.printStackTrace();
		}

	}

		// Reads in all playlists from playlists.csv
		// Removes the playlist based on playlistId
		// Rewrites playlists back into playlists.csv
	public static void removePlaylist(String playlistId){
		try{
				//Build reader instance
				//Read playlists.csv
				//Default seperator is comma
				//Default quote character is double quote
				//Start reading from line number 1 (line numbers start from zero)
			CSVReader reader = new CSVReader(new FileReader(PLAYLISTS_DATABASE), ',', '"', 0);
			List<String[]> allRows = reader.readAll();

				// Iterator created for List of records
				// For each String[] record, if 0th element (playlistId) is the one to be deleted, it is removed from List
			for (Iterator<String[]> iterator = allRows.listIterator(); iterator.hasNext(); ){
				String[] record = iterator.next();
				if (record[1].equals(playlistId)) {
					iterator.remove();
				}
			}

				// Writes List of playlists back into playlists.csv
			CSVWriter writer = new CSVWriter(new FileWriter(PLAYLISTS_DATABASE));
			writer.writeAll(allRows);
			writer.flush();
            writer.close();
		}

		catch(Exception e){
			e.printStackTrace();
		}
	}
		// Converts ArrayList of Strings into a single comma seperated string
		// of all songs in playlist
	private static String arrayListParser(ArrayList<String> songs){
		String collect = songs.stream().collect(Collectors.joining(","));
		return collect;
	}

	public ArrayList<String> getSongsInPlaylist(ArrayList<String> songs){
		try{
			CSVReader reader = new CSVReader(new FileReader(PLAYLISTS_DATABASE), ',', '"', 0);
			List<String[]> allRows = reader.readAll();

			for (Iterator<String[]> iterator = allRows.listIterator(); iterator.hasNext(); ){
				String[] record = iterator.next();
				if (record[1].equals(playlistId)) {
					// Get songs and add to ArrayList
					for(int i = 7; i < record.length; i++){
						songs.add(record[i]);
					}
				}
			}			
			return songs;
		}
		catch (Exception e){	
		}	
		return songs;
	}
	
	private String[] getPlaylistNameandDate(){
		String[] nameAndDate = new String[2];
		try{
			CSVReader reader = new CSVReader(new FileReader(PLAYLISTS_DATABASE), ',', '"', 0);
			List<String[]> allRows = reader.readAll();			
			for (Iterator<String[]> iterator = allRows.listIterator(); iterator.hasNext(); ){
				String[] record = iterator.next();
				if (record[1].equals(playlistId)) {
					nameAndDate[0] = record[3];
					nameAndDate[1] = record[5];
				}
			}			
			return nameAndDate;
		}
		catch (Exception e){	
		}
		
		return nameAndDate;
	}
}
