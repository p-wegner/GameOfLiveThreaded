package game.network.messages;

import java.util.function.Consumer;

public class Message {

	// TODO will be moved here out of the objects
	private NeighborRoutingInfo neighborRoutingInfo;
	private Object object;

	public Message(NeighborRoutingInfo neighborRoutingInfo, Object object) {
		this.neighborRoutingInfo = neighborRoutingInfo;
		this.object = object;
	}

	@SuppressWarnings("unchecked")
	public <T> void ifPresent(Class<T> class1, Consumer<T> consumer) {
		if (class1.isAssignableFrom(object.getClass())) {
			consumer.accept((T)object);
		}
	}

}
