package player;

import java.io.IOException;
import java.sql.SQLException;

import exceptions.IllegalOperationException;
import exceptions.PlaylistNotCreatedException;
import utils.Constants;
import utils.PlaylistOperations;
import utils.Reason;

public class RegularPlayer extends Player {

	public RegularPlayer(String username) {
		this.username = username;
	}

	@Override
	public void createPlaylist(String name) throws PlaylistNotCreatedException {
		if (playlists.size() == Constants.MAX_PLAYLISTS)
			throw new PlaylistNotCreatedException(Reason.MAX_COUNT_REACHED);
		try {
			PlaylistOperations.createPlaylist(username, name);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			throw new PlaylistNotCreatedException(Reason.UNKNOWN, e);
		}
		Playlist p = new Playlist(playlists.size(), name);
		this.playlists.add(p);
	}

	@Override
	public boolean addSongToPlaylist(String playlistName, Song song) throws IllegalOperationException {
		if (songs.size() == Constants.MAX_SONGS)
			throw new IllegalOperationException();
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
