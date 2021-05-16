package game.network.factory;

// TODO move into CellAware?
public interface HasContainerReference<T> {
	//TODO remove
	T getContainer();

	// pretty much impossible to avoid as container and containee know each other
	void setContainer(T container);
}