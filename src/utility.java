
import org.jaudiotagger.audio.*;
import org.jaudiotagger.tag.*;
import java.util.*;
import java.io.*;

public class utility {
	public static void main(String[] args) throws Exception {
		utility u = new utility();
		u.getMusic("../songs/Black_Ant_-_01_-_Fater_Lee.mp3");
	}

	public void getMusic(String dir) throws Exception {
		System.out.println("Getting music from " + dir);
        AudioFile f = AudioFileIO.read(new File(dir));
		Tag tag = f.getTag();
		AudioHeader a = f.getAudioHeader();
		System.out.println("Length: " + a.getTrackLength());
		Iterator iter = tag.getFields();
		while (iter.hasNext()) {
            TagField t = (TagField)iter.next();
			System.out.println(t.getId() + " " + t.toString());
		}
	}

}
