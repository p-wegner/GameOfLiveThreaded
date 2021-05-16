package game.network;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import game.api.NetworkCell;
import game.util.RunnableUtil;
import game.util.StreamUtil;

public class Network {

	private List<NetworkCell> nodes;
//	private int width;
//	private int height;

	public Network(List<NetworkCell> nodes, int width, int height) {
		this.nodes = nodes;
//		this.width = width;
//		this.height = height;
	}

	public String dumpDebug() {
		return dump(navi -> navi.toString());
	}

	public <D, V> String dumpData(Function<D, String> mapper) {
		return dump(navi -> mapper.apply(navi.getData()));
	}

	private <D, V> String dump(Function<NavigatableCell, String> mapper) {
		StringBuilder sb = new StringBuilder();
		iterateByLine(
				navi -> {
					sb.append(mapper.apply(navi));
					sb.append(" ");
				},
				() -> sb.append("\n"));
		return sb.toString();
	}

	public <X> void iterateDataByLine(Consumer<X> callback) {
		iterateDataByLine(callback, RunnableUtil.NO_ACTION);
	}

	public <X> void iterateDataByLine(Consumer<X> callback, Runnable onNextLine) {
		iterateByLine(cell -> callback.accept(cell.getData()), onNextLine);
	}

	public void iterateByLine(Consumer<NavigatableCell> callback) {
		iterateByLine(callback, RunnableUtil.NO_ACTION);
	}

	public void iterateByLine(Consumer<NavigatableCell> callback, Runnable onNextLine) {
		NavigatableCell topLeft = findTopLeft(nodes.get(0));

		topLeft.stream(NavigatableCell.Direction.DOWN)
				.peek(__ -> onNextLine.run())
				.flatMap(navi -> navi.stream(NavigatableCell.Direction.RIGHT))
				.forEach(callback);
	}

	private NavigatableCell findTopLeft(NavigatableCell middleNode) {
		NavigatableCell leftMost = StreamUtil.getLast(middleNode.iterator(NavigatableCell.Direction.LEFT));
		NavigatableCell topLeft = StreamUtil.getLast(leftMost.iterator(NavigatableCell.Direction.TOP));
		return topLeft;
	}
}
