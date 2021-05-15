package game;

public class Debug {

	static final boolean enabled = false;

	static void print(String s) {
		if (enabled)
			System.out.print(s);
	}

	static void println(String s) {
		if (enabled)
			System.out.println(s);
	}
}
