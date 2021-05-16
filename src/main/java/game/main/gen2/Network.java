package game.main.gen2;

import java.util.List;
import java.util.function.Consumer;

import game.util.StreamUtil;

public class Network {

	private List<NetworkNode> nodes;
	private int width;
	private int height;

	public Network(List<NetworkNode> nodes, int width, int height) {
		this.nodes = nodes;
		this.width = width;
		this.height = height;
	}

	public String dumpDebug() {
		StringBuilder sb = new StringBuilder();
		iterateByLine(
				navi -> {
					sb.append(navi.toString());
					sb.append(" ");
				},
				() -> sb.append("\n"));
		return sb.toString();
	}

	public void iterateByLine(Consumer<Navigatable> callback, Runnable onNextLine) {
		Navigatable topLeft = findTopLeft(nodes.get(0));

		topLeft.stream(Navigatable.Direction.DOWN)
				.peek(__ -> onNextLine.run())
				.flatMap(navi -> navi.stream(Navigatable.Direction.RIGHT))
				.forEach(callback);
	}

	private Navigatable findTopLeft(Navigatable middleNode) {
		Navigatable leftMost = StreamUtil.getLast(middleNode.iterator(Navigatable.Direction.LEFT));
		Navigatable topLeft = StreamUtil.getLast(leftMost.iterator(Navigatable.Direction.TOP));
		return topLeft;
	}
}
