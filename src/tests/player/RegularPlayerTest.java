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
import utils.Constants;
import utils.dbmgr.UserOperations;

public class RegularPlayerTest {

	@BeforeClass
	public static void addUser() throws ClassNotFoundException, SQLException, IOException {
		UserOperations.createUser("test-user", "test-password", "test-firstname", "test-lastname", "1993-01-01");
	}
	
	@Test
	public void test() throws ClassNotFoundException, SQLException, IOException, PlaylistNotCreatedException {
		User user = Authenticate.authUser("test-user", "test-password").getUser();
		boolean success = false;
		for(int i = 0; i < Constants.MAX_PLAYLISTS; ++i)
			user.getPlayer().createPlaylist("test" + i);
		try {
			user.getPlayer().createPlaylist("error-playlist");
		} catch (PlaylistNotCreatedException ex) {
			success = true;
		}
		for(int i = 0; i < Constants.MAX_PLAYLISTS; ++i) {
			user.getPlayer().removePlaylist("test" + i);
		}
		assertTrue(success);
	}
	
	@AfterClass
	public static void removeUser() throws ClassNotFoundException, SQLException, IOException {
		UserOperations.removeUser("test-user");
	}
}
