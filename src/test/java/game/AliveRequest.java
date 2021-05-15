package game;

import game.main.Node;

public class AliveRequest {

	private Node source;
	private NeighborRoutingInfo routing;
	private int messageId;

	public AliveRequest(Node source, NeighborRoutingInfo routing, int messageId) {
		this.source = source;
		this.routing = routing;
		this.messageId = messageId;
	}

	public Node getSource() {
		return source;
	}

	public NeighborRoutingInfo getRouting() {
		return routing;
	}

	public int getMessageId() {
		return messageId;
	}

}
