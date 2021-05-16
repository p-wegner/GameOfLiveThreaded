package game.main.gen2;

public class EmptyNetworkNodeImpl implements NetworkNode {

	private static final EmptyNetworkNodeImpl INSTANCE = new EmptyNetworkNodeImpl();

	private EmptyNetworkNodeImpl() {
		//
	}

	public static NetworkNode create() {
		return INSTANCE;
	}

	@Override
	public String toString() {
		return "E";
	}

	@Override
	public NetworkNode top() {
		return this;
	}

	@Override
	public NetworkNode left() {
		return this;
	}

	@Override
	public NetworkNode down() {
		return this;
	}

	@Override
	public NetworkNode right() {
		return this;
	}

	@Override
	public void updateNeighbor(int neighbor, NetworkNode node) {
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
	public boolean isIteratable() {
		return false;
	}
}
