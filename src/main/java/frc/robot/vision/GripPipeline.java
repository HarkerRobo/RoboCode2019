package frc.robot.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

/**
* GripPipeline class.
*
* <p>An OpenCV pipeline generated by GRIP.
*
* @author GRIP
*/
public class GripPipeline {

	//Outputs
	private Mat blurOutput = new Mat();

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 * This is the primary method that runs the entire pipeline and updates the outputs.
	 */
	public void process() {
		BlurType blurType = BlurType.get("Box Blur");
        double blurRadius = 20.0;

        Mat currentFrame = new Mat();
        VideoCapture data = new VideoCapture("http://10.10.72.11:5802");
        data.read(currentFrame);

		blur(currentFrame, blurType, blurRadius, blurOutput);
	}

	/**
	 * This method is a generated getter for the output of a Blur.
	 * @return Mat output from Blur.
	 */
	public Mat blurOutput() {
		return blurOutput;
	}


	/**
	 * An indication of which type of filter to use for a blur.
	 * Choices are BOX, GAUSSIAN, MEDIAN, and BILATERAL
	 */
	enum BlurType{
		BOX("Box Blur"), GAUSSIAN("Gaussian Blur"), MEDIAN("Median Filter"),
			BILATERAL("Bilateral Filter");

		private final String label;

		BlurType(String label) {
			this.label = label;
		}

		public static BlurType get(String type) {
			if (BILATERAL.label.equals(type)) {
				return BILATERAL;
			}
			else if (GAUSSIAN.label.equals(type)) {
			return GAUSSIAN;
			}
			else if (MEDIAN.label.equals(type)) {
				return MEDIAN;
			}
			else {
				return BOX;
			}
		}

		@Override
		public String toString() {
			return this.label;
		}
	}

	/**
	 * Softens an image using one of several filters.
	 * @param input The image on which to perform the blur.
	 * @param type The blurType to perform.
	 * @param doubleRadius The radius for the blur.
	 * @param output The image in which to store the output.
	 */
	private void blur(Mat input, BlurType type, double doubleRadius,
		Mat output) {
		int radius = (int)(doubleRadius + 0.5);
		int kernelSize;
		switch(type){
			case BOX:
				kernelSize = 2 * radius + 1;
				Imgproc.blur(input, output, new Size(kernelSize, kernelSize));
				break;
			case GAUSSIAN:
				kernelSize = 6 * radius + 1;
				Imgproc.GaussianBlur(input,output, new Size(kernelSize, kernelSize), radius);
				break;
			case MEDIAN:
				kernelSize = 2 * radius + 1;
				Imgproc.medianBlur(input, output, kernelSize);
				break;
			case BILATERAL:
				Imgproc.bilateralFilter(input, output, -1, radius, radius);
				break;
		}
	}




}

