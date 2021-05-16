package game.network.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import game.api.NetworkCell;
import game.network.Network;

public class NetworkFactory {

	private int width;
	private int height;
	private List<NetworkCell> nodes = new ArrayList<>();

	private NetworkCellFactory cellFactory;

	public NetworkFactory(int width, int height, NetworkCellFactory cellFactory) {
		this.width = width;
		this.height = height;
		this.cellFactory = cellFactory;
	}

	public Network create() {
		// ___? ? ? ?
		// ?<-N<-N<-N<-N ...
		// ___^__^__^__^ ...
		// ?<-N<-N<-N<-N ...

		NetworkCell top = cellFactory.createEmpty();
		for (int y = 0; y < height; y++) {
			ColumnInfo columnInfo = new ColumnInfo(top);
			RowInfo rowInfo = createRow(columnInfo);

			top = rowInfo.getLeftMost();
		}

		nodes.forEach(node -> node.linkWithCornerNeighbors());
		// there is no order
		Collections.shuffle(nodes);

		return new Network(nodes, width, height);
	}

	private RowInfo createRow(ColumnInfo columnInfo) {
		Iterator<NetworkCell> topRowIterator = columnInfo.topRowIterator();

		NetworkCell leftMost = null;
		NetworkCell leftNode = cellFactory.createEmpty();
		for (int x = 0; x < width; x++) {
			NetworkCell node = cellFactory.create(topRowIterator.next(), leftNode);
			node.linkWithTopLeftneighbors();
			nodes.add(node);
			leftNode = node;
			if (x == 0) {
				leftMost = node;
			}
		}
		return new RowInfo(leftMost);
	}
}
