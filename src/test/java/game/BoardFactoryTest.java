package game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

public class BoardFactoryTest {

	@Test
	void testName() throws Exception {
		
		ExecutorService threads = Executors.newFixedThreadPool(10);
		Boardfactory boardfactory = new Boardfactory(10,10);
		Board b = boardfactory.buildRandom(threads);

		b.dumpByLine();
		//b.start();
	}
	
}
