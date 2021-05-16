package game.main.gen2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NetworkFactory {

	private int width;
	private int height;
	private List<NetworkNode> nodes = new ArrayList<>();

	private DebugInfoFactory debugInfoFactory = new DebugInfoFactory();

	public NetworkFactory(int width, int height) {
		this.width = width;
		this.height = height;
	}

	private NetworkNode createEmpty() {
		return EmptyNetworkNodeImpl.create();
	}

	private List<NetworkNode> createEmptyNeighbors() {
		return Arrays.asList(
				createEmpty(), createEmpty(),
				createEmpty(), createEmpty(),
				createEmpty(), createEmpty(),
				createEmpty(), createEmpty());
	}

	public Network create() {
		// ___? ? ? ?
		// ?<-N<-N<-N<-N ...
		// ___^__^__^__^ ...
		// ?<-N<-N<-N<-N ...

		NetworkNode top = createEmpty();
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
		Iterator<NetworkNode> topRowIterator = columnInfo.topRowIterator();

		NetworkNode leftMost = null;
		NetworkNode leftNode = createEmpty();
		for (int x = 0; x < width; x++) {
			NetworkNode node = createNode(topRowIterator.next(), leftNode);
			node.linkWithTopLeftneighbors();
			nodes.add(node);
			leftNode = node;
			if (x == 0) {
				leftMost = node;
			}
		}
		return new RowInfo(leftMost);
	}

	private NetworkNode createNode(NetworkNode top, NetworkNode left) {
		List<NetworkNode> neighbors = createEmptyNeighbors();

		// TODO pretty internal info....
		neighbors.set(1, top);
		neighbors.set(3, left);

		NetworkNodeImpl node = new NetworkNodeImpl(neighbors);
		node.setDebug(debugInfoFactory.nextId());
		return node;
	}

}
