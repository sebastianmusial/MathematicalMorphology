package pl.polsl.mathematicalMorphology;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Sebastian Musiał
 */
public final class Filters extends ArrayList<Filter> {

	private static Filters filters;

	private Filters() {
		add(new BinaryErosion());
		add(new BinaryDilation());
	}

	public static Filters getFilters() {
		if (filters == null) {
			filters = new Filters();
		}
		return filters;
	}

	public static Optional<Filter> findByName(String name) {
		for (Filter f : filters) {
			if (f.toString().contains(name)) {
				return Optional.of(f);
			}
		}
		return Optional.empty();
	}

	public static <T extends Filter> Optional<T> findByClass(Class<T> x) {
		for (Filter f : filters) {
			if (f.getClass().equals(x)) {
				T result;
				try {
					result = (T) f;
				} catch (Exception e) {
					result = null;
				}
				return Optional.of(result);
			}
		}
		return Optional.empty();
	}
}
