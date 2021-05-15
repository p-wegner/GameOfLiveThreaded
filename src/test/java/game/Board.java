package game;

import java.util.List;

public class Board {

	private List<Node> nodes;

	public Board(List<Node> nodes) {
		this.nodes = nodes;
	}

	public void dumpByLine() {
		Node random = nodes.get(55);
		Node topLeft = findTopLeft(random);
		
		
		Node y = topLeft;
		do {
			Node x = y;
			do {
				System.out.print(x.getDebug() + " ");
				x = x.right();
			} while(!x.isEmpty());
			
			y = y.down();
			System.out.print("\n");
		} while(!y.isEmpty());
		
	}

	private Node findTopLeft(Node node) {
		while(!node.left().isEmpty()) {
			node = node.left();
		}
		while(!node.top().isEmpty()) {
			node = node.top();
		}
		Node topLeft = node;
		return topLeft;
	}
}
