import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class MRay
{
	private Display m_display;
	private static int WIDTH = 960;
	private static int HEIGHT = 720;
	private static float HEIGHT_WIDTH_RATIO = (float) HEIGHT / (float) WIDTH;
	private static float PIXEL_SIZE = 2f / (float) WIDTH;

	public MRay()
	{
		m_display = new Display(WIDTH, HEIGHT);
	}

	public void start()
	{		
		Scene scene = scene_cylinder();
		// Scene scene = scene_ballAndPlane();
		// Scene scene = scene_ballCube();

		long start = System.nanoTime();
		traceImage(scene, m_display, 4, getCores());
		long time = System.nanoTime() - start;
		float seconds = (float) time / 1000000000f;
		System.out.println("Render time: " + seconds + " seconds");	

		m_display.drawBuffer();	

		if (false)
		{
			saveImage();
		}


		while (true)
		{
			try
			{
				Thread.sleep(32);
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	private void traceImage(Scene scene, Display display, int samplesPerPixel, int numberOfThreads)
	{
		int[] buffer = display.getPixels();
		Vector3f camera = new Vector3f(0f, 0f, 0f);

		RaytracerProcess[] threads = new RaytracerProcess[numberOfThreads];
		int threadHeight = HEIGHT / threads.length;
		int lastThread = threadHeight + (HEIGHT - threadHeight * threads.length);

		for (int i = 0; i < threads.length - 1; i++)
		{
			threads[i] = new RaytracerProcess();
			threads[i].init(buffer, threadHeight * i, threadHeight * (i + 1), WIDTH, samplesPerPixel, HEIGHT_WIDTH_RATIO, scene, camera, PIXEL_SIZE);
		}
		threads[threads.length - 1] = new RaytracerProcess();
		threads[threads.length - 1].init(buffer, HEIGHT - lastThread, HEIGHT, WIDTH, samplesPerPixel, HEIGHT_WIDTH_RATIO, scene, camera, PIXEL_SIZE);

		for (int i = 0; i < threads.length; i++)
		{
			threads[i].start();
		}

		for (int i = 0; i < threads.length; i++)
		{
			try
			{
				threads[i].join();
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
		}

	}

	private void saveImage()
	{
		File outputFile = new File("./out/" + getTimestamp() + ".png");
		try
		{
			ImageIO.write(m_display.getBufferedImage(), "png", outputFile);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

	}

	private Scene scene_cylinder()
	{
		Scene scene = new Scene();
		scene.addObject(new Cylinder(new Vector3f(0f, 0f, 5f), new Vector3f(0f, 1f, 0f), 1f, new Vector3f(255f, 255f, 255f)));
		scene.addObject(new Sphere(new Vector3f(1f, 0f, 3f), 0.5f, new Vector3f(255f, 255f, 255f)));
		scene.addObject(new Sphere(new Vector3f(-1f, 0f, 3f), 0.5f, new Vector3f(255f, 255f, 255f)));
		scene.addObject(new Sphere(new Vector3f(2f, 1f, 3f), 0.25f, new Vector3f(255f, 255f, 255f)));

		scene.addLight(new Light(new Vector3f(-5f, 0f, 0f), 5f, new Vector3f(255f, 0f, 0f)));
		scene.addLight(new Light(new Vector3f(5f, 0f, 0f), 5f, new Vector3f(0f, 255f, 0f)));
		return scene;
	}

	private Scene scene_ballAndPlane()
	{
		Scene scene = new Scene();
		scene.addObject(new Plane(new Vector3f(0f, -2f, 0f), new Vector3f(0f, 1f, 0f), new Vector3f(255f, 255f, 255f)));
		scene.addObject(new Sphere(new Vector3f(0f, 0f, 10f), 2f, new Vector3f(255f, 255f, 255f)));
		scene.addObject(new Sphere(new Vector3f(3f, -1f, 7f), 1f, new Vector3f(255f, 255f, 255f)));
		scene.addObject(new Sphere(new Vector3f(-3f, -1f, 7f), 1f, new Vector3f(255f, 255f, 255f)));

		scene.addLight(new Light(new Vector3f(-7f, 2f, 0f), 5f, new Vector3f(255f, 153f, 51f)));
		scene.addLight(new Light(new Vector3f(7f, 2f, 0f), 5f, new Vector3f(51f, 153f, 255f)));
		return scene;
	}

	private Scene scene_ballCube()
	{
		Scene scene = new Scene();
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				for (int k = 0; k < 4; k++)
				{
					scene.addObject(new Sphere(new Vector3f(-3f + i * 2f,
															-3f + j * 2f,
															 7f + k * 2f), 1f,
											   new Vector3f(255f, 255f, 255f)));
				}
			}
		}

		scene.addLight(new Light(new Vector3f(0f, 0f, 0f), 2f, new Vector3f(255f, 0f, 0f)));
		scene.addLight(new Light(new Vector3f(-8f, 0f, 8f), 5f, new Vector3f(0f, 255f, 0f)));
		scene.addLight(new Light(new Vector3f(8f, 0f, 8f), 5f, new Vector3f(0f, 0f, 255f)));
		return scene;
	}

	private int getCores()
	{
		return Runtime.getRuntime().availableProcessors();
	}

	private String getTimestamp()
	{
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");
		return dateFormat.format(date);
	}

	public static void main(String[] args)
	{
		MRay mray = new MRay();
		mray.start();
	}
}