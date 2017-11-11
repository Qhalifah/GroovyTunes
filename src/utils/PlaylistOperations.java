package utils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	public static int createPlaylist(String user, String name) throws ClassNotFoundException, SQLException, IOException {
		int ID = -1;
		String query = "SELECT MAX(playlist_ID) FROM " + Constants.PLAYLIST_TABLE;
		Statement stmt = GroovyConnection.getConnection().createStatement();
		ResultSet set = stmt.executeQuery(query);
		if(set.next()) {
			ID = set.getInt(1);
		} else {
			ID = 1;
		}
		query = "INSERT INTO " + Constants.PLAYLIST_TABLE + "(playlist_ID, username, name) VALUES "
				+ "(?,?,?)";
		PreparedStatement statement = GroovyConnection.getConnection().prepareStatement(query);
		statement.setInt(1, ID);
		statement.setString(2, user);
		statement.setString(3, name);
		try {
			ID = statement.executeUpdate();
		} catch (SQLException ex) {
			ID = -1;
		}
		return ID;
	}

	public static boolean addSong(int songID, int id) throws ClassNotFoundException, SQLException, IOException {
		boolean inserted = false;
		String query = "INSERT INTO " + Constants.PLAYLIST_SONGS + "(playlist_ID, song_ID) VALUES "
				+ "(?,?)";
		PreparedStatement statement = GroovyConnection.getConnection().prepareStatement(query);
		if(statement.executeUpdate() == 1)
			inserted = true;
		return inserted;
	}

	public static void renamePlaylist(int ID, String newName) throws ClassNotFoundException, SQLException, IOException {
		String query = "UPDATE " + Constants.PLAYLIST_TABLE + " SET name = ? WHERE playlist_ID = ?";
		PreparedStatement statement = GroovyConnection.getConnection().prepareStatement(query);
		statement.setString(1, newName);
		statement.setInt(1, ID);
		statement.executeUpdate();
	}

	public static void removeSong(int playlistID, int songID) throws ClassNotFoundException, SQLException, IOException {
		String query = "DELETE FROM " + Constants.PLAYLIST_SONGS + " WHERE song_ID = ? AND playlist_ID = ?";
		PreparedStatement statement = GroovyConnection.getConnection().prepareStatement(query);
		statement.setInt(1, songID);
		statement.setInt(2, playlistID);
		statement.executeUpdate();
	}

	public static void removePlaylist(int playlistID, String username) throws ClassNotFoundException, SQLException, IOException {
		// Remove a playlist from user's accoutn
		String query = "DELETE FROM " + Constants.PLAYLIST_TABLE + " WHERE playlist_ID = ? AND username = ?";
		PreparedStatement statement = GroovyConnection.getConnection().prepareStatement(query);
		statement.setInt(1, playlistID);
		statement.setString(2, username);
		statement.executeUpdate();
		// Remove all songs in the playlist
		query = "DELETE FROM " + Constants.PLAYLIST_SONGS + " WHERE playlist_ID = ?";
		statement = GroovyConnection.getConnection().prepareStatement(query);
		statement.setInt(1, playlistID);
		statement.executeUpdate();
	}
}

