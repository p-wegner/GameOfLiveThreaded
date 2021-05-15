package game.globals;

public class Debug {

	static final boolean enabled = false;
	static final boolean info = false;

	public static void print(String s) {
		if (enabled)
			System.out.print(s);
	}

	public static void println(String s) {
		if (enabled)
			System.out.println(s);
	}

	public static void info(String s) {
		if (info)
			System.out.println(s);
	}
}
