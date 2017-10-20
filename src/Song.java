import org.json.simple.JSONObject;
public class Song{
	public String title;
	public String albumartist;
	public String album;
	public String genre;
	public double duration;
	public String songId;
	public static int nextId = 1;


	public String getMetaData(){
		JSONObject obj = new JSONObject();
		obj.put("title", this.title);
		obj.put("albumartist", this.albumartist);
		obj.put("album", this.album);
		obj.put("genre", this.genre);
		obj.put("songId", this.songId);
		return obj.toString();
	}



}
