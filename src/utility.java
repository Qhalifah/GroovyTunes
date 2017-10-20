
import org.jaudiotagger.audio.*;
import org.jaudiotagger.tag.*;
import java.util.*;
import java.io.*;


public class utility {


	public void getMusic(String dir) throws Exception {
		System.out.println("Getting music from " + dir);
		File[] files = new File(dir).listFiles();
		for(File file: files){
			Song song = createSong(file);
			System.out.println(song.getMetaData());
		}
	}

	public Song createSong(File file) throws Exception{
		AudioFile f = AudioFileIO.read(file);
		Tag tag = f.getTag();
		AudioHeader a = f.getAudioHeader();
		Song song = new Song();
		song.title = tag.getFirst(FieldKey.TITLE);
		song.albumartist = tag.getFirst(FieldKey.ALBUM);
		song.album = tag.getFirst(FieldKey.ALBUM_ARTIST);
		song.genre = tag.getFirst(FieldKey.GENRE);
		song.duration = a.getTrackLength();
		song.songId = "" + song.nextId;
		song.nextId++;
		return song;
	}

}
