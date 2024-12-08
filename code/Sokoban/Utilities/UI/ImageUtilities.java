package Utilities.UI;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;

public class ImageUtilities
{
      public static Image getImage(String filename) throws Exception
      {
	 Image image = Toolkit.getDefaultToolkit().createImage(filename);

	 // This is probably a very bad thing to do, but who really cares in the end?
 	 MediaTracker tracker = new MediaTracker(new Canvas());
 	 tracker.addImage(image, 0);
 	 tracker.waitForAll();
	 
	 return image;
      }

      public static int[][] getPixels(Image image) throws Exception
      {
	 int[] pix = new int[(int) image.getWidth(null) * image.getHeight(null)];
	 PixelGrabber grabber = new PixelGrabber(image, 0, 0, (int) image.getWidth(null), (int) image.getHeight(null), pix, 0, (int) image.getWidth(null));
	 grabber.grabPixels();

	 int pixels[][] = new int[image.getWidth(null)][image.getHeight(null)];

	 int index = 0;
	 for(int y = 0; y < pixels[0].length; y++)
	 {
	    for(int x = 0; x < pixels.length; x++)
	       pixels[x][y] = pix[index++];
	 }

	 return pixels;
      }

      public static Image getScaledImage(Image image, double scaleX, double scaleY) throws Exception
      {
	 AffineTransform transform = new AffineTransform();
	 transform.scale(scaleX, scaleY);
	 return applyTransform(image, transform);
      }

      public static Image getShearedImage(Image image, double shiftransform, double shiftY) throws Exception
      {
	 AffineTransform transform = new AffineTransform();
	 transform.shear(shiftransform, shiftY);
	 return applyTransform(image, transform);
      }

      public static Image getTranslatedImage(Image image, double x, double y) throws Exception
      {
	 AffineTransform transform = new AffineTransform();
	 transform.translate(x, y);
	 return applyTransform(image, transform);
      }

      public static Image getRotatedImage(Image image, double degrees) throws Exception
      {
	 AffineTransform transform = new AffineTransform();
	 transform.rotate(Math.toRadians(degrees), image.getWidth(null)/2, image.getHeight(null)/2);
	 return applyTransform(image, transform);
      }

      private static Image applyTransform(Image image, AffineTransform transform) throws Exception
      {
	 BufferedImage bufferedImage = toBufferedImage(image);
	 AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

	 bufferedImage = op.filter(bufferedImage, null);
	 return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
      }

      public static BufferedImage toBufferedImage(Image image)
      {
	 BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	 Graphics2D g = bufferedImage.createGraphics();
	 g.drawImage(image, 0, 0, null);
	 return bufferedImage.getSubimage(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
      }
}
