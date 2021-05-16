package game.gameoflife;

import java.util.List;
import java.util.Optional;

import game.api.Message;
import game.api.NetworkCell;
import game.globals.Stats;
import game.network.CellAware;
import game.network.NavigatableCell;
import game.util.ThreadingUtil;
import game.util.Threads;

public class GameOfLifeCell implements CellAware {

	private boolean alive;

	private Threads threads;
	private Runnable nextOp = null;
	private int tick = 0;

	// TODO should not be navigatable cell, but facade
	private NavigatableCell container;

	private LifeCommunicator lifeCommunicator = new LifeCommunicator(this);

	public GameOfLifeCell(Threads threads) {
		this.threads = threads;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAliveRandom(double probability) {
		this.alive = Math.random() < probability;
	}

	public void start() {
		threads.submit(this, () -> gameOfLifeLoop());
	}

	private void gameOfLifeLoop() {
		// we need to initially send out requests, so that we can receive valid answers,
		// otherwise initial responses alive neighbors would be 0
		lifeCommunicator.requestNeighborAliveStatus(container);
		while (true) {
			tick();
			ThreadingUtil.sleep(100);
			updateGeneration();
			ThreadingUtil.sleep(100);
		}
	}

	private void tick() {
		tick++;

		Optional<List<AliveResponse>> responses = lifeCommunicator.communicate(tick);

		if (responses.isPresent()) {
			List<AliveResponse> aliveResponses = responses.get();
			gameLogik(aliveResponses.size());
		}
	}

	private void updateGeneration() {
		Optional.ofNullable(nextOp).ifPresent(it -> it.run());
		nextOp = null;
	}

	private void gameLogik(int neighborsAlive) {
		if (alive) {
			if (neighborsAlive < 2) {
				Stats.inc("alive < 2");
				nextOp = () -> this.alive = false;
			}
			if (neighborsAlive == 2 || neighborsAlive == 3) {
				Stats.inc("alive == 2");
				nextOp = () -> this.alive = true;
			}
			if (neighborsAlive > 3) {
				Stats.inc("alive > 3");
				nextOp = () -> this.alive = false;
			}
		} else {
			if (neighborsAlive == 3) {
				Stats.inc("alive == 3");
				nextOp = () -> this.alive = true;
			}
		}
	}

	// TODO remove
	@Override
	public NavigatableCell getContainer() {
		return container;
	}

	@Override
	public void setContainer(NavigatableCell container) {
		this.container = container;
	}

	@Override
	public void send(Message message) {
		if (!alive) {
			return;
		}
		lifeCommunicator.handle(message);
	}

	public void validateRequest(NetworkCell sourceNode, NetworkCell targetNode) {
		if (targetNode != this) {
			throw new RuntimeException("TARGETNODE WRONG");
		}
		if (!container.containsNeighbor(sourceNode)) {
			throw new RuntimeException("This is not my neighbor!!");
		}
	}
}
