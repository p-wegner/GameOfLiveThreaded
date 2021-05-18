package game.gameoflife;

import java.util.List;
import java.util.stream.Collectors;

import org.reflections.Reflections;

public class WillLiveRuleProvider {
	private static Reflections reflections = new Reflections("game");
	static List<? extends WillLiveRule> allRules = reflections.getSubTypesOf(WillLiveRule.class).stream()
			.map(clazz -> {
				try {
					return clazz.getDeclaredConstructor().newInstance();
				} catch (Exception e) {
					// what to do?
					return null;
				}
			}).collect(Collectors.toList());

	public static boolean oneRuleMatches(boolean alive, int neighborsAlive) {
		return allRules.stream().anyMatch(rule -> rule.willLive(alive, neighborsAlive));

	}

}
