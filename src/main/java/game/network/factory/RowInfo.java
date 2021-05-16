package game.network.factory;

import game.api.NetworkCell;

public class RowInfo {

	private NetworkCell leftMost;

	public RowInfo(NetworkCell leftMost) {
		this.leftMost = leftMost;
		// TODO Auto-generated constructor stub
	}

	public NetworkCell getLeftMost() {
		return leftMost;
	}

}
