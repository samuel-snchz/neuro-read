package ch.samuelsnchz.neuroread.recognition;

import java.util.List;

public interface CharacterRecognizer {
	int INPUT_SIZE = 28;
	int TOTAL_INPUT_SIZE = INPUT_SIZE * INPUT_SIZE;

	record Prediction(char character, double confidence) {
		public Prediction {
			if (!(confidence >= 0 && confidence <= 1)) {
				throw new IllegalArgumentException(
						"Confidence is out of range. Must be in [0, 1], got " + confidence);
			}
		}
	}

	/**
	 * Recognize the character drawn in the given image.
	 *
	 * @param image a grayscale image of size INPUT_SIZE x INPUT_SIZE
	 * @return the candidate characters sorted by decreasing confidence
	 * 		   as a non-empty unmodifiable list
	 * @throws IllegalArgumentException if the image does not have the expected size
	 */
	List<Prediction> recognize(GrayscaleImage image);
}
