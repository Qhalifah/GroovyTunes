package player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import exceptions.IllegalOperationException;
import exceptions.PlaylistNotCreatedException;
import utils.PlaylistOperations;
import utils.Reason;

public class PremiumPlayer extends Player {

	public PremiumPlayer(String username) {
		this.username = username;
	}

	@Override
	public void createPlaylist(String name) throws PlaylistNotCreatedException {
		try {
			if (PlaylistOperations.createPlaylist(username, name) != 1)
				throw new PlaylistNotCreatedException(Reason.UNKNOWN);
			Playlist p = new Playlist(playlists.size(), name);
			playlists.add(p);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			throw new PlaylistNotCreatedException(Reason.UNKNOWN, e);
		}

	}

	@Override
	public boolean addSongToPlaylist(String playlistName, int songID) throws IllegalOperationException {
		Playlist playlist = null;
		boolean added = false;
		for (int i = 0; i < playlists.size(); ++i) {
			Playlist p = (Playlist) playlists.get(i);
			if (p.getName().equals(playlistName))
				playlist = p;
		}
		if (playlist == null)
			throw new IllegalOperationException();
		for (int i = 0; i < songs.size(); ++i) {
			if (((Song) songs.get(i)).getSongID() == songID) {
				try {
					playlist.addSong((Song) songs.get(i));
					added = true;
				} catch (ClassNotFoundException | SQLException | IOException e) {
					e.printStackTrace();
				}
			}
		}
		return added;
	}

	@Override
	public boolean addSharedPlaylist(int id) {
		try {
			String name = PlaylistOperations.getPlaylistNameByID(id);
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

}
