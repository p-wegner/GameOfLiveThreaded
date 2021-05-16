package game.globals;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO synchronized... synchronized everywhere
public class Stats {

	static Map<String, Supplier<Long>> suppliers = Collections.synchronizedMap(new HashMap<>());
	static Map<String, Long> stats = Collections.synchronizedMap(new HashMap<>());
	static Map<String, Map<Long, Long>> histo = Collections.synchronizedMap(new HashMap<>());

	public static synchronized void inc(String value) {
		Long long1 = stats.getOrDefault(value, 0l);
		stats.put(value, long1 + 1);
	}

	public static synchronized void histo(String value, long size) {
		Map<Long, Long> newHistoMap = histo.computeIfAbsent(value, __ -> Collections.synchronizedMap(new HashMap<>()));
		newHistoMap.computeIfAbsent((long) size, __ -> 0l);
		newHistoMap.computeIfPresent((long) size, (__, v) -> v + 1);
	}

	public static void provider(String value, Supplier<Long> statSupplier) {
		suppliers.put(value, statSupplier);
	}

	public static String stats() {
		Stream<String> supplierStream = suppliers.entrySet().stream()
				.map(entry -> entry.getKey() + ": " + entry.getValue().get());
		Stream<String> statsStream = stats.entrySet().stream()
				.map(entry -> entry.getKey() + ": " + entry.getValue());

		return Stream.concat(supplierStream, statsStream)
				.collect(Collectors.joining("\n"));
	}

	public static String histos() {
		return histo.entrySet().stream()
				.map(entry -> entry.getKey() + ": " + printHisto(entry.getKey(), entry.getValue()))
				.collect(Collectors.joining("\n"));
	}

	private static String printHisto(String key, Map<Long, Long> value) {
		return value.entrySet().stream()
				.sorted(Comparator.comparing(entry -> entry.getKey()))
				.map(entry -> entry.getKey() + ": x" + entry.getValue())
				.collect(Collectors.joining(" | "));
	}

	public static void histo(String value, int size) {
		histo(value, (long) size);
	}

}
