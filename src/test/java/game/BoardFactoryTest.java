package game;

//import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

import game.globals.Stats;
import game.main.Board;
import game.main.Boardfactory;
import game.util.ThreadingUtil;
import game.util.Threads;

public class BoardFactoryTest {

	private static final int Y = 15;
	private static final int X = 15;

	@Test
	void testName() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(X * Y);
		Threads threads = new Threads(executor);
		Boardfactory boardfactory = new Boardfactory(X, Y);
		Board board = boardfactory.create(threads);

		printLayout(board);
		setAliveRandom(board);
		startThreads(board);

		dumpBoardForever(threads, board);
	}

	private void dumpBoardForever(Threads threads, Board board) throws InterruptedException {
		while (true) {
			board.printByLine(node -> node.printLive());
			System.out.println("Busy threads: " + threads.getBusyCount());
			System.out.println(Stats.stats());
			System.out.println("-------------------------------------------------");
			ThreadingUtil.sleep(200);
		}
	}

	private void startThreads(Board board) {
		board.iterateByLine(node -> node.start());
	}

	private void setAliveRandom(Board board) {
		board.iterateByLine(node -> node.setAlive(Math.random() < 0.3));
	}

	private void printLayout(Board board) {
		board.printByLine(node -> node.debugPrint());
	}
}
