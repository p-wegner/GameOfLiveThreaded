package game.network.factory;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import game.network.Network;

public class NetworkFactoryTest {

	@Test
	void testNetworkCreateSmall() throws Exception {
		NetworkFactory networkFactory = new NetworkFactory(2, 2, NetworkCellFactory.createDefault());
		Network network = networkFactory.create();

		System.out.println(network.dumpDebug());

		fail();
	}

	@Test
	void testNetworkCreateLarge() throws Exception {
		NetworkFactory networkFactory = new NetworkFactory(15, 15, NetworkCellFactory.createDefault());
		Network network = networkFactory.create();

		System.out.println(network.dumpDebug());

		fail();
	}

}
