package mathematicalMorphology.test;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;

/**
 * Negacja
 */
class Negate extends Filter {
	public Negate() {
		super("Negacja", new int[][]{{1}});

		pixelProcedure = (x, y) -> {
			int color = source.getRGB(x, y);
			int r, g, b;
			r = (color >> 16) & 0xFF;
			g = (color >> 8) & 0xFF;
			b = (color >> 0) & 0xFF;
			r = 255 - r;
			g = 255 - g;
			b = 255 - b;
			color = (r << 16) | (g << 8) | b;
			dest.setRGB(x, y, color);
		};
	}
}

class BinaryErosion extends Filter {
	public BinaryErosion() {
		super(
				"Erozja",
				new int[][]{
						{1, 1, 1},
						{1, 1, 1},
						{1, 1, 1}
				}
		);

		imageType = BufferedImage.TYPE_BYTE_BINARY;
		pixelProcedure = erosionPixelFilter;
	}
}

/**
 * Weaker than BinaryErosion
 */
class BinaryErosion2 extends Filter {
	public BinaryErosion2() {
		super(
				"Erozja 2",
				new int[][]{
						{0, 1, 0},
						{1, 1, 1},
						{0, 1, 0}
				}
		);

		imageType = BufferedImage.TYPE_BYTE_BINARY;
		pixelProcedure = erosionPixelFilter;
	}
}

/**
 * Stronger than BinaryErosion
 */
class BinaryErosion3 extends Filter {
	public BinaryErosion3() {
		super("Erozja 3",
				new int[][]{
						{1, 1, 1},
						{1, 1, 1},
						{0, 0, 0}
				});
		imageType = BufferedImage.TYPE_BYTE_BINARY;

	}

	@Override
	public Filter filter() {
		pixelProcedure = erosionPixelFilter;
		filterImage();
		rotateFilter();
		createNewDest();

		filterImage();
		rotateFilter();
		createNewDest();

		filterImage();
		rotateFilter();
		createNewDest();

		filterImage();
		dest = null;
		return this;
	}
}

class BinaryDilation extends Filter {
	public BinaryDilation() {
		super(
				"Dylatacja",
				new int[][]{
						{1, 1, 1},
						{1, 1, 1},
						{1, 1, 1}
				}
		);
		imageType = BufferedImage.TYPE_BYTE_BINARY;
		pixelProcedure = dilationPixelFilter;
	}
}

/**
 * Weaker than BinaryDlilation
 */
class BinaryDilation2 extends Filter {
	public BinaryDilation2() {
		super(
				"Dylatacja 2",
				new int[][]{
						{0, 1, 0},
						{1, 1, 1},
						{0, 1, 0}
				}
		);
		imageType = BufferedImage.TYPE_BYTE_BINARY;
		pixelProcedure = dilationPixelFilter;
	}
}

/**
 * Work in progress
 */
class BinaryScelet extends Filter {
	public BinaryScelet() {
		super(
				"Szkieletyzacja",
				new int[][]{
						{0, 0, 0},
						{1, 1, 1},
						{1, 1, 1}
				}
		);

		this.imageType = BufferedImage.TYPE_BYTE_BINARY;
	}

	@Override
	public Filter filter() {
		pixelProcedure = skeletonPixelFilter;
		filterImage();
		createNewDest();
		rotateFilter();

		//filterImage();
		dest = null;
		return this;
	}


}

/**
 * Detect horizontalIndexes edges
 */
class HorizontalPrewitt extends Filter {
	public HorizontalPrewitt() {
		super("poziomy Prewitt",
				new int[][]{{-1, -1, -1}, {0, 0, 0}, {1, 1, 1}});
	}
}

/**
 * Detect horizontalIndexes edges. Theoretically better than Prewitt
 */
class HorizontalSobel extends Filter {
	public HorizontalSobel() {
		super("poziomy Sobel",
				new int[][]{{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}});
	}
}

/**
 * Detect verticalIndexes edges
 */
class VerticalPrewitt extends Filter {
	public VerticalPrewitt() {
		super("pionowy Prewitt",
				new int[][]{
						{-1, 0, 1},
						{-1, 0, 1},
						{-1, 0, 1}}
		);
	}
}

/**
 * Detect verticalIndexes edges. Theoretically better than Prewitt.
 */
class VerticalSobel extends Filter {
	public VerticalSobel() {
		super("pionowy Sobel",
				new int[][]{
						{-1, 0, 1},
						{-2, 0, 2},
						{-1, 0, 1}}
		);
	}
}

/**
 * Smooth picture
 */
