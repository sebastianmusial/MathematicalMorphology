package pl.polsl.mathematicalMorphology;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Sebastian Musiał
 */
public final class Filters extends ArrayList<Filter> {

	private static Filters filters;

	private Filters() {

		// ---------------------
		// erozja 3x3
		// ---------------------
		add(new Erosion3x3(
				"Erozja 3x3 - el. str. same jedynki",
				new int[][] {
					{1, 1, 1},
					{1, 1, 1},
					{1, 1, 1}
				}
		));

		add(new Erosion3x3(
				"Erozja 3x3 - el. str. 'plus'",
				new int[][] {
					{0, 1, 0},
					{1, 1, 1},
					{0, 1, 0}
			}
		));

		add(new Erosion3x3(
				"Erozja 3x3 - el. str. 'minus'",
				new int[][] {
					{0, 0, 0},
					{1, 1, 1},
					{0, 0, 0}
				}
		));

		add(new Erosion3x3(
				"Erozja 3x3 - el. str. 'pionowa kreska'",
				new int[][] {
						{0, 1, 0},
						{0, 1, 0},
						{0, 1, 0}
				}
		));

		// ---------------------
		// erozja 5x5
		// ---------------------
		add(new Erosion5x5(
				"Erozja 5x5 - el. str. 'same jedynki'",
				new int[][] {
						{1, 1, 1, 1, 1},
						{1, 1, 1, 1, 1},
						{1, 1, 1, 1, 1},
						{1, 1, 1, 1, 1},
						{1, 1, 1, 1, 1}
				}
		));

		add(new Erosion5x5(
				"Erozja 5x5 - el. str. 'plus'",
				new int[][] {
						{0, 0, 1, 0, 0},
						{0, 0, 1, 0, 0},
						{1, 1, 1, 1, 1},
						{0, 0, 1, 0, 0},
						{0, 0, 1, 0, 0}
				}
		));

		add(new Erosion5x5(
				"Erozja 5x5 - el. str. 'minus'",
				new int[][] {
						{0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0},
						{1, 1, 1, 1, 1},
						{0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0}
				}
		));

		add(new Erosion5x5(
				"Erozja 5x5 - el. str. 'pionowa kreska'",
				new int[][] {
						{0, 0, 1, 0, 0},
						{0, 0, 1, 0, 0},
						{0, 0, 1, 0, 0},
						{0, 0, 1, 0, 0},
						{0, 0, 1, 0, 0}
				}
		));

		// ---------------------
		// dylatacja 3x3
		// ---------------------
		add(new Dilation3x3(
				"Dylatacja 3x3 - el. str. same jedynki",
				new int[][] {
						{1, 1, 1},
						{1, 1, 1},
						{1, 1, 1}
				}
		));

		add(new Dilation3x3(
				"Dylatacja 3x3 - el. str. 'plus'",
				new int[][] {
						{0, 1, 0},
						{1, 1, 1},
						{0, 1, 0}
				}
		));

		add(new Dilation3x3(
				"Dylatacja 3x3 - el. str. 'minus'",
				new int[][] {
						{0, 0, 0},
						{1, 1, 1},
						{0, 0, 0}
				}
		));

		add(new Dilation3x3(
				"Dylatacja 3x3 - el. str. 'pionowa kreska'",
				new int[][] {
						{0, 1, 0},
						{0, 1, 0},
						{0, 1, 0}
				}
		));

		// ---------------------
		// dylatacja 5x5
		// ---------------------
		add(new Dilation5x5(
				"Dylatacja 5x5 - el. str. 'same jedynki'",
				new int[][] {
						{1, 1, 1, 1, 1},
						{1, 1, 1, 1, 1},
						{1, 1, 1, 1, 1},
						{1, 1, 1, 1, 1},
						{1, 1, 1, 1, 1}
				}
		));

		add(new Dilation5x5(
				"Dylatacja 5x5 - el. str. 'plus'",
				new int[][] {
						{0, 0, 1, 0, 0},
						{0, 0, 1, 0, 0},
						{1, 1, 1, 1, 1},
						{0, 0, 1, 0, 0},
						{0, 0, 1, 0, 0}
				}
		));

		add(new Dilation5x5(
				"Dylatacja 5x5 - el. str. 'minus'",
				new int[][] {
						{0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0},
						{1, 1, 1, 1, 1},
						{0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0}
				}
		));

		add(new Dilation5x5(
				"Dylatacja 5x5 - el. str. 'pionowa kreska'",
				new int[][] {
						{0, 0, 1, 0, 0},
						{0, 0, 1, 0, 0},
						{0, 0, 1, 0, 0},
						{0, 0, 1, 0, 0},
						{0, 0, 1, 0, 0}
				}
		));
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
