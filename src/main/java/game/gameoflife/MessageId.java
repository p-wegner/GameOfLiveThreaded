package game.gameoflife;

public class MessageId {

	private int id = 0;
	private int tick = 0;
	private int tickInterval;

	public MessageId(int tickInterval) {
		this.tickInterval = tickInterval;
		// fire the first time
		//this.tick = -tickInterval-1;
	}

	public void nextId(int tick) {
		id++;
		this.tick = tick;
	}

	public boolean isElapsed(int ticksToCompare) {
		return ticksToCompare > tick + tickInterval;
	}

	public int getId() {
		return id;
	}
}