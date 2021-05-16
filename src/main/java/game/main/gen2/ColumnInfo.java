package game.main.gen2;

import java.util.Iterator;

public class ColumnInfo {

	private NetworkNode top;

	public ColumnInfo(NetworkNode top) {
		this.top = top;
	}

	public Iterator<NetworkNode> topRowIterator() {
		return new Iterator<NetworkNode>() {

			private NetworkNode current = top;
			
			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public NetworkNode next() {
				NetworkNode value = current;
				current = current.right();
				return value;
			}
		};
	}

}
