package game.api;

public interface NeighborEditable {

	void updateNeighbor(int neighbor, NetworkCell node);

	void linkWithTopLeftneighbors();

	void linkWithCornerNeighbors();
}
