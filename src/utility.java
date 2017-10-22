
import org.jaudiotagger.audio.*;
import org.jaudiotagger.tag.*;
import java.util.*;
import java.io.*;
import org.json.simple.*;


public class utility {


	public JSONObject getMusic(String dir) throws Exception {
		System.out.println("Getting music from " + dir);
		File[] files = new File(dir).listFiles();
		JSONObject songs = new JSONObject();
		for(File file: files){
			Song song = createSong(file);
			songs.put(file.getPath(), song.getMetaData());
		}
		return songs;
	}

	public ArrayList<Song> getAllSongs(String dir) throws Exception {
		System.out.println("Getting music from " + dir);
		File[] files = new File(dir).listFiles();
		ArrayList<Song> songs = new ArrayList<Song>();
		for(File file: files){
			Song song = createSong(file);
			songs.add(song);
		}
		return songs;
	}

	public Song createSong(File file) throws Exception{
		AudioFile f = AudioFileIO.read(file);
		Tag tag = f.getTag();
		AudioHeader a = f.getAudioHeader();
		Song song = new Song(
			tag.getFirst(FieldKey.TITLE),
			tag.getFirst(FieldKey.ALBUM),
			tag.getFirst(FieldKey.ALBUM_ARTIST),
			tag.getFirst(FieldKey.GENRE),
			a.getTrackLength()
		);
		return song;
	}



}