class Gauss5x5 extends Filter {
	public Gauss5x5() {
		super("Gauss 5x5",
				new int[][]{
						{2, 4, 5, 4, 2},
						{4, 9, 12, 9, 4},
						{5, 12, 15, 12, 5},
						{4, 9, 12, 9, 4},
						{2, 4, 5, 4, 2}});
	}
}

/**
 * Smooth picture. Greater radius.
 */
class BinarySmooth7x7 extends Filter {
	public BinarySmooth7x7() {
		super(
				"Wyładzanie binarne 7x7",
				new int[][]{
						{0, 1, 2, 4, 2, 1, 0},
						{1, 2, 4, 8, 4, 2, 1},
						{2, 4, 8, 16, 8, 4, 2},
						{4, 8, 16, 32, 16, 8, 4},
						{2, 4, 8, 16, 8, 4, 2},
						{1, 2, 4, 8, 4, 2, 1},
						{0, 1, 2, 4, 2, 1, 0}
				});
	}
}

/**
 * Small radius gauss smooth.
 */
class Gauss3x3 extends Filter {
	public Gauss3x3() {
		super("Gauss 3x3",
				new int[][]{{1, 2, 1}, {2, 4, 2}, {1, 2, 1}});
	}
}

/**
 * Smooth based on avarage value.
 */
class Smooth extends Filter {
	public Smooth() {
		super("Wygładzenie R1",
				new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
	}
}

/**
 * Simple binarization
 */
class Binarize extends Filter {
	public Binarize() {
		super("Binaryzacja", new int[][]{{1}});
		imageType = BufferedImage.TYPE_BYTE_BINARY;
	}
}

/**
 * Detect edges in all directions
 */
class AllEdges extends Filter {
	public AllEdges() {
		super(
				"Wszystkie krawędzie",
				new int[][]{
						{-1, -1, -1},
						{-1, 8, -1},
						{-1, -1, -1}
				}
		);
		imageType = BufferedImage.TYPE_BYTE_BINARY;
	}
}

/**
 * Detects edges in all directions. Inverse.
 */
class AllEdgesInverted extends Filter {
	public AllEdgesInverted() {
		super(
				"Wszystkie krawędzie odwrócone",
				new int[][]{
						{1, 1, 1},
						{1, -8, 1},
						{1, 1, 1}
				}
		);

		imageType = BufferedImage.TYPE_BYTE_BINARY;
	}
}

/**
 * Custom filter
 */
class CustomFilter extends Filter {
	public CustomFilter() {
		super("filtr testowy", new int[][]{{1}});
	}

	@Override
	public Filter filter() {

		Gauss3x3 gs = (Gauss3x3) Filters.findByClass(Gauss3x3.class).get();
		VerticalPrewitt vp = ((VerticalPrewitt) Filters.findByClass(VerticalPrewitt.class).get());

		gs.source = source;
		gs.filter();

		vp.source = gs.source;
		vp.filter();

		source = vp.source;
		dest = null;
		return this;
	}
}

/**
 * An abstract Filter class
 *
 * @author Sebastian Musial
 */
public abstract class Filter {
	protected BufferedImage source;
	protected BufferedImage dest;

	int[][] filter;
	int totalFilterWeight;
	int filterHalfWidth;
	int filterHalfHeight;
	int imageType;

	private String name;
	protected BiConsumer<Integer, Integer> pixelProcedure;

	/**
	 * This is the actual filter method which is applied to every pixel.
	 */
	protected final BiConsumer<Integer, Integer> filterImagePixel = (x, y) -> {
		int dr = 0, dg = 0, db = 0, r, g, b, color;
		for (int row = 0; row < filter.length; row++) {
			for (int col = 0; col < filter[row].length; col++) {
				color = source.getRGB(x - filterHalfWidth + col, y - filterHalfHeight + row);
				r = color >> 16 & 0xFF;
				g = color >> 8 & 0xFF;
				b = color & 0xFF;
				r *= filter[row][col];
				g *= filter[row][col];
				b *= filter[row][col];
				dr += r;
				dg += g;
				db += b;
			}
		}
		dr /= totalFilterWeight;
		dg /= totalFilterWeight;
		db /= totalFilterWeight;
		color = (dr << 16) | (dg << 8) | db;
		dest.setRGB(x, y, color);
	};

	/**
	 * Sets current pixel's color to black if any of the masked pixels are black
	 */
	protected final BiConsumer<Integer, Integer> erosionPixelFilter = (x, y) -> {
		int color = 0;
		boolean setToBlack = false;

		for (int row = 0; row < filter.length; row++) {
			for (int col = 0; col < filter[row].length; col++) {
				color = source.getRGB(x - filterHalfWidth + col, y - filterHalfHeight + row);
				//color >>= 16;
				color &= 0xFF;
				int mask = filter[row][col];
				if (mask > 0 && color == 0) {
					setToBlack |= true;
				}
			}
		}
		if (setToBlack) {
			color &= 0xFF000000;
		} else {
			color |= 0x00FFFFFF;
		}

		dest.setRGB(x, y, color);
	};

