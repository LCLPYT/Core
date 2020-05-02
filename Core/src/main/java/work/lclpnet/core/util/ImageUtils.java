package work.lclpnet.core.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ImageUtils {

	public enum ScalingType {
		NONE,
		CONTAINED,
		COVERED,
		STRETCHED;

		public BufferedImage resize(BufferedImage source, int destinationW, int destinationH) {
			switch (this) {
			case CONTAINED: return ImageUtils.resize(source, destinationW, destinationH, false);
			case COVERED: return ImageUtils.resize(source, destinationW, destinationH, true);
			case STRETCHED: return resizeStretched(source, destinationW, destinationH);
			default: return source;
			}
		}
		
	}

	private static BufferedImage resize(BufferedImage source, int destinationW, int destinationH, boolean covered) {
		float ratioW = (float) destinationW / (float) source.getWidth();
		float ratioH = (float) destinationH / (float) source.getHeight();
		int finalW, finalH;
		int x, y;

		if(covered ? ratioW > ratioH : ratioW < ratioH) {
			finalW = destinationW;
			finalH = (int) (source.getHeight() * ratioW);
		}
		else {
			finalW = (int) (source.getWidth() * ratioH);
			finalH = destinationH;
		}

		x = (destinationW - finalW) / 2;
		y = (destinationH - finalH) / 2;

		return drawImage(source, destinationW, destinationH, x, y, finalW, finalH);
	}

	 private static BufferedImage resizeStretched(BufferedImage source, int destinationW, int destinationH) {
		return drawImage(source, destinationW, destinationH, 0, 0, destinationW, destinationH);
	 }

	 public static BufferedImage drawImage(BufferedImage source, int bufferW, int bufferH, int posX, int posY, int sourceW, int sourceH) {
		 BufferedImage newImage = new BufferedImage(bufferW, bufferH, BufferedImage.TYPE_INT_ARGB);

		 Graphics graphics = newImage.getGraphics();
		 graphics.drawImage(source, posX, posY, sourceW, sourceH, null);
		 graphics.dispose();
		 return newImage;
	 }
}
