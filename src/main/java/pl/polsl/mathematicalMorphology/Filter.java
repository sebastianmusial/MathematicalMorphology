package pl.polsl.mathematicalMorphology;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * RGB to HSI filter
 */
class RGB2HSI extends Filter {
	float[][][] hsiValues;

	public RGB2HSI() {
		super("RGB2HSI", new int[][]{{1}});

		pixelProcedure = (x, y) -> {
			hsiValues[x][y] = rgb2hsi(source.getRGB(x, y));
		};
	}

	@Override
	protected Filter withImage(BufferedImage bufferedImage) {
		Filter filter = super.withImage(bufferedImage);
		hsiValues = new float[source.getWidth()][source.getHeight()][3];
		return filter;
	}

	private float[] rgb2hsi(int rgb){
		float[] hsi = new float[3];
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = (rgb) & 0xFF;
		Color.RGBtoHSB(r, g, b, hsi);
		return hsi;
	}
}

class Erosion extends Filter {
	public Erosion() {
		super(
				"Erozja",
				new int[][]{
						{1, 1, 1},
						{1, 1, 1},
						{1, 1, 1}
				}
		);

		pixelProcedure = erosionPixelFilter;
	}

	@Override
	protected Filter withImage(BufferedImage bufferedImage) {
		Filter filter = super.withImage(bufferedImage);
		calcHsiValues();
		return filter;
	}
}

class Dilation extends Filter {
	public Dilation() {
		super(
				"Dylatacja",
				new int[][]{
						{1, 1, 1},
						{1, 1, 1},
						{1, 1, 1}
				}
		);
		pixelProcedure = dilationPixelFilter;
	}

	@Override
	protected Filter withImage(BufferedImage bufferedImage) {
		Filter filter = super.withImage(bufferedImage);
		calcHsiValues();
		return filter;
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

	RGBHSIPixel[][] pixels;

	/**
	 * Sets current pixel's color to black if any of the masked pixels are black
	 */
	protected final BiConsumer<Integer, Integer> erosionPixelFilter = (x, y) -> {
		List<RGBHSIPixel> pixelsToConsider = new ArrayList<>();

		for (int row = 0; row < filter.length; row++) {
			for (int col = 0; col < filter[row].length; col++) {
				if(filter[row][col] == 1) {
					pixelsToConsider.add(pixels[x - filterHalfWidth + col][y - filterHalfHeight + row]);
				}
			}
		}

        dest.setRGB(x, y, Collections.min(pixelsToConsider, Formulas::comparePixels).getRgb());
	};

	protected final BiConsumer<Integer, Integer> dilationPixelFilter = (x, y) -> {
		List<RGBHSIPixel> pixelsToConsider = new ArrayList<>();

		for (int row = 0; row < filter.length; row++) {
			for (int col = 0; col < filter[row].length; col++) {
				if(filter[row][col] == 1) {
					pixelsToConsider.add(pixels[x - filterHalfWidth + col][y - filterHalfHeight + row]);
				}
			}
		}
		dest.setRGB(x, y, Collections.max(pixelsToConsider, Formulas::comparePixels).getRgb());
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
		return withImage(SwingFXUtils.fromFXImage(image, null));
	}

	protected Filter withImage(BufferedImage bufferedImage) {
		source = bufferedImage;
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
		for (int row = filterHalfHeight; row < source.getHeight() - filterHalfHeight; row++) {
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
		for (int col = filterHalfWidth; col < source.getWidth() - filterHalfWidth; col++) {
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

	protected void calcHsiValues(){
		RGB2HSI rgb2HSI = new RGB2HSI();
		rgb2HSI.withImage(source).filter();
		float[][][] hsiValues = rgb2HSI.hsiValues;

		pixels = new RGBHSIPixel[source.getWidth()][source.getHeight()];
		for(int i = 0; i<source.getWidth(); ++i){
			for(int j = 0; j<source.getHeight(); ++j){
				pixels[i][j] = new RGBHSIPixel(hsiValues[i][j], source.getRGB(i, j));
			}
		}

	}

	@Override
	public String toString() {
		return name;
	}
}
