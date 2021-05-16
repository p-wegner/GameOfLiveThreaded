package game.gameoflife;

import java.util.concurrent.Executors;
import java.util.function.Consumer;

import game.api.Dumper;
import game.globals.Stats;
import game.network.Network;
import game.network.factory.CellContainerDatafactory;
import game.network.factory.NetworkCellFactory;
import game.network.factory.NetworkCellFactoryImpl;
import game.network.factory.NetworkFactory;
import game.util.ThreadingUtil;
import game.util.Threads;

public class GameOfLife {

	private GameOfLifeConfig config;

	public GameOfLife(GameOfLifeConfig config) {
		this.config = config;
	}
	
	public void runForever() {
		Network network = createNetwork();

		System.out.println(network.dumpDebug());
		
		network.iterateDataByLine(randomAliveSetter());
		System.out.println(network.dumpData(fieldDumper()));

		network.iterateDataByLine(gameStarter());

		dumpBoardForever(network);
	}

	private Network createNetwork() {
		Threads threads = new Threads(Executors.newFixedThreadPool(config.getX() * config.getY()));
		Stats.provider("Busy threads", () -> (long)threads.getBusyCount());
		CellContainerDatafactory dataFactory = () -> new GameOfLifeCell(threads, config);

		NetworkCellFactory cellFactory = new NetworkCellFactoryImpl(dataFactory);
		NetworkFactory networkFactory = new NetworkFactory(config.getX(), config.getY(), cellFactory);

		Network network = networkFactory.create();
		return network;
	}

	private void dumpBoardForever(Network network) {
		while (true) {
			System.out.println(network.dumpData(fieldDumper()));
			System.out.println(Stats.stats());
			System.out.println(Stats.histos());
			System.out.println("-------------------------------------------------");
			ThreadingUtil.sleep(config.getScreenUpdateInterval());
		}
	}

	private Consumer<GameOfLifeCell> gameStarter() {
		return (GameOfLifeCell gameOfLife) -> gameOfLife.start();
	}

	private Consumer<GameOfLifeCell> randomAliveSetter() {
		return (GameOfLifeCell gameOfLife) -> gameOfLife.setAliveRandom(0.7);
	}

	private Dumper<GameOfLifeCell> fieldDumper() {
		return gameOfLifeCell -> gameOfLifeCell.isAlive() ? "." : " ";
	}
	
}
