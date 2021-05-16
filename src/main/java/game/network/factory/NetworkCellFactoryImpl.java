package game.network.factory;

import java.util.Arrays;
import java.util.List;

import game.api.NetworkCell;
import game.network.CellAware;
import game.network.CellImpl;
import game.network.EmptyNetworkNodeImpl;

public class NetworkCellFactoryImpl implements NetworkCellFactory {

	private DebugInfoFactory debugInfoFactory = new DebugInfoFactory();
	private CellContainerDatafactory dataFactory;

	public NetworkCellFactoryImpl(CellContainerDatafactory dataFactory) {
		this.dataFactory = dataFactory;
	}

	@Override
	public NetworkCell createEmpty() {
		return EmptyNetworkNodeImpl.create();
	}

	private List<NetworkCell> createEmptyNeighbors() {
		return Arrays.asList(
				createEmpty(), createEmpty(),
				createEmpty(), createEmpty(),
				createEmpty(), createEmpty(),
				createEmpty(), createEmpty());
	}

	@Override
	public NetworkCell create(NetworkCell top, NetworkCell left) {
		List<NetworkCell> neighbors = createEmptyNeighbors();

		// TODO thats pretty internal info.... maybe need neighborsfactory :D
		neighbors.set(1, top);
		neighbors.set(3, left);

		CellAware data = dataFactory.create();
		CellImpl node = new CellImpl(neighbors, data);
		node.setDebug(debugInfoFactory.nextId());
		return node;
	}
}
