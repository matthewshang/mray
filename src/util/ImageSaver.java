package util;

import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class ImageSaver
{
	public static void saveAsPNG(BufferedImage image, String name, String location)
	{
		File outputFile = new File(location + name + ".png");
		try
		{
			ImageIO.write(image, "png", outputFile);
		}
		catch (IOException ex)
		{
			System.out.println("ImageSaver: could not save image");
			ex.printStackTrace();
		}
	}
}