package game.main;

import java.util.List;
import java.util.function.Consumer;

public class Board {

	private List<Node> nodes;
	private int width;
	private int height;

	public Board(List<Node> nodes, int width, int height) {
		this.nodes = nodes;
		this.width = width;
		this.height = height;
	}

	//TODO: generateString
	//TODO: move into Node
	public void printByLine(Consumer<Node> callback) {
		Consumer<Node> item = node -> {
			callback.accept(node);
			System.out.print(" ");
		};

		Runnable nextLine = () -> System.out.print("\n");

		iterateByLine(item, nextLine);
	}

	public void iterateByLine(Consumer<Node> callback) {
		iterateByLine(callback, () -> {
		});
	}

	public void iterateByLine(Consumer<Node> callback, Runnable onNextLine) {
		Node topLeft = findTopLeftBasedOnRandomNode();

		Node y = topLeft;
		do {
			Node x = y;
			do {
				callback.accept(x);
				x = x.right();
			} while (!x.isEmpty());

			y = y.down();
			onNextLine.run();
		} while (!y.isEmpty());
	}

	private Node findTopLeftBasedOnRandomNode() {
		Node random = nodes.get((int) Math.random() * (width * height));
		Node topLeft = findTopLeft(random);
		return topLeft;
	}

	private Node findTopLeft(Node node) {
		while (!node.left().isEmpty()) {
			node = node.left();
		}
		while (!node.top().isEmpty()) {
			node = node.top();
		}
		Node topLeft = node;
		return topLeft;
	}

	public void simulateTick() {
		nodes.forEach(it -> it.simulateTick());
	}

	public void nextGeneration() {
		nodes.forEach(it -> it.nextGeneration());
	}
}
