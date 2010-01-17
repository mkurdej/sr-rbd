package ddb.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Test;

public class UtilTest {

	
	@Test
	public void testGetMACAddress() {
		assertEquals("Make sure to write your own MAC in the test",
				"00-15-AF-EB-26-F2", Util.getMACAddress());
	}

	@Test
	public void testGenerateGUID() {
		// generate a lot of GUIDs -- million or so
		// sort the list
		// is there any duplicate ?
		final int COUNT = 100 * 1000;
		String[] guids = new String[COUNT];
		for (int i = 0; i < guids.length; ++i) {
			guids[i] = Util.generateGUID();
		}
		Arrays.sort(guids);
		String g = guids[0];
		int duplicates = 0;
		for (int i = 1; i < guids.length; ++i) {
			if (g.equals(guids[i])) {
				++duplicates;
			}
			g = guids[i];
		}
		if (duplicates != 0) {
			fail("Duplicated GUIDs -- " + duplicates + " out of " + COUNT);
		}
	}

}
