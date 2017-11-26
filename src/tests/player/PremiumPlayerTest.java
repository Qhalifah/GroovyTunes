package player;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import auth.Authenticate;
import exceptions.PlaylistNotCreatedException;
import user.User;
import utils.dbmgr.SongOperations;
import utils.dbmgr.UserOperations;

public class PremiumPlayerTest {

	@BeforeClass
	public static void addUser() throws ClassNotFoundException, SQLException, IOException {
		UserOperations.createUser("test-user", "test-password", "test-firstname", "test-lastname", "1993-01-01");
		UserOperations.activatePremium("test-user");
	}
	
	@Test
	public void testA_GetAllSongs() throws ClassNotFoundException, SQLException, IOException {
		User user = Authenticate.authUser("test-user", "test-password").getUser();
		assertTrue(user.getPlayer().getAllSongs().size() == SongOperations.getAllSongs().size());
	}
	
	@Test
	public void testB_CreatePlaylist() throws ClassNotFoundException, SQLException, IOException, PlaylistNotCreatedException {
		User user = Authenticate.authUser("test-user", "test-password").getUser();
		try {
			user.getPlayer().createPlaylist("test-playlist");
		} catch (PlaylistNotCreatedException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		if(user.getPlayer().renamePlaylist("test-playlist", "new-test-playlist") == false) {
			assertTrue(false);
		}
		if(user.getPlayer().removePlaylist("new-test-playlist") == false) {
			assertTrue(false);
		}
		assertTrue(true);
		
	}
	
	@AfterClass
	public static void removeUser() throws ClassNotFoundException, SQLException, IOException {
		UserOperations.removeUser("test-user");
	}
}
