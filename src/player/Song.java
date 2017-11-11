package player;

import org.json.simple.JSONObject;

public class Song implements Playable {
	private String title;
	private String albumArtist;
	private String album;
	private String genre;
	private double duration;
	private int songID;
	private String URL;

	public Song(int ID, String title, String albumartist, String album, String genre, double duration, String URL) {
		this.songID = ID;
		this.title = title;
		this.albumArtist = albumartist;
		this.album = album;
		this.genre = genre;
		this.duration = duration;
		this.URL = URL;
	}

	private Song() {}

	public static Song getSong(String title, String albumartist, String album, String genre, double duration, String URL) {
		Song s = new Song();
		s.title = title;
		s.albumArtist = albumartist;
		s.album = album;
		s.genre = genre;
		s.duration = duration;
		s.URL = URL;
		return s;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject play() {
		JSONObject song = new JSONObject();
		song.put("id", songID);
		song.put("url", URL);
		return song;
	}

	@SuppressWarnings("unchecked")
	public JSONObject getMetaData() {
		JSONObject obj = new JSONObject();
		obj.put("title", this.title);
		obj.put("albumArtist", this.albumArtist);
		obj.put("album", this.album);
		obj.put("genre", this.genre);
		obj.put("duration", this.duration);
		obj.put("songID", this.songID);
		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getAsJSON() {
		JSONObject songJSON = new JSONObject();
		songJSON.put("id", songID);
		songJSON.put("title", title);
		return songJSON;
	}

	public String getTitle() {
		return title;
	}

	public String getAlbumArtist() {
		return albumArtist;
	}

	public String getAlbum() {
		return album;
	}

	public String getGenre() {
		return genre;
	}

	public double getDuration() {
		return duration;
	}

	public int getSongID() {
		return songID;
	}

	public String getURL() {
		return URL;
	}
}
