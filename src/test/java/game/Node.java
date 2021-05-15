package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Node {

	static class EmptyNode extends Node {
		public EmptyNode() {
			// important: use do nothing constructor, or else emptynode depends on
			// emptynodes (infinite loop)
			super();
			setDebug("?");
		}

		public boolean isEmpty() {
			return true;
		}

		protected void updateNeighbor(int neighbor, Node node) {
			// nothing
		}

		protected Node tryGetNeighbor(int neighbor) {
			return this;
		}
	}

	private ExecutorService threads;
//	private Node top;
//	private Node left;
	private String debug;

	// 012
	// 3 4
	// 567
	protected List<Node> neighbours;
	private boolean isAlive;

	private List<Runnable> nextOps = new ArrayList<>();
	private int wait1 = getWaitTime();
	private int wait2 = getWaitTime();

	private Node() {
		// nothing
	}

	public Node(ExecutorService threads, Node top, Node left, boolean isAlive) {
		this.threads = threads;
		this.isAlive = isAlive;
		neighbours = new ArrayList<>(createEmptyNeighbors());
		neighbours.set(1, top);
		neighbours.set(3, left);
	}

	public boolean isEmpty() {
		return false;
	}

	public static Node empty() {
		return new EmptyNode();
	}

	private void registerDown(Node node) {
		updateNeighbor(6, node);
	}

	private void registerRight(Node node) {
		updateNeighbor(4, node);
	}

	protected void updateNeighbor(int neighbor, Node node) {
		Debug.println(debug + ": My new " + neighbor + " is " + node.debug);
		neighbours.set(neighbor, node);
	}

	protected Node tryGetNeighbor(int neighbor) {
		return neighbours.get(neighbor);
	}

	public Node top() {
		return tryGetNeighbor(1);
	}

	public Node left() {
		return tryGetNeighbor(3);
	}

	public Node down() {
		return tryGetNeighbor(6);
	}

	public Node right() {
		return tryGetNeighbor(4);
	}

	public void linkWithTopLeftNeighbours() {
		top().registerDown(this);
		left().registerRight(this);
		// top.linkWithTopLeftNeighbours();
		// left.linkWithTopLeftNeighbours();
	}

	public void linkWithCornerNeighbors() {
		top().left().updateNeighbor(7, this);
		down().right().updateNeighbor(0, this);

		top().right().updateNeighbor(5, this);
		down().left().updateNeighbor(2, this);
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	private List<Node> createEmptyNeighbors() {
		return Arrays.asList(Node.empty(), Node.empty(), Node.empty(),
				Node.empty(), Node.empty(), Node.empty(), Node.empty(), Node.empty());
	}

	public void debugPrint() {
		System.out.print(debug);
	}

	public void printLive() {
		System.out.print(isAlive ? "." : " ");
	}

	public void simulateTick() {
		// todo: we take neighbors alive information!
		long neighborsAlive = neighborsStream().filter(it -> it.isAlive).count();

		if (isAlive) {
			if (neighborsAlive < 2) {
				nextOps.add(() -> this.isAlive = false);
			}
			if (neighborsAlive == 2 || neighborsAlive == 3) {
				nextOps.add(() -> this.isAlive = true);
			}
			if (neighborsAlive > 3) {
				nextOps.add(() -> this.isAlive = false);
			}
		} else {
			if (neighborsAlive == 3) {
				nextOps.add(() -> this.isAlive = true);
			}
		}
	}

	public void nextGeneration() {
		nextOps.forEach(it -> it.run());
		nextOps.clear();
	}

	private Stream<Node> neighborsStream() {
		return IntStream.rangeClosed(0, 7).boxed().map(num -> tryGetNeighbor(num));
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public void start() {
		threads.submit(() -> {
			while (true) {
				simulateTick();
				ThreadingUtil.sleep(wait1);
				nextGeneration();
				ThreadingUtil.sleep(wait2);
			}
		});
	}

	private int getWaitTime() {
		return (int) (Math.random() * 100) + 50;
	}
}
