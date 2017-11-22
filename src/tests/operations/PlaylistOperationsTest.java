package operations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import user.User;
import utils.Constants;
import utils.dbmgr.PlaylistOperations;
import utils.dbmgr.UserOperations;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlaylistOperationsTest {

	int playlistID;

	@BeforeClass
	public static void addUser() throws ClassNotFoundException, SQLException, IOException {
		UserOperations.createUser("test-user", "test-password", "test-firstname", "test-lastname", "1993-01-01");
	}

	@Test
	public void testA_CreatePlaylist() throws ClassNotFoundException, SQLException, IOException {
		playlistID = PlaylistOperations.createPlaylist("test-user", "test-playlist");
		assertFalse(playlistID == -1);
	}

	@Test
	public void testB_AddSong() throws ClassNotFoundException, SQLException, IOException {
		if (playlistID == -1)
			assertTrue(false);
		boolean added = PlaylistOperations.addSong(1, playlistID);
		assertTrue(added);
	}

	@Test
	public void testC_PopulatePlaylist() throws ParseException, ClassNotFoundException, SQLException, IOException {
		if(playlistID == -1)
			assertTrue(false);
		DateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
		User u = new User("test-user", "test-password", "test-firstname", "test-lastname", df.parse("1993-01-01"), new Date(), false);
		boolean size = PlaylistOperations.populatePlaylist(u).size() == 1;
		assertTrue(size);
	}
	
	@Test
	public void testD_RenamePlaylist() throws ClassNotFoundException, SQLException, IOException {
		if(playlistID == -1)
			assertTrue(false);
		int playlist = PlaylistOperations.renamePlaylist(playlistID, "test-new-playlist");
		assertTrue(playlist == 1);
	}
	
	@Test
	public void testE_GetAllSongsByID() throws ClassNotFoundException, SQLException, IOException {
		if(playlistID == -1)
			assertTrue(false);
		boolean songs = PlaylistOperations.getAllSongsByID(playlistID).size() == 1;
		assertTrue(songs);
	}
	
	@Test
	public void testF_RemoveSong() throws ClassNotFoundException, SQLException, IOException {
		if(playlistID == -1)
			assertTrue(false);
		boolean song = PlaylistOperations.removeSong(playlistID, 1);
		assertTrue(song);
	}
	
	@Test
	public void testG_RemovePlaylist() throws ClassNotFoundException, SQLException, IOException {
		if(playlistID == -1)
			assertTrue(false);
		PlaylistOperations.addSong(1, playlistID);
		boolean playlist = PlaylistOperations.removePlaylist(playlistID, "test-user");
		assertTrue(playlist);
	}
	
	@AfterClass
	public static void removeUser() throws ClassNotFoundException, SQLException, IOException {
		UserOperations.removeUser("test-user");
	}
}
