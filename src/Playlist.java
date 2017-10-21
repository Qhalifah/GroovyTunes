import java.util.Date;
public class Playlist{
	
	private String playlistName;
	public String playlistId;
	public Date createDate;
	
	public Playlist(){
		
	}

	public void addSong(String songId){
		
	}
	
	public void removeSong(String songId){
		
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