package game.main.gen2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NetworkFactoryTest {

	@Test
	void testNetworkCreateSmall() throws Exception {
		NetworkFactory networkFactory = new NetworkFactory(2, 2);
		Network network = networkFactory.create();

		System.out.println(network.dumpDebug());

		fail();
	}

	@Test
	void testNetworkCreateLarge() throws Exception {
		NetworkFactory networkFactory = new NetworkFactory(15, 15);
		Network network = networkFactory.create();

		System.out.println(network.dumpDebug());

		fail();
	}

}
