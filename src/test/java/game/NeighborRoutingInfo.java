package game;

import game.main.Node;

public class NeighborRoutingInfo {

	private Integer targetPort;
	private Node targetNode;

	public NeighborRoutingInfo(Integer targetPort, Node targetNode) {
		this.targetPort = targetPort;
		this.targetNode = targetNode;
	}

	public Integer getTargetPort() {
		return targetPort;
	}

	public Node getTargetNode() {
		return targetNode;
	}
}
