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
	private static int CHUNK_WIDTH = 96;
	private static int CHUNK_HEIGHT = 72;
	private static float HEIGHT_WIDTH_RATIO = (float) HEIGHT / (float) WIDTH;
	private static float PIXEL_SIZE = 2f / (float) WIDTH;

	public MRay()
	{
		m_display = new Display(WIDTH, HEIGHT);
	}

	public void start()
	{		
		// Scene scene = scene_box();
		// Scene scene = scene_cylinder();
		Scene scene = scene_ballAndPlane();
		// Scene scene = scene_ballCube();

		long start = System.nanoTime();
		traceImage(scene, m_display, 8, getCores() - 1);
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
		Vector3f camera = new Vector3f(0f, 0f, 0f);

		RaytracerProcess[] threads = new RaytracerProcess[numberOfThreads];
		CubbyHole chunker = new CubbyHole();

		for (int i = 0; i < threads.length; i++)
		{
			threads[i] = new RaytracerProcess();
			threads[i].init(chunker, samplesPerPixel, HEIGHT_WIDTH_RATIO, scene, camera, PIXEL_SIZE);
		}

		RenderChunk[] chunks = chunkImage(WIDTH, HEIGHT, CHUNK_WIDTH, CHUNK_HEIGHT);

		for (int i = 0; i < threads.length; i++)
		{
			threads[i].start();
		}

		int currentChunk = 0;

		while (currentChunk < chunks.length)
		{
			chunker.put(chunks[currentChunk++]);
		}

		// To fix weird bug of last chunk not having time to finish
        chunker.put(chunks[chunks.length - 1]);
		
		for (int i = 0; i < threads.length; i++)
		{
			threads[i].stopRunning();
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

		int[] buffer = display.getPixels();
		for (int i = 0; i < chunks.length; i++)
		{
			chunks[i].copyToBuffer(buffer, WIDTH);
		}
	}

	private RenderChunk[] chunkImage(int imageWidth, int imageHeight, int chunkWidth, int chunkHeight)
	{
		int columns = imageWidth / chunkWidth;
		int rows = imageHeight / chunkHeight;
		RenderChunk[] chunks = new RenderChunk[columns * rows];
		for (int row = 0; row < rows; row++)
		{
			for (int column = 0; column < columns; column++)
			{
				chunks[row * columns + column] = new RenderChunk(chunkWidth * column, chunkWidth * (column + 1),
																 chunkHeight * row, chunkHeight * (row + 1));
				chunks[row * columns + column].id = row * columns + column;
			}
		}

		return chunks;
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

	// private Scene scene_box()
	// {
	// 	Scene scene = new Scene();
	// 	scene.addObject(new Plane(new Vector3f(4f, 0f, 0f), new Vector3f(-1f, 0f, 0f), new Vector3f(255f, 0f, 0f)));

	// 	scene.addObject(new Plane(new Vector3f(-4f, 0f, 0f), new Vector3f(1f, 0f, 0f), new Vector3f(0f, 0f, 255f)));
	// 	scene.addObject(new Plane(new Vector3f(0f, 4f, 0f), new Vector3f(0f, -1f, 0f), new Vector3f(255f, 255f, 255f)));
	// 	scene.addObject(new Plane(new Vector3f(0f, -4f, 0f), new Vector3f(0f, 1f, 0f), new Vector3f(255f, 255f, 255f)));
	// 	scene.addObject(new Plane(new Vector3f(0f, 0f, 10f), new Vector3f(0f, 0f, -1f), new Vector3f(255f, 255f, 255f)));

	// 	scene.addObject(new Sphere(new Vector3f(0f, 0f, 7f), 1f, new Vector3f(255f, 255f, 255f)));

	// 	scene.addLight(new Light(new Vector3f(-3f, 3.9f, 5f), 7f, new Vector3f(0f, 0f, 255f)));
	// 	scene.addLight(new Light(new Vector3f(3f, 3.9f, 5f), 7f, new Vector3f(255f, 0f, 0f)));
	// 	scene.addLight(new Light(new Vector3f(0f, 0f, 0f), 3f, new Vector3f(255f, 255f, 255f)));
	// 	scene.addLight(new Light(new Vector3f(-3f, 0f, 6f), 7f, new Vector3f(255f, 255f, 255f)));

	// 	return scene;

	// }

	private Scene scene_box()
	{
		Scene scene = new Scene();
		
		scene.addObject(new Sphere(new Vector3f(0f, 0f, 5f), 1f, new Vector3f(255f, 255f, 255f)));
		scene.addLight(new Light(new Vector3f(0f, 0f, 10f), 4f, new Vector3f(255f, 0f, 0f)));
		return scene;

	}

	private Scene scene_cylinder()
	{
		Scene scene = new Scene();
		scene.addObject(new Cylinder(new Vector3f(0f, 0f, 5f), new Vector3f(0f, 1f, 0f), 1f, new Vector3f(255f, 255f, 255f)));
		scene.addObject(new Sphere(new Vector3f(1f, 0f, 3f), 0.5f, new Vector3f(255f, 255f, 255f)));
		scene.addObject(new Sphere(new Vector3f(-1f, 0f, 3f), 0.5f, new Vector3f(255f, 255f, 255f)));
		scene.addObject(new Sphere(new Vector3f(2f, 1f, 3f), 0.25f, new Vector3f(255f, 255f, 255f)));
		scene.addObject(new Plane(new Vector3f(0f, 0f, 10f), new Vector3f(0f, 0f, -1f), new Vector3f(255f, 255f, 255f)));

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

		scene.addLight(new Light(new Vector3f(-7f, 3f, 0f), 5f, new Vector3f(255f, 153f, 51f)));
		scene.addLight(new Light(new Vector3f(7f, 3f, 0f), 5f, new Vector3f(51f, 153f, 255f)));
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