package player;

import java.io.IOException;
import java.sql.SQLException;

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
			if(PlaylistOperations.createPlaylist(username, name, playlists.size()) != 1)
				throw new PlaylistNotCreatedException(Reason.UNKNOWN);
			Playlist p = new Playlist(playlists.size(), name);
			playlists.add(p);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			throw new PlaylistNotCreatedException(Reason.UNKNOWN, e);
		}
		
	}

	@Override
	public boolean addSongToPlaylist(String playlistName, int songID) throws IllegalOperationException {
		// TODO Auto-generated method stub
		return false;
	}

}
