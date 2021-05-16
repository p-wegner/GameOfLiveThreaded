package game.main.gen2;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Navigatable {

	enum Direction {
		TOP(1, node -> node.top()),
		DOWN(6, node -> node.down()),
		LEFT(3, node -> node.left()),
		RIGHT(4, node -> node.right());

//		private int neighbor;
		private Function<Navigatable, Navigatable> next;

		Direction(int neighbor, Function<Navigatable, Navigatable> next) {
//			this.neighbor = neighbor;
			this.next = next;
		}

		public Navigatable next(Navigatable networkNode) {
			return next.apply(networkNode);
		}
	}

	NetworkNode top();

	NetworkNode left();

	NetworkNode down();

	NetworkNode right();
	
	boolean isIteratable();	

	default Stream<Navigatable> stream(Direction direction) {
		Iterator<Navigatable> iterator = iterator(direction);
		Iterable<Navigatable> iterable = () -> iterator;
		return StreamSupport.stream(iterable.spliterator(), false);
	}
	
	default Iterator<Navigatable> iterator(Direction direction) {
		final Navigatable instance = this;
		
		return new Iterator<Navigatable>() {
			private Navigatable current = instance;

			@Override
			public boolean hasNext() {
				return current.isIteratable();
			}

			@Override
			public Navigatable next() {
				Navigatable value = current;
				current = direction.next(current);
				return value;
			}
		};
	}
}