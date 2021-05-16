package game.network.factory;

import game.api.NetworkCell;

public class RowInfo {

	private NetworkCell leftMost;

	public RowInfo(NetworkCell leftMost) {
		this.leftMost = leftMost;
	}

	public NetworkCell getLeftMost() {
		return leftMost;
	}

}
