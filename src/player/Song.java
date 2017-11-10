package player;

import org.json.simple.JSONObject;

public class Song implements Playable {
	private String title;
	private String albumArtist;
	private String album;
	private String genre;
	private double duration;
	private String songID;
	private String URL;

	public Song(String title, String albumartist, String album, String genre, double duration, String URL) {
		this.title = title;
		this.albumArtist = albumartist;
		this.album = album;
		this.genre = genre;
		this.duration = duration;
		this.URL = URL;
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

	public String getSongID() {
		return songID;
	}

	public String getURL() {
		return URL;
	}
}
