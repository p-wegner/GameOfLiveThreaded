package game.network.factory;

import game.api.NetworkCell;
import game.network.CellAware;
import game.network.NavigatableCell;
import game.network.messages.Message;

public interface NetworkCellFactory {

	NetworkCell create(NetworkCell top, NetworkCell left);

	NetworkCell createEmpty();
	
	static NetworkCellFactory createDefault() {
		return new NetworkCellFactoryImpl(() -> new CellAware() {
			
			@Override
			public void send(Message message) {
				//
			}
			
			@Override
			public void setContainer(NavigatableCell container) {
				//
			}
			
			@Override
			public NavigatableCell getContainer() {
				return null;
			}
		});
	}
}
