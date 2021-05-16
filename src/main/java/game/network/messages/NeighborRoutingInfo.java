package game.network.messages;

import game.api.NetworkCell;
import game.gameoflife.AliveRequest;
import game.gameoflife.AliveResponse;

public class NeighborRoutingInfo {

	private NetworkCell sourceNode;
	private NetworkCell targetNode;
	private Integer targetPort;

	public NeighborRoutingInfo(NetworkCell sourceNode, NetworkCell targetNode, Integer targetPort) {
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		this.targetPort = targetPort;
	}

	//TODO remove getters as far as possible
	
	public NetworkCell getSourceNode() {
		return sourceNode;
	}

	public NetworkCell getTargetNode() {
		return targetNode;
	}

	public Integer getTargetPort() {
		return targetPort;
	}

	public void send(AliveRequest request) {
		targetNode.send(new Message(this, request));
	}

	public void respond(AliveResponse aliveResponse) {
		sourceNode.send(new Message(this, aliveResponse));
	}
}
