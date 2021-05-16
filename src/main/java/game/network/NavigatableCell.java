package game.network;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import game.api.DataContainer;
import game.api.NetworkCell;
import game.gameoflife.AliveRequest;
import game.main.NeighborRoutingInfo;

public interface NavigatableCell extends DataContainer {

	enum Direction {
		TOP(1, node -> node.top()),
		DOWN(6, node -> node.down()),
		LEFT(3, node -> node.left()),
		RIGHT(4, node -> node.right());

//		private int neighbor;
		private Function<NavigatableCell, NavigatableCell> next;

		Direction(int neighbor, Function<NavigatableCell, NavigatableCell> next) {
//			this.neighbor = neighbor;
			this.next = next;
		}

		public NavigatableCell next(NavigatableCell navigatable) {
			return next.apply(navigatable);
		}
	}

	NetworkCell top();

	NetworkCell left();

	NetworkCell down();

	NetworkCell right();
	
	boolean isIterable();	

	default Stream<NavigatableCell> stream(Direction direction) {
		Iterator<NavigatableCell> iterator = iterator(direction);
		Iterable<NavigatableCell> iterable = () -> iterator;
		return StreamSupport.stream(iterable.spliterator(), false);
	}
	
	default Iterator<NavigatableCell> iterator(Direction direction) {
		final NavigatableCell instance = this;
		
		return new Iterator<NavigatableCell>() {
			private NavigatableCell current = instance;

			@Override
			public boolean hasNext() {
				return current.isIterable();
			}

			@Override
			public NavigatableCell next() {
				NavigatableCell value = current;
				current = direction.next(current);
				return value;
			}
		};
	}

	// TODO Put into own interface, which will probably be the datacell facade for the cell
	Stream<NeighborRoutingInfo> neighborsRoutingStream();

	boolean containsNeighbor(NetworkCell node);
}