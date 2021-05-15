package game.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class Threads {

	private ExecutorService executor;
	private AtomicInteger busy = new AtomicInteger(0);

	public Threads(ExecutorService executor) {
		this.executor = executor;
	}

	public void submit(Runnable runnable ) {
		executor.submit(() -> {
			busy.incrementAndGet();
			runnable.run();
			busy.decrementAndGet();
		});
	}
	
	public int getBusyCount() {
		return busy.get();
	}

}
