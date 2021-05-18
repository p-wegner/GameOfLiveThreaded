package game.gameoflife;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import game.globals.Stats;
import game.network.CellAware;
import game.network.NavigatableCell;
import game.network.messages.Message;
import game.network.messages.NeighborRoutingInfo;
import game.util.Threads;

public class GameOfLifeCell implements CellAware {

	private boolean alive;

	private Threads threads;
	private Runnable nextOp = null;
	private int tick = 0;

	// TODO should not be navigatable cell, but facade for messaging/message-routing
	private NavigatableCell container;

	private GameOfLifeConfig config;
	private LifeCommunicator lifeCommunicator;

	public GameOfLifeCell(Threads threads, GameOfLifeConfig config) {
		this.threads = threads;
		this.config = config;
		this.lifeCommunicator = new LifeCommunicator(this, config.getGenerationInterval());
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
		// we need to initially send out requests
		// after that we can receive then send in the loop
		lifeCommunicator.requestNeighborAliveStatus();

		while (true) {
			tick();
			// threads.randomWaiter(100, 100);
			updateGeneration();
			threads.randomWaiter(config.getTickBaseWait(), config.getTickRandomWait());
		}
	}

	private void tick() {
		tick++;

		Optional<List<AliveResponse>> responses = lifeCommunicator.communicate(tick);

		if (responses.isPresent()) {
			List<AliveResponse> aliveResponses = responses.get();
			gameLogic(aliveResponses.size());
		}
	}

	private void updateGeneration() {
		Optional.ofNullable(nextOp).ifPresent(it -> it.run());
		nextOp = null;
	}

	private void gameLogic(int neighborsAlive) {
		Stats.inc("alive " + describeComparison(neighborsAlive));
		nextOp = () -> this.alive = nextAliveState(neighborsAlive);
	}

	private boolean nextAliveState(int neighborsAlive) {
		return WillLiveRuleProvider.oneRuleMatches(this.alive, neighborsAlive);
	}

	private String describeComparison(int neighborsAlive) {
		if (WillLiveRule.NEEDED_NEIGHBOURS_TO_SURVIVE.contains(neighborsAlive)) {
			return "== " + neighborsAlive;
		}
		if (neighborsAlive > 3) {
			return " > 3";
		}
		return " < 2";
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
		// as long as each one handles itself we have to handle messages even if !alive
//		if (!alive) {
//			return;
//		}
		lifeCommunicator.handle(message);
	}

	public void sendNeighbors(Consumer<NeighborRoutingInfo> consumer) {
		container.neighborsRoutingStream().forEach(consumer);
	}
}
