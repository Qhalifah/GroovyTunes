
import org.jaudiotagger.audio.*;
import org.jaudiotagger.tag.*;
import java.util.*;
import java.io.*;
import org.json.simple.*;
import com.opencsv.*;
import java.util.stream.Collectors;


public class utility {
	private static final String SONGS_DATABASE = "./databases/songs.csv";


	public static void main(String args[])throws Exception{
		utility u = new utility();
		new File(SONGS_DATABASE).delete();
		u.updateSongDB("./songs");
	}

	public JSONObject getMusic() throws Exception {
		JSONObject songs = new JSONObject();

		CSVReader reader = new CSVReader(new FileReader(SONGS_DATABASE), ',', '"', 0);
		List<String[]> allRows = reader.readAll();
		for (Iterator<String[]> iterator = allRows.listIterator(); iterator.hasNext(); ){
			String[] record = iterator.next();
			Song song = createSong(record);
			songs.put(record[13], song.getMetaData());
		}
		return songs;
	}

	public ArrayList<Song> getAllSongs() throws Exception {
		ArrayList<Song> songs = new ArrayList<Song>();

		CSVReader reader = new CSVReader(new FileReader(SONGS_DATABASE), ',', '"', 0);
		List<String[]> allRows = reader.readAll();
		for (Iterator<String[]> iterator = allRows.listIterator(); iterator.hasNext(); ){
			String[] record = iterator.next();
			Song song = createSong(record);
		}
		return songs;
	}

	public void updateSongDB(String dir) throws Exception {
		File[] files = new File(dir).listFiles();
		ArrayList<Song> songs = new ArrayList<Song>();
		for(File file: files){
			Song song = createSong(file);
			song.addToDb(file.getPath());
		}
	}

	public Song createSong(String[] record) throws Exception{
		Song song = new Song(
			record[3],
			record[5],
			record[7],
			record[9],
			Double.parseDouble(record[11]),
			record[1]
		);
		return song;
	}

	public Song createSong(File file) throws Exception{
		AudioFile f = AudioFileIO.read(file);
		Tag tag = f.getTag();
		AudioHeader a = f.getAudioHeader();
		Song song = new Song(
			tag.getFirst(FieldKey.TITLE).replaceAll(",", ";"),
			tag.getFirst(FieldKey.ALBUM).replaceAll(",", ";"),
			tag.getFirst(FieldKey.ALBUM_ARTIST).replaceAll(",", ";"),
			tag.getFirst(FieldKey.GENRE).replaceAll(",", ";"),
			a.getTrackLength()
		);
		return song;
	}



}
