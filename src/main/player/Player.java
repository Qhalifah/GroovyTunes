package player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exceptions.IllegalOperationException;
import exceptions.PlaylistNotCreatedException;
import utils.Reason;
import utils.dbmgr.PlaylistOperations;

public abstract class Player {
	protected List<Playable> songs;
	protected List<Playable> playlists;
	protected String username;

	public List<Playable> getAllSongs() {
		return songs;
	}

	public void populatePlaylists(List<Playable> playlists) {
		this.playlists = playlists;
	}

	public void populateSongs(List<Playable> songs) {
		this.songs = songs;
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
			songsJSON.add(song.getMetaData());
		}
		return songsJSON;
	}

	public abstract void createPlaylist(String name) throws PlaylistNotCreatedException;

	public abstract boolean addSongToPlaylist(String playlistName, Song songID) throws IllegalOperationException;

	public boolean removeSongFromPlaylist(String name, Song song) {
		boolean removed = false;
		for (int i = 0; i < playlists.size(); ++i) {
			Playlist p = (Playlist) playlists.get(i);
			if (p.getName().equals(name)) {
				try {
					PlaylistOperations.removeSong(p.getID(), song.getSongID());
					removed = p.removeSong(song);
				} catch (ClassNotFoundException | SQLException | IOException e) {
					e.printStackTrace();
				}
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
				try {
					System.out.println("LOGGER: playlist ID: " + playlist.getID());
					PlaylistOperations.renamePlaylist(playlist.getID(), newName);
					playlist.setName(newName);
					renamed = true;
				} catch (ClassNotFoundException | SQLException | IOException e) {
					renamed = false;
					System.out.println("LOGGER: playlist: Error in SQL");
					playlist.setName(name);
					e.printStackTrace();
				}
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
				try {
					PlaylistOperations.removePlaylist(playlist.getID(), username);
					playlists.remove(i);
					removed = true;
				} catch (ClassNotFoundException | SQLException | IOException e) {
					e.printStackTrace();
				}
			}
		}
		return removed;
	}
	
	public String getSharableLink(String name) {
		Playlist playlist = null;
		for(int i = 0; i < playlists.size(); ++i) {
			Playlist p = (Playlist) playlists.get(i);
			if(p.getName().equals(name)) {
				playlist = p;
				break;
			}
		}
		return playlist.getID() + "";
	}
	
	public boolean addSharedPlaylist(String shareID) {
		try {
			int id = Integer.parseInt(shareID);
			String name = PlaylistOperations.getPlaylistNameByID(id);
			for(Playable p: playlists) {
				Playlist playlist = (Playlist) p;
				if(playlist.getName().equals(name)) {
					return false;
				}
			}
			this.createPlaylist(name);
			Playlist playlist = this.getPlaylist(name);
			List<Playable> newSongs = null;
			newSongs = PlaylistOperations.getAllSongsByID(id);
			for (int i = 0; i < newSongs.size(); ++i) {
				playlist.addSong((Song) newSongs.get(i));
			}
			return true;
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
			return false;
		} catch (PlaylistNotCreatedException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Playlist getPlaylist(String name) {
		for(int i = 0; i < playlists.size(); ++i) {
			Playlist p = (Playlist) playlists.get(i);
			if(p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}

	public Song getSong(Song song) {
		for(int i = 0; i < songs.size(); ++i) {
			Song p = (Song) songs.get(i);
			if(p.getSongID() == song.getSongID()) {
				return p;
			}
		}
		return null;
	}
}
