import java.util.*;
public class Playlist{

	private String playlistName;
	public String playlistId;
	public Date createDate;
	private ArrayList<String> songs;

	public Playlist(){
		this.playlistId = UUID.randomUUID().toString();
		songs = new ArrayList<String>();
	}

	public void addSong(String songId){
		songs.add(songId);
	}

	public void removeSong(String songId){
		songs.remove(songId);
	}

	public String getName(){
		return playlistName;
	}

	public void setName(String name){
		playlistName = name;
	}

	public String share(){
		return "";
	}
}
