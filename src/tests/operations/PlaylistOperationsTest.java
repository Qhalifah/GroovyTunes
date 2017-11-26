package operations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import auth.Authenticate;
import auth.Authenticate.AUTH_RESULT;
import auth.Authenticate.AuthResult;
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
		System.out.println("Playlist created with id = " + playlistID);
		assertFalse(playlistID == -1);
		assertTrue(PlaylistOperations.removePlaylist(playlistID, "test-user"));
	}

	@Test
	public void testB_AddSong() throws ClassNotFoundException, SQLException, IOException {
		playlistID = PlaylistOperations.createPlaylist("test-user", "test-playlist");
		boolean added = PlaylistOperations.addSong(1, playlistID);
		added = added & PlaylistOperations.removePlaylist(playlistID, "test-user");
		assertTrue(added);
	}

	@Test
	public void testC_PopulatePlaylist() throws ParseException, ClassNotFoundException, SQLException, IOException {
		playlistID = PlaylistOperations.createPlaylist("test-user", "test-playlist");
		boolean added = PlaylistOperations.addSong(1, playlistID);
		AuthResult authUser = Authenticate.authUser("test-user", "test-password");
		if(authUser.getResult() == AUTH_RESULT.INCORRECT_PASSWORD || authUser.getResult() == AUTH_RESULT.NO_SUCH_USER)
			assertTrue(false);
		added = added & PlaylistOperations.populatePlaylist(authUser.getUser()).size() == 1;
		added = added & PlaylistOperations.removePlaylist(playlistID, "test-user");
		assertTrue(added);
	}
	
	@Test
	public void testD_RenamePlaylist() throws ClassNotFoundException, SQLException, IOException {
		playlistID = PlaylistOperations.createPlaylist("test-user", "test-playlist");
		System.out.println("PlaylistID: " + playlistID);
		boolean success = PlaylistOperations.renamePlaylist(playlistID, "test-new-playlist") == 1;
		success = success & PlaylistOperations.removePlaylist(playlistID, "test-user");
		assertTrue(success);
	}
	
	@Test
	public void testE_GetAllSongsByID() throws ClassNotFoundException, SQLException, IOException {
		playlistID = PlaylistOperations.createPlaylist("test-user", "test-playlist");
		boolean success = PlaylistOperations.addSong(1, playlistID);
		success = success & PlaylistOperations.getAllSongsByID(playlistID).size() == 1;
		success = success & PlaylistOperations.removePlaylist(playlistID, "test-user");
		assertTrue(success);
	}
	
	@Test
	public void testF_RemoveSong() throws ClassNotFoundException, SQLException, IOException {
		playlistID = PlaylistOperations.createPlaylist("test-user", "test-playlist");
		boolean success = PlaylistOperations.addSong(1, playlistID);
		success = success & PlaylistOperations.removeSong(playlistID, 1);
		success = success & PlaylistOperations.removePlaylist(playlistID, "test-user");
		assertTrue(success);
	}
	
	@AfterClass
	public static void removeUser() throws ClassNotFoundException, SQLException, IOException {
		UserOperations.removeUser("test-user");
	}
}
