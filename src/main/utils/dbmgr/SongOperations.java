package utils.dbmgr;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import player.Playable;
import player.Song;
import utils.Constants;

public class SongOperations {

	public static List<Playable> getAllSongs() throws ClassNotFoundException, SQLException, IOException {
		List<Playable> songs = new ArrayList<>();
		String query = "SELECT * FROM " + Constants.SONG_TABLE;
		PreparedStatement statement = GroovyConnection.getConnection().prepareStatement(query);
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			Song s = new Song(resultSet.getInt("ID"), 
					resultSet.getString("title"), 
					resultSet.getString("artist"),
					resultSet.getString("album"), 
					resultSet.getString("genre"), 
					resultSet.getDouble("duration"),
					resultSet.getString("url"));
			songs.add(s);
		}
		return songs;
	}
}
