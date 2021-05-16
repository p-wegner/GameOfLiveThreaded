package game.gameoflife;

import org.junit.jupiter.api.Test;

public class GameOfLifeTest {

	@Test
	void gameOfLife() throws Exception {

		GameOfLifeConfig config = new GameOfLifeConfig();
		config.setX(30);
		config.setY(15);
		config.setScreenUpdateInterval(200); // 50 - 500

		// every n-th tick a new generation is created
		config.setGenerationInterval(10); // check how many responses are dropped because of being too late
		// tick duration
		config.setTickBaseWait(50);
		config.setTickRandomWait(50);

		new GameOfLife(config).runForever();
	}

}
