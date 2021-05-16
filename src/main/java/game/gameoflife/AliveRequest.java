package game.gameoflife;

import game.main.NeighborRoutingInfo;

public class AliveRequest {

	// TODO move routing into message
	private NeighborRoutingInfo routing;
	private int messageId;

	public AliveRequest(NeighborRoutingInfo routing, int messageId) {
		this.routing = routing;
		this.messageId = messageId;
	}

	public NeighborRoutingInfo getRouting() {
		return routing;
	}

	public int getMessageId() {
		return messageId;
	}

}
