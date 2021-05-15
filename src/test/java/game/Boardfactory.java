package game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class Boardfactory {

	private int width;
	private int height;

	public Boardfactory(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Board buildRandom(ExecutorService threads) {
		int debugCounter = 0;
		List<Node> nodes = new ArrayList<>();

		// ___? ? ? ?
		// ?<-N<-N<-N<-N ...
		
		
		Node top = Node.empty();
		for (int y = 0; y < height; y++) {
			Node leftMost = null;
			
			Node left = Node.empty();
			for (int x = 0; x < width; x++) {
				System.out.println("building " + debugCounter);
				
				Node node = new Node(threads, top, left);
				node.setDebug("" + debugCounter);
				debugCounter++;

				nodes.add(node);
				node.linkWithTopLeftNeighbours();

				left = node;
				top = top.right();
				if (x==0) {
					leftMost = node;
				}
			}
			top = leftMost;
		}

		return new Board(nodes);
	}

}
