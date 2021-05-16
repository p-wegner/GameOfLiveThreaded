package game.main.gen2;

public interface NeighborEditable {

	void updateNeighbor(int neighbor, NetworkNode node);

	void linkWithTopLeftneighbors();

	void linkWithCornerNeighbors();
}
