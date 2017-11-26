package player;

import java.io.IOException;
import java.sql.SQLException;

import exceptions.IllegalOperationException;
import exceptions.PlaylistNotCreatedException;
import utils.Reason;
import utils.dbmgr.PlaylistOperations;

public class PremiumPlayer extends Player {

	public PremiumPlayer(String username) {
		this.username = username;
	}

	@Override
	public void createPlaylist(String name) throws PlaylistNotCreatedException {
		try {
			if (PlaylistOperations.createPlaylist(username, name) == -1)
				throw new PlaylistNotCreatedException(Reason.UNKNOWN);
			Playlist p = new Playlist(playlists.size(), name);
			playlists.add(p);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			throw new PlaylistNotCreatedException(Reason.UNKNOWN, e);
		}

	}

	@Override
	public boolean addSongToPlaylist(String playlistName, Song song) throws IllegalOperationException {
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
			if (((Song) songs.get(i)).getSongID() == song.getSongID()) {
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

}
