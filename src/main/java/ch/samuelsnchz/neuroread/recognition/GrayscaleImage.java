package ch.samuelsnchz.neuroread.recognition;

import java.util.Objects;

final public class GrayscaleImage {
	private final static int GS_IMAGE_SIZE = 28;

	private final static double BLACK = 0.0;
	private final static double WHITE = 1.0;

	private final int width;
	private final int height;
	//private final double[] pixels;

	private final double[] grayscaleImage;

	private GrayscaleImage(int width, int height, double[] grayscaleImage) {
		if (width <= 0) {
			throw new IllegalArgumentException("Image's width must be > 0, got " + width);
		}
		if (height <= 0) {
			throw new IllegalArgumentException("Image's width must be > 0, got " + height);
		}
		if (grayscaleImage.length != width * height){
			throw new IllegalArgumentException("Image's number of pixel do not match width and height.");
		}

		this.width = width;
		this.height = height;
		this.grayscaleImage = Objects.requireNonNull(grayscaleImage.clone(), "Image's pixel must not be null.");
	}

	public int getWidth() { return width; }
	public int getHeight() { return height; }

	public double getPixel(int posX, int posY) {
		if (posX >= width || posY >= height){
			throw new IllegalArgumentException("Pixel's index out of range.");
		}
		return grayscaleImage[posY * width + posX];
	}

	public static final class Builder {
		private final int width;
		private final int height;
		private final double[] grayscaleImage;

		public Builder(int width, int height) {
			this.width = width;
			this.height = height;
			this.grayscaleImage = new double[GS_IMAGE_SIZE * GS_IMAGE_SIZE];
		}

		public Builder setPixel(int posX, int posY, double value) {
			if (posX >= width || posY >= height){
				throw new IllegalArgumentException("Pixel's index out of range.");
			}
			grayscaleImage[posX + posY * width] = value;
			return this;
		}

		public GrayscaleImage build() {
			return new GrayscaleImage(width, height, grayscaleImage);
		}
	}

}
