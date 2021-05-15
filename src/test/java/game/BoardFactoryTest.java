package game;

//import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

public class BoardFactoryTest {

	@Test
	void testName() throws Exception {

		ExecutorService threads = Executors.newFixedThreadPool(15*15);
		Boardfactory boardfactory = new Boardfactory(15, 15);
		Board b = boardfactory.buildRandom(threads);

		b.printByLine(node -> node.debugPrint());
		b.iterateByLine(node -> node.setAlive(Math.random() < 0.3));
		b.printByLine(node -> node.printLive());

		// hack: use iterator, otherwise 'walker' with stack required
		b.iterateByLine(node -> node.linkWithCornerNeighbors());

		b.iterateByLine(node -> node.start());
		
		while (true) {
//			b.simulateTick();
//			b.nextGeneration();
			b.printByLine(node -> node.printLive());
			System.out.println("-------------------------------------------------");
			Thread.sleep(200);
		}
	}
}
