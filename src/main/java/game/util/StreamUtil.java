package game.util;

import java.util.Iterator;

public class StreamUtil {

	public static <X> X getLast(Iterator<X> iterator) {
		X last = null; 
		while (iterator.hasNext()) {
			last = iterator.next();
		}
		return last;
	}

}
