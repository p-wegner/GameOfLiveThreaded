package game.api;

import game.network.messages.Message;

public interface CellCommunicator {

	void send(Message message);

}