	protected final BiConsumer<Integer, Integer> dilationPixelFilter = (x, y) -> {
		int color = 0;
		boolean setToWhite = false;

		for (int row = 0; row < filter.length; row++) {
			for (int col = 0; col < filter[row].length; col++) {
				color = source.getRGB(x - filterHalfWidth + col, y - filterHalfHeight + row);
				//color >>= 16;
				color &= 0xFF;
				int mask = filter[row][col];
				if (mask > 0 && color > 0) {
					setToWhite |= true;
				}
			}
		}
		if (setToWhite) {
			color |= 0x00FFFFFF;
		} else {
			color &= 0xFF000000;
		}

		dest.setRGB(x, y, color);
	};

	protected final BiConsumer<Integer, Integer> skeletonPixelFilter = (x, y) -> {
		int color = 0;
		boolean setToBlack = true;

		for (int row = 0; row < filter.length; row++) {
			for (int col = 0; col < filter[row].length; col++) {
				color = source.getRGB(x - filterHalfWidth + col, y - filterHalfHeight + row);
				color &= 0xFF;
				int mask = filter[row][col];
				if ((mask > 0 && color == 0) || (mask == 0 && color > 0)) {
					setToBlack &= false;
				}
			}
		}
		if (setToBlack) {
			color &= 0xFF000000;
		} else {
			color |= 0x00FFFFFF;
		}

		dest.setRGB(x, y, color);
	};

	protected void negateFilter() {
		for (int i = 0; i < filter.length; i++) {
			for (int j = 0; j < filter[i].length; j++) {
				filter[i][j] = filter[i][j] == 0 ? 255 : 0;
			}
		}
	}

	protected void rotateFilter() {
		int n = filter.length;
		int tmp;
		for (int i = 0; i < n / 2; i++) {
			for (int j = i; j < n - i - 1; j++) {
				tmp = filter[i][j];
				filter[i][j] = filter[j][n - i - 1];
				filter[j][n - i - 1] = filter[n - i - 1][n - j - 1];
				filter[n - i - 1][n - j - 1] = filter[n - j - 1][i];
				filter[n - j - 1][i] = tmp;
			}
		}
	}

	protected Filter(String name, int[][] filter) {
		this.name = name;
		this.filter = filter;
		//this.imageType = BufferedImage.TYPE_BYTE_GRAY;
		this.imageType = BufferedImage.TYPE_INT_RGB;
		this.pixelProcedure = filterImagePixel;

		calculateTotalFilterWeight();
	}

	/**
	 * Calculate the sum of weights of all filter fields.
	 */
	private void calculateTotalFilterWeight() {
		totalFilterWeight = 0;
		filterHalfHeight = (filter.length - 1) / 2;
		filterHalfWidth = (filter[0].length - 1) / 2;
		for (int row = 0; row < filter.length; row++) {
			for (int col = 0; col < filter[row].length; col++) {
				totalFilterWeight += filter[row][col];
			}
		}
		if (totalFilterWeight == 0) {
			totalFilterWeight = 1;
		}
	}

	/**
	 * Set the image to be filtered.
	 *
	 * @param image source image
	 * @return
	 */
	public Filter withImage(Image image) {
		source = SwingFXUtils.fromFXImage(image, null);
		createNewDest();
		return this;
	}

	protected void createNewDest() {
		dest = new BufferedImage(source.getWidth(), source.getHeight(), imageType);
	}

	/**
	 * Do the filtering.
	 *
	 * @return
	 */
	public Filter filter() {
		filterImage();
		dest = null;
		return this;
	}

	protected final void filterImage() {
		for (int row = filterHalfWidth; row < source.getHeight() - filterHalfWidth; row++) {
			filterImageRow(row);
		}
		source = dest;
	}

	/**
	 * Applies filter to a single row.
	 *
	 * @param row index of the row to be filtered
	 */
	protected final void filterImageRow(int row) {
		for (int col = filterHalfHeight; col < source.getWidth() - filterHalfHeight; col++) {
			pixelProcedure.accept(col, row);
		}
	}

	/**
	 * Output the filtered image to an ImageView.
	 *
	 * @param targetView
	 */
	public void setImage(ImageView targetView) {
		targetView.setImage(SwingFXUtils.toFXImage(source, null));
		source = null;
		dest = null;
	}

	@Override
	public String toString() {
		return name;
	}
}
