package operations;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;

import utils.dbmgr.SongOperations;

public class SongOperationsTest {

	@Test
	public void testGetAllSongs() throws ClassNotFoundException, SQLException, IOException {
		boolean songs = SongOperations.getAllSongs().size() == 23;
		assertTrue(songs);
	}
}
