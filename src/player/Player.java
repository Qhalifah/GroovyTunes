package player;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exceptions.IllegalOperationException;
import exceptions.PlaylistNotCreatedException;
import utils.Reason;

public abstract class Player {
	protected List<Playable> songs;
	protected List<Playable> playlists;

	public List<Playable> getAllSongs() {
		return songs;
	}

	@SuppressWarnings("unchecked")
	public JSONArray getPlaylistsAsJSON() {
		JSONArray playlistsJSON = new JSONArray();
		for (Playable p : playlists) {
			Playlist playlist = (Playlist) p;
			playlistsJSON.add(playlist.getNameAsJSON());
		}
		return playlistsJSON;
	}

	@SuppressWarnings("unchecked")
	public JSONArray getAllSongsAsJSON() {
		JSONArray songsJSON = new JSONArray();
		for (Playable p : songs) {
			Song song = (Song) p;
			songsJSON.add(song.getAsJSON());
		}
		return songsJSON;
	}

	public abstract void createPlaylist(String name) throws PlaylistNotCreatedException;

	public abstract boolean addSongToPlaylist(String name, String ID) throws IllegalOperationException;

	public boolean removeSongFromPlaylist(String name, String ID) {
		boolean removed = false;
		for (int i = 0; i < playlists.size(); ++i) {
			Playlist p = (Playlist) playlists.get(i);
			if (p.getName().equals(name)) {
				removed = p.removeSong(ID);
			}
		}
		return removed;
	}

	public boolean renamePlaylist(String name, String newName) throws PlaylistNotCreatedException {
		boolean renamed = false;
		for (Playable p : playlists) {
			Playlist playlist = (Playlist) p;
			if (playlist.getName().equals(newName)) {
				throw new PlaylistNotCreatedException(Reason.ALREADY_EXISTS);
			}
		}
		for (int i = 0; i < playlists.size(); ++i) {
			Playlist playlist = (Playlist) playlists.get(i);
			if (playlist.getName().equals(name)) {
				playlist.rename(newName);
				renamed = true;
			}
		}
		return renamed;
	}

	public JSONObject getSongsInPlaylistAsJSON(String name) {
		JSONObject songsInPlaylistJSON = null;
		for (Playable p : playlists) {
			Playlist playlist = (Playlist) p;
			if (playlist.getName().equals(name)) {
				songsInPlaylistJSON = playlist.getAsJSON();
			}
		}
		return songsInPlaylistJSON;

	}

	public boolean removePlaylist(String name) {
		boolean removed = false;
		for (int i = 0; i < playlists.size(); ++i) {
			Playlist playlist = (Playlist) playlists.get(i);
			if (playlist.getName().equals(name)) {
				playlists.remove(i);
				removed = false;
			}
		}
		return removed;
	}
}
