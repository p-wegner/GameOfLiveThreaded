package game.globals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StatsTest {

	@Test
	void testName() throws Exception {
		Stats.histo("myval", 1);
		Stats.histo("myval", 5);
		Stats.histo("myval", 1);

		Assertions.assertEquals("myval: 1: x2 | 5: x1", Stats.histos());
	}

}
