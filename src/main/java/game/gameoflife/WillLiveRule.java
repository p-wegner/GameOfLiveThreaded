package game.gameoflife;

import java.util.List;

public interface WillLiveRule {
	boolean willLive(boolean selfAlive, int livingNeighbours);

	public static final List<Integer> NEEDED_NEIGHBOURS_TO_SURVIVE = List.of(2, 3);

	class AliveWithNeighbours implements WillLiveRule {
		@Override
		public boolean willLive(boolean selfAlive, int livingNeighbours) {
			return selfAlive && (NEEDED_NEIGHBOURS_TO_SURVIVE.contains(livingNeighbours));
		}

	}

	class NotAliveWithNeighbours implements WillLiveRule {
		@Override
		public boolean willLive(boolean selfAlive, int livingNeighbours) {
			return !selfAlive && (livingNeighbours == 3);
		}
	}
}
