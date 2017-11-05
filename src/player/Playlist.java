package player;

import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import com.opencsv.*;
import org.json.simple.*;

public class Playlist implements Playable {

	private static final String PLAYLISTS_DATABASE = "./databases/playlists.csv"; // Location of playlists.csv file
	private String name;
	private String ID;
	public String createDate;
	private ArrayList<String> songList;
	private List<Playable> songs;

	public Playlist(){
		this.ID = UUID.randomUUID().toString();
		this.songList = new ArrayList<String>();
		this.createDate = new Date().toString();
		this.updatePlaylist();
	}

	public Playlist(String ID){
		this.ID = ID;
		this.songList = new ArrayList<String>(); // Must read in songs from old playlist
		this.songList = getSongsInPlaylist(this.songList);
		this.name = getPlaylistNameandDate()[0];
		this.createDate = getPlaylistNameandDate()[1];
	}

	public void play() {

	}

	public void addSong(String songId){
		this.songs.add(songId);
		this.updatePlaylist();
	}

	public JSONObject toJSON(){
		JSONObject playlist = new JSONObject();
		playlist.put("playlistName", this.name);
		playlist.put("playlistId", this.ID);
		playlist.put("createDate", this.createDate);
		JSONArray jSongs = new JSONArray();
		for(String s : songList){
			jSongs.add(s);
		}
		playlist.put("songs", jSongs);
		return playlist;
	}

	public void removeSong(String songId){
		this.songList.remove(songId);
		this.updatePlaylist();
	}

	public String getName() {
		return this.name;
	}

	public String getID() {
		return this.ID;
	}

	public void setName(String name){
		this.name = name;
		this.updatePlaylist();
	}

	public String share(){
		return "";
	}


	public void updatePlaylist(){
			// Removed existing playlist from database
		removePlaylist(ID);

		try{
			CSVWriter writer = new CSVWriter(new FileWriter(PLAYLISTS_DATABASE, true)); // true flag for appending to end of file
			String songList = arrayListParser(songList);
				// record: ["Playlist ID:", playlistId, "Playlist Name:", playlistName, "Date Created:", createdDate, "Song ID's:", songList]
			String[] record = ("Playlist ID:" + "," + ID + "," + "Playlist Name:" + "," + name + "," + "Date Created:" + "," + createDate + "," + "Song ID's:" + "," + songList).split(",");
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
	public static void removePlaylist(String ID){
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

		catch(Exception e){
			e.printStackTrace();
		}
	}
		// Converts ArrayList of Strings into a single comma seperated string
		// of all songs in playlist
	private String arrayListParser(ArrayList<String> songs){
		String collect = songList.stream().collect(Collectors.joining(","));
		return collect;
	}

	public ArrayList<String> getSongsInPlaylist(ArrayList<String> songs){
		try{
			CSVReader reader = new CSVReader(new FileReader(PLAYLISTS_DATABASE), ',', '"', 0);
			List<String[]> allRows = reader.readAll();

			for (Iterator<String[]> iterator = allRows.listIterator(); iterator.hasNext(); ){
				String[] record = iterator.next();
				if (record[1].equals(ID)) {
					// Get songs and add to ArrayList
					for(int i = 7; i < record.length; i++){
						songList.add(record[i]);
					}
				}
			}			
			return songList;
		}
		catch (Exception e){	
		}	
		return songList;
	}
	
	private String[] getPlaylistNameandDate(){
		String[] nameAndDate = new String[2];
		try{
			CSVReader reader = new CSVReader(new FileReader(PLAYLISTS_DATABASE), ',', '"', 0);
			List<String[]> allRows = reader.readAll();			
			for (Iterator<String[]> iterator = allRows.listIterator(); iterator.hasNext(); ){
				String[] record = iterator.next();
				if (record[1].equals(ID)) {
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
