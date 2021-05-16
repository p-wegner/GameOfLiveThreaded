package game.gameoflife;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import game.gameoflife.MessageId;

public class MessageIdTest {

	@Test
	void testName() throws Exception {
		MessageId messageId = new MessageId(20);
		assertTrue(messageId.isElapsed(0));
		assertTrue(messageId.isElapsed(100));
	}
}
