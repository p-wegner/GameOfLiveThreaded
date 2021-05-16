package game.main.gen2;

import java.util.List;

/**
 * @author Lord
 *
 */
public class NetworkNodeImpl implements NetworkNode {
	
	// 012
	// 3 4
	// 567
	protected List<NetworkNode> neighbors;
	private String debug;

	public NetworkNodeImpl(List<NetworkNode> neighbors) {
		this.neighbors = neighbors;
	}

	public void linkWithTopLeftneighbors() {
		top().updateNeighbor(6, this);
		left().updateNeighbor(4, this);
	}

	public void linkWithCornerNeighbors() {
		top().left().updateNeighbor(7, this);
		down().right().updateNeighbor(0, this);

		top().right().updateNeighbor(5, this);
		down().left().updateNeighbor(2, this);
	}

	public void updateNeighbor(int neighbor, NetworkNode node) {
		neighbors.set(neighbor, node);
	}

	private NetworkNode getNeighbor(int neighbor) {
		return neighbors.get(neighbor);
	}

	public NetworkNode top() {
		return getNeighbor(1);
	}

	public NetworkNode left() {
		return getNeighbor(3);
	}

	public NetworkNode down() {
		return getNeighbor(6);
	}

	public NetworkNode right() {
		return getNeighbor(4);
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	//TODO FIX
//	private Stream<NeighborRoutingInfo> neighborsRoutingStream() {
//		return IntStream.rangeClosed(0, 7).boxed()
//				.map(port -> new NeighborRoutingInfo(port, neighbors.get(port)));
//	}
	
	@Override
	public String toString() {
		return debug;
	}

	@Override
	public boolean isIteratable() {
		return true;
	}
}
