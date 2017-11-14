package player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import utils.PlaylistOperations;

public class Playlist implements Playable {

	private String name;
	private int ID;
	private List<Playable> songs;

	public Playlist(int ID, String name) {
		this.ID = ID;
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject play() {
		JSONObject playlist = new JSONObject();
		JSONArray arr = new JSONArray();
		for (Playable p : songs) {
			arr.add(p.play());
		}
		playlist.put("songs", arr);
		return playlist;
	}

	public boolean addSong(Song s) throws ClassNotFoundException, SQLException, IOException {
		songs.add(s);
		return PlaylistOperations.addSong(s.getSongID(), this.ID);
	}

	public boolean removeSong(Song song) {
		boolean removed = false;
		for (int i = 0; i < songs.size(); ++i) {
			Song s = (Song) songs.get(i);
			if (s.getSongID() == song.getSongID()) {
				songs.remove(s);
				removed = true;
			}
		}
		return removed;
	}

	public void populate(List<Playable> songs) {
		this.songs = songs;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	public JSONObject getNameAsJSON() {
		JSONObject playlistJSON = new JSONObject();
		playlistJSON.put("name", name);
		return playlistJSON;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getAsJSON() {
		JSONArray allSongsAsJSON = new JSONArray();
		for (Playable p : songs) {
			allSongsAsJSON.add(p.getAsJSON());
		}
		JSONObject obj = new JSONObject();
		obj.put("name", name);
		obj.put("songs", allSongsAsJSON);
		return obj;
	}

	public int getID() {
		return this.ID;
	}
}
