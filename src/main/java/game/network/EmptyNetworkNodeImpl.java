package game.network;

import java.util.stream.Stream;

import game.api.Message;
import game.api.NetworkCell;
import game.main.NeighborRoutingInfo;

public class EmptyNetworkNodeImpl implements NetworkCell {

	private static final EmptyNetworkNodeImpl INSTANCE = new EmptyNetworkNodeImpl();

	private EmptyNetworkNodeImpl() {
		//
	}

	public static NetworkCell create() {
		return INSTANCE;
	}

	@Override
	public String toString() {
		return "E";
	}

	@Override
	public NetworkCell top() {
		return this;
	}

	@Override
	public NetworkCell left() {
		return this;
	}

	@Override
	public NetworkCell down() {
		return this;
	}

	@Override
	public NetworkCell right() {
		return this;
	}

	@Override
	public void updateNeighbor(int neighbor, NetworkCell node) {
		//
	}

	@Override
	public void linkWithTopLeftneighbors() {
		//
	}

	@Override
	public void linkWithCornerNeighbors() {
		//
	}

	@Override
	public boolean isIterable() {
		return false;
	}

	@Override
	public <X> X getData() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Stream<NeighborRoutingInfo> neighborsRoutingStream() {
		return Stream.empty();
	}

	@Override
	public void send(Message message) {
		// nothing
	}
}
