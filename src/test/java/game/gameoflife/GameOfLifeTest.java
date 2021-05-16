package game.gameoflife;

import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import game.api.Dumper;
import game.globals.Stats;
import game.network.Network;
import game.network.factory.CellContainerDatafactory;
import game.network.factory.NetworkCellFactory;
import game.network.factory.NetworkCellFactoryImpl;
import game.network.factory.NetworkFactory;
import game.util.ThreadingUtil;
import game.util.Threads;

public class GameOfLifeTest {

	private static final int Y = 15;
	private static final int X = 15;

	@Test
	void gameOfLife() throws Exception {
		Threads threads = new Threads(Executors.newFixedThreadPool(X * Y));
		CellContainerDatafactory dataFactory = () -> new GameOfLifeCell(threads);

		NetworkCellFactory cellFactory = new NetworkCellFactoryImpl(dataFactory);
		NetworkFactory networkFactory = new NetworkFactory(X, Y, cellFactory);

		Network network = networkFactory.create();

		System.out.println(network.dumpDebug());
		
		network.iterateDataByLine(randomAliveSetter());
		System.out.println(network.dumpData(fieldDumper()));

		network.iterateDataByLine(gameStarter());

		dumpBoardForever(threads, network);
	}

	private void dumpBoardForever(Threads threads, Network network) {
		while (true) {
			System.out.println(network.dumpData(fieldDumper()));
			System.out.println("Busy threads: " + threads.getBusyCount());
			System.out.println(Stats.stats());
			System.out.println("-------------------------------------------------");
			ThreadingUtil.sleep(200);
		}
	}

	private Consumer<GameOfLifeCell> gameStarter() {
		return (GameOfLifeCell gameOfLife) -> gameOfLife.start();
	}

	private Consumer<GameOfLifeCell> randomAliveSetter() {
		return (GameOfLifeCell gameOfLife) -> gameOfLife.setAliveRandom(0.3);
	}

	private Dumper<GameOfLifeCell> fieldDumper() {
		return gameOfLifeCell -> gameOfLifeCell.isAlive() ? "." : " ";
	}
}
