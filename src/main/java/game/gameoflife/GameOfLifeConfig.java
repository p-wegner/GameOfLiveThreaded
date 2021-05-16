package game.gameoflife;

public class GameOfLifeConfig {
	
	private int generationInterval;
	private int tickBaseWait;
	private int tickRandomWait;
	private int x;
	private int y;
	private int screenUpdateInterval;

	public int getGenerationInterval() {
		return generationInterval;
	}

	public void setGenerationInterval(int generationInterval) {
		this.generationInterval = generationInterval;
	}

	public int getTickBaseWait() {
		return tickBaseWait;
	}

	public void setTickBaseWait(int tickBaseWait) {
		this.tickBaseWait = tickBaseWait;
	}

	public int getTickRandomWait() {
		return tickRandomWait;
	}

	public void setTickRandomWait(int tickRandomWait) {
		this.tickRandomWait = tickRandomWait;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getScreenUpdateInterval() {
		return screenUpdateInterval;
	}

	public void setScreenUpdateInterval(int screenUpdateInterval) {
		this.screenUpdateInterval = screenUpdateInterval;
	}

}
