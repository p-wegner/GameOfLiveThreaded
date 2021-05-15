package game.globals;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Stats {

	static Map<String, Long> stats = new HashMap<>();

	public static synchronized void inc(String value) {
		Long long1 = stats.getOrDefault(value, 0l);
		stats.put(value, long1 + 1);
	}

	public static String stats() {
		return stats.entrySet().stream()
				.map(entry -> entry.getKey() + ": " + entry.getValue())
				.collect(Collectors.joining("\n"));
	}

}
