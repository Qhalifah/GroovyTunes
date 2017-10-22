import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import com.opencsv.*;

public class Playlist{

	private static final String PLAYLISTS_DATABASE = "./databases/playlists.csv"; // Location of playlists.csv file
	private String playlistName;
	public String playlistId;
	public Date createDate;
	private ArrayList<String> songs;

	public Playlist(){
		this.playlistId = UUID.randomUUID().toString();
		this.songs = new ArrayList<String>();
		//updatePlaylist(this.playlistId, this.songs);
	}

	public Playlist(String playlistId){
		this.playlistId = playlistId;
		this.songs = new ArrayList<String>();
	}

	public void addSong(String songId){
		this.songs.add(songId);
		updatePlaylist(this.playlistId, this.songs);
	}

	public void removeSong(String songId){
		this.songs.remove(songId);
		updatePlaylist(this.playlistId, this.songs);
	}

	public String getName(){
		return this.playlistName;
	}

	public void setName(String name){
		this.playlistName = name;
	}

	public String share(){
		return "";
	}

	public static void updatePlaylist(String playlistId, ArrayList<String> songs){
			// Removed existing playlist from database
		removePlaylist(playlistId);

		try{
			CSVWriter writer = new CSVWriter(new FileWriter(PLAYLISTS_DATABASE, true)); // true flag for appending to end of file
			String songList = arrayListParser(songs);
			String[] record = ("playListId," + playlistId + ",songList," + songList).split(",");
			writer.writeNext(record);
			//writer.write("\"" + playlistId + "\"" + "," + "\"" + songList + "\"" + "\n")
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

			/* 			int index = -1;
			for(int i = 0; i < allRows.length; i++){
				if(allRows.get(i)[0].equals(playlistId)){
					index = i;
					break;
				}
			}
			if(index >= 0){
				allRows.remove(index);
			}
			*/

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
}
