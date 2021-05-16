package game.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import game.AliveRequest;
import game.AliveResponse;
import game.MessageId;
import game.NeighborRoutingInfo;
import game.globals.Debug;
import game.globals.Stats;
import game.util.ThreadingUtil;
import game.util.Threads;

public class Node {

	private String debug;

	// 012
	// 3 4
	// 567
	protected List<Node> neighbors;

	private Threads threads;
	private List<AliveRequest> requests = Collections.synchronizedList(new ArrayList<>());
	private List<AliveResponse> responses = Collections.synchronizedList(new ArrayList<>());

	private boolean isAlive;
	private AtomicBoolean threadRunning = new AtomicBoolean(false);

	private List<Runnable> nextOps = new ArrayList<>();
	private int wait1 = genWaitTime();
	private int wait2 = genWaitTime();
	private int tick = 0;
	private MessageId messageId = new MessageId(5);

	protected Node() {
		// nothing
	}

	public Node(Threads threads, Node top, Node left, boolean isAlive) {
		this.threads = threads;
		this.isAlive = isAlive;
		neighbors = new ArrayList<>(createEmptyneighbors());
		neighbors.set(1, top);
		neighbors.set(3, left);
	}

	public static Node empty() {
		return new EmptyNode();
	}

	// TODO private
	public boolean isEmpty() {
		return false;
	}

	private void registerDown(Node node) {
		updateNeighbor(6, node);
	}

	private void registerRight(Node node) {
		updateNeighbor(4, node);
	}

	protected void updateNeighbor(int neighbor, Node node) {
		Debug.println(debug + ": My new " + neighbor + " is " + node.debug);
		neighbors.set(neighbor, node);
	}

	protected Node tryGetNeighbor(int neighbor) {
		return neighbors.get(neighbor);
	}

	// TODO private
	public Node top() {
		return tryGetNeighbor(1);
	}

	// TODO private
	public Node left() {
		return tryGetNeighbor(3);
	}

	// TODO private
	public Node down() {
		return tryGetNeighbor(6);
	}

	// TODO private
	public Node right() {
		return tryGetNeighbor(4);
	}

	public void linkWithTopLeftneighbors() {
		top().registerDown(this);
		left().registerRight(this);
	}

	public void linkWithCornerneighbors() {
		top().left().updateNeighbor(7, this);
		down().right().updateNeighbor(0, this);

		top().right().updateNeighbor(5, this);
		down().left().updateNeighbor(2, this);
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	private List<Node> createEmptyneighbors() {
		return Arrays.asList(Node.empty(), Node.empty(), Node.empty(),
				Node.empty(), Node.empty(), Node.empty(), Node.empty(), Node.empty());
	}

	//TODO: use tostring instead maybe?
	public void debugPrint() {
		System.out.print(debug);
	}

	// TODO: Board feature
	public void printLive() {
		System.out.print(isAlive ? "." : " ");
	}

	private void lifeAction() {
//		while (isAlive) {
		// we need to initially send out requests, so that we can receive valid answers,
		// otherwise initial responses alive neighbors would be 0
		requestNeighborAliveStatus();

		while (true) {
			simulateTick();
			ThreadingUtil.sleep(wait1);
			nextGeneration();
			ThreadingUtil.sleep(wait2);
		}
	}

	public void simulateTick() {
		tick++;

		answerAliveRequests();

		if (messageId.isElapsed(tick)) {
			List<AliveResponse> alive = evaluateAliveResponses();

			messageId.nextId(tick);
			requestNeighborAliveStatus();

			gameLogik(alive.size());
		}
	}

	private List<AliveResponse> evaluateAliveResponses() {
		List<AliveResponse> responses2 = responses;
		responses = new ArrayList<>();

		List<AliveResponse> alive = responses2.stream()
				.filter(response -> response.getMessageId() == messageId.getId())
				.collect(Collectors.toList());
		return alive;
	}

	private void answerAliveRequests() {
		List<AliveRequest> requests2 = requests;
		requests = new ArrayList<>();
		if (isAlive) {
			requests2.forEach(request -> {
				if (request.getRouting().getTargetNode() != this) {
					throw new RuntimeException("TARGETNODE WRONG");
				}
				if (!neighbors.contains(request.getSource())) {
					throw new RuntimeException("This is not my neighbor!!");
				}
				Node source = request.getSource();
				source.send(new AliveResponse(request));
			});
		}
	}

	private void requestNeighborAliveStatus() {
		neighborsRoutingStream()
				.forEach(routing -> {
					AliveRequest request = new AliveRequest(this, routing, messageId.getId());
					routing.getTargetNode().send(request);
				});
	}

	private void gameLogik(int neighborsAlive) {

//		//////////// TODO
//		return;
		if (isAlive) {
			if (neighborsAlive < 2) {
				Stats.inc("alive < 2");
				nextOps.add(() -> this.isAlive = false);
			}
			if (neighborsAlive == 2 || neighborsAlive == 3) {
				Stats.inc("alive == 2");
				nextOps.add(() -> this.isAlive = true);
			}
			if (neighborsAlive > 3) {
				Stats.inc("alive > 3");
				nextOps.add(() -> this.isAlive = false);
			}
		} else {
			if (neighborsAlive == 3) {
				Stats.inc("alive == 3");
				nextOps.add(() -> this.isAlive = true);
			}
		}
	}

	private void send(AliveRequest aliveRequest) {
//		if (isAlive) {
		requests.add(aliveRequest);
//		}
	}

	private void send(AliveResponse aliveResponse) {
//		if (isAlive) {
		responses.add(aliveResponse);
//		}
	}

	public void nextGeneration() {
		nextOps.forEach(it -> it.run());
		nextOps.clear();
	}

	private Stream<NeighborRoutingInfo> neighborsRoutingStream() {
		return IntStream.rangeClosed(0, 7).boxed()
				.map(port -> new NeighborRoutingInfo(port, neighbors.get(port)));
	}

	public void setAlive(boolean isAlive) {
//		boolean wasSetAlive = !this.isAlive && isAlive;
		this.isAlive = isAlive;
//		if (wasSetAlive) {
		start();
//		}
	}

	public void start() {
		if (/* isAlive && */ threadRunning.compareAndExchange(false, true)) {
			threads.submit(() -> {
				lifeAction();
				threadRunning.set(false);
			});
		}
	}

	private int genWaitTime() {
		return (int) (Math.random() * 100) + 50;
	}
}
