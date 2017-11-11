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
		if(playlists.size() == Constants.MAX_PLAYLISTS)
			throw new PlaylistNotCreatedException(Reason.MAX_COUNT_REACHED);
		try {
			PlaylistOperations.createPlaylist(username, name, playlists.size());
		} catch (ClassNotFoundException | SQLException | IOException e) {
			throw new PlaylistNotCreatedException(Reason.UNKNOWN, e);
		}
		Playlist p = new Playlist(playlists.size(), name);
		this.playlists.add(p);
	}

	@Override
	public boolean addSongToPlaylist(String name, int ID) throws IllegalOperationException {
		// TODO Auto-generated method stub
		return false;
	}
}
