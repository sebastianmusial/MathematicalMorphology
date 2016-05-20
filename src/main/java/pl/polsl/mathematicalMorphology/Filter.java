package pl.polsl.mathematicalMorphology;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;

/**
 * RGB to HSI filter
 */
class RGB2HSI extends Filter {
	public RGB2HSI() {
		super("RGB2HSI", new int[][]{{1}});

		pixelProcedure = (x, y) -> {
			int color = source.getRGB(x, y);
			dest.setRGB(x, y, rgb2hsi(color));
		};
	}

	private int rgb2hsi(int rgb){
		float[] hsi = new float[3];
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = (rgb) & 0xFF;
		Color.RGBtoHSB(r, g, b, hsi);
		hsi[0] = (hsi[0]) * 250;
		hsi[1] = (hsi[1]) * 250;
		hsi[2] = (hsi[2]) * 250;
		return (int)Math.round(hsi[0])*65536 + (int)Math.round(hsi[1])*256 + (int)Math.round(hsi[2]);
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

	protected Filter(String name, int[][] filter) {
		this.name = name;
		this.filter = filter;
		//this.imageType = BufferedImage.TYPE_BYTE_GRAY;
		this.imageType = BufferedImage.TYPE_INT_RGB;

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
