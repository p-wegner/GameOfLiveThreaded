package game.network.factory;

import game.api.NetworkCell;

public interface NetworkCellFactory {

	NetworkCell create(NetworkCell top, NetworkCell left);

	NetworkCell createEmpty();
	
	static NetworkCellFactory createDefault() {
		return new NetworkCellFactoryImpl(() -> null);
	}
}
