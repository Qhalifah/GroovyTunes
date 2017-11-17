package player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import user.User;
import utils.dbmgr.PlaylistOperations;
import utils.dbmgr.SongOperations;

public abstract class PlayerFactory {

	public static Player getPlayer(User user) {
		Player player = null;
		if (user.isPremiumUser()) {
			player = new PremiumPlayer(user.getUsername());
		} else {
			player = new RegularPlayer(user.getUsername());
		}
		List<Playable> playlist = null;
		List<Playable> songs = null;
		try {
			playlist = PlaylistOperations.populatePlaylist(user);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			playlist = new ArrayList<>();
			e.printStackTrace();
		}
		try {
			songs = SongOperations.getAllSongs();
		} catch (ClassNotFoundException | SQLException | IOException e) {
			songs = new ArrayList<>();
			e.printStackTrace();
		}
		player.populatePlaylists(playlist);
		player.populateSongs(songs);
		return player;
	}
}
