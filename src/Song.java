import org.json.simple.*;
import java.util.*;
public class Song{
	public String title;
	public String albumartist;
	public String album;
	public String genre;
	public double duration;
	public String songId;


	public Song(String title, String albumartist, String album, String genre, double duration){
		this.title = title;
		this.albumartist = albumartist;
		this.album = album;
		this.genre = genre;
		this.duration = duration;
		this.songId = UUID.randomUUID().toString();
	}


	public JSONObject getMetaData(){
		JSONObject obj = new JSONObject();
		obj.put("title", this.title);
		obj.put("albumartist", this.albumartist);
		obj.put("album", this.album);
		obj.put("genre", this.genre);
		obj.put("duration", this.duration);
		obj.put("songId", this.songId);
		return obj;
	}



}
