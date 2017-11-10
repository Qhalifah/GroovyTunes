package utils;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import player.Song;

public class Utility {

	public static void updateSongDB(String directory, boolean clearPrevious) throws CannotReadException, IOException,
			TagException, ReadOnlyFileException, InvalidAudioFrameException, ClassNotFoundException, SQLException {
		Utility.log("Reading songs from directory");
		File[] files = new File(directory).listFiles();
		List<Song> songs = new ArrayList<>();
		for (File file : files) {
			songs.add(Utility.readSongFromFile(file));
		}
		if (clearPrevious) {
			Utility.log("Removing previous entries from DB");
			String clearQuery = "DELETE FROM " + Constants.SONG_TABLE;
			GroovyConnection.getConnection().prepareStatement(clearQuery).execute();
		}
		Utility.log("Preparing to update DB");
		String maxQuery = "SELECT MAX(ID) FROM " + Constants.SONG_TABLE;
		int id = 0;
		Statement stmt = GroovyConnection.getConnection().createStatement();
		stmt.execute(maxQuery);
		ResultSet resultSet = stmt.getResultSet();
		if (resultSet.next())
			id = resultSet.getInt(1);
		String insertQuery = "INSERT INTO " + Constants.SONG_TABLE
				+ "(ID, title, artist, genre, duration, album, url) VALUES " + "(?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement statement = GroovyConnection.getConnection().prepareStatement(insertQuery);
		Utility.log("Updating DB");
		for (Song song : songs) {
			id += 1;
			statement.setInt(1, id);
			statement.setString(2, song.getTitle());
			statement.setString(3, song.getAlbumArtist());
			statement.setString(4, song.getGenre());
			statement.setDouble(5, song.getDuration());
			statement.setString(6, song.getAlbum());
			statement.setString(7, song.getURL());
			statement.execute();
		}

	}

	public static Song readSongFromFile(File file)
			throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		AudioFile f = AudioFileIO.read(file);
		Tag tag = f.getTag();
		AudioHeader a = f.getAudioHeader();
		Song song = new Song(tag.getFirst(FieldKey.TITLE).replaceAll(",", ";"),
				tag.getFirst(FieldKey.ALBUM).replaceAll(",", ";"),
				tag.getFirst(FieldKey.ALBUM_ARTIST).replaceAll(",", ";"),
				tag.getFirst(FieldKey.GENRE).replaceAll(",", ";"), a.getTrackLength(), file.getAbsolutePath());
		return song;
	}

	public static void log(String message) {
		System.out.println("[GROOVY TUNES]: " + message);
	}
}
