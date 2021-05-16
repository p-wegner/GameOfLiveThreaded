package game.network.factory;

import java.util.Iterator;

import game.api.NetworkCell;

public class ColumnInfo {

	private NetworkCell top;

	public ColumnInfo(NetworkCell top) {
		this.top = top;
	}

	public Iterator<NetworkCell> topRowIterator() {
		return new Iterator<NetworkCell>() {

			private NetworkCell current = top;
			
			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public NetworkCell next() {
				NetworkCell value = current;
				current = current.right();
				return value;
			}
		};
	}

}
