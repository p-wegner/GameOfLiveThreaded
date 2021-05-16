package game.network;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import game.api.NetworkCell;
import game.network.messages.Message;
import game.network.messages.NeighborRoutingInfo;

public class CellImpl implements NetworkCell {

	// 012
	// 3 4
	// 567
	protected List<NetworkCell> neighbors;
	private String debug;
	private CellAware data;

	public CellImpl(List<NetworkCell> neighbors, CellAware data) {
		this.neighbors = neighbors;
		this.data = data;
		data.setContainer(this);
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

	public void updateNeighbor(int neighbor, NetworkCell node) {
		neighbors.set(neighbor, node);
	}

	private NetworkCell getNeighbor(int neighbor) {
		return neighbors.get(neighbor);
	}

	public NetworkCell top() {
		return getNeighbor(1);
	}

	public NetworkCell left() {
		return getNeighbor(3);
	}

	public NetworkCell down() {
		return getNeighbor(6);
	}

	public NetworkCell right() {
		return getNeighbor(4);
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	@Override
	public String toString() {
		return debug;
	}

	@Override
	public boolean isIterable() {
		return true;
	}

	@SuppressWarnings("unchecked")
	public <X> X getData() {
		return (X) data;
	}

	@Override
	public Stream<NeighborRoutingInfo> neighborsRoutingStream() {
		return IntStream.rangeClosed(0, 7).boxed()
				.map(port -> new NeighborRoutingInfo(this, neighbors.get(port), port));
	}

	@Override
	public void send(Message message) {
		data.send(message);
	}

	@Override
	public boolean containsNeighbor(NetworkCell node) {
		return neighbors.contains(node);
	}
}
