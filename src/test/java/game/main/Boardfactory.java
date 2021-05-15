package game.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import game.globals.Debug;
import game.util.Threads;

public class Boardfactory {

	private int width;
	private int height;

	public Boardfactory(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Board create(Threads threads) {
		int debugCounter = 0;
		List<Node> nodes = new ArrayList<>();

		// ___? ? ? ?
		// ?<-N<-N<-N<-N ...
		
		Node top = Node.empty();
		for (int y = 0; y < height; y++) {
			Node leftMost = null;
			
			Node left = Node.empty();
			for (int x = 0; x < width; x++) {
				Debug.println("building " + debugCounter);
				
				boolean nodeIsOn = Math.random()>0.5;
				Node node = new Node(threads, top, left, nodeIsOn);
				node.setDebug("" + debugCounter);
				debugCounter++;

				nodes.add(node);
				node.linkWithTopLeftneighbors();

				left = node;
				top = top.right();
				if (x==0) {
					leftMost = node;
				}
			}
			top = leftMost;
		}
		
		nodes.forEach(node -> node.linkWithCornerneighbors());
		Collections.shuffle(nodes);
		
		return new Board(nodes, width, height);
	}

}
