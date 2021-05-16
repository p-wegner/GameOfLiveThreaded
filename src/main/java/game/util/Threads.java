package game.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Threads {

	private ExecutorService executor;
	private AtomicInteger busy = new AtomicInteger(0);
	private Map<Object, AtomicBoolean> threadRunningForObject = new HashMap<>();

	public Threads(ExecutorService executor) {
		this.executor = executor;
	}

	public void submit(Object supervisingObject, Runnable runnable) {
		AtomicBoolean threadRunning = threadRunningForObject.computeIfAbsent(supervisingObject,
				__ -> new AtomicBoolean(false));

		if (!threadRunning.compareAndExchange(false, true)) {
			executor.submit(() -> {
				busy.incrementAndGet();
				try {
					runnable.run();
				} catch (Exception ex) {
					System.out.println("THREADFAIL: " + ex);
				}
				busy.decrementAndGet();
				threadRunning.set(false);
			});
		}
	}

	public int getBusyCount() {
		return busy.get();
	}

	public void randomWaiter(int base, int random) {
		ThreadingUtil.sleep((int) (Math.random() * random) + base);
	}
}
