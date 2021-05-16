package game.main;

public class EmptyNode extends Node {
	
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

	public void start() {
		// nothing
	}

	public void simulateTick() {
		// nothing
	}
}
