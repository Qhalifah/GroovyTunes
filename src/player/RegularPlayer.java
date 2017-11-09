package player;

import exceptions.IllegalOperationException;
import exceptions.PlaylistNotCreatedException;

public class RegularPlayer extends Player {

	@Override
	public void createPlaylist(String name) throws PlaylistNotCreatedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addSongToPlaylist(String name, String ID) throws IllegalOperationException {
		// TODO Auto-generated method stub
		return false;
	}
}
