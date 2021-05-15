package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

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

	private List<Node> createEmptyNeighbors() {
		return Arrays.asList(Node.empty(), Node.empty(), Node.empty(),
				Node.empty(), Node.empty(), Node.empty(), Node.empty(), Node.empty());
	}

	protected Node() {
		// nothing
	}

	public Node(ExecutorService threads, Node top, Node left) {
		this.threads = threads;
//		this.top = top;
//		this.left = left;
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
		System.out.println(debug + ": My new " + neighbor + " is " + node.debug);
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

	public void setDebug(String debug) {
		this.debug = debug;
	}

	public String getDebug() {
		return debug;
	}

}
