import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class Playlist{

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
	
	public static void updatePlaylist(String playlistId, ArrayList<String> songs) throws IOException{
		FileWriter writer = new FileWriter("./databases/playlists.csv");
		String songList = arrayListParser(songs);
		writer.write("\"playlistId\"," + "\"" + songList + "\"" + "\n")
		writer.flush();
		writer.close();
		
	}
	
	public void updatePlaylist(String playlistId){
		
	}
	
	public static void removePlaylist(){
		
	}
	
	private static String arrayListParser(ArrayList<String> songs){
		String collect = songs.stream().collect(Collectors.joining(","));
		return collect;
	}
}
