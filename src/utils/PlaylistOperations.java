package utils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import player.Playable;
import player.Playlist;
import player.Song;
import user.User;

public class PlaylistOperations {

	public static List<Playable> populatePlaylist(User user) throws ClassNotFoundException, SQLException, IOException {
		List<Playable> playlists = new ArrayList<>();
		String query = "SELECT playlist_ID, name FROM " + Constants.PLAYLIST_TABLE + " where username = ?";
		PreparedStatement stmt = GroovyConnection.getConnection().prepareStatement(query);
		stmt.setString(1, user.getUsername());
		ResultSet resultSet = stmt.executeQuery();
		while(resultSet.next()) {
			int id = resultSet.getInt("playlist_ID");
			String name = resultSet.getString("name");
			Playlist p = new Playlist(id, name);
			String songQuery = "SELECT * FROM " + Constants.SONG_TABLE + " WHERE ID IN (SELECT song_ID FROM " + Constants.PLAYLIST_SONGS + " WHERE playlist_ID = ?)";
			PreparedStatement statement = GroovyConnection.getConnection().prepareStatement(songQuery);
			statement.setInt(1, id);
			ResultSet songResultSet = statement.executeQuery();
			List<Playable> songs = new ArrayList<>();
			while(songResultSet.next()) {
				Song s = new Song(songResultSet.getInt("ID"),
						songResultSet.getString("title"),
						songResultSet.getString("artist"),
						songResultSet.getString("album"),
						songResultSet.getString("genre"),
						songResultSet.getDouble("duration"),
						songResultSet.getString("url"));
				songs.add(s);
			}
			p.populate(songs);
			playlists.add(p);
		}
		return playlists;
	}
	
	public static int createPlaylist(String user, String name, int id) throws ClassNotFoundException, SQLException, IOException {
		int ID = -1;
		String query = "INSERT INTO " + Constants.PLAYLIST_TABLE + "(playlist_ID, username, name) VALUES "
				+ "(?,?,?)";
		PreparedStatement statement = GroovyConnection.getConnection().prepareStatement(query);
		statement.setInt(1, id);
		statement.setString(2, user);
		statement.setString(3, name);
		try {
			ID = statement.executeUpdate();
		} catch (SQLException ex) {
			ID = -1;
		}
		return ID;
	}
}
