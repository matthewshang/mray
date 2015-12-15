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

	public MRay()
	{
		m_display = new Display(WIDTH, HEIGHT);
	}

	public void start()
	{		
		// Scene scene = TestScene.box();
		Scene scene = TestScene.ballAndPlane();
		// Scene scene = TestScene.ballCube();

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

		CubbyHole chunker = new CubbyHole();
		Renderer renderer = new Renderer(WIDTH, HEIGHT, samplesPerPixel, scene);
		MRayWorker[] workers = new MRayWorker[numberOfThreads];

		for (int i = 0; i < workers.length; i++)
		{
			workers[i] = new MRayWorker();
			workers[i].init(chunker, renderer);
		}

		for (int i = 0; i < workers.length; i++)
		{
			workers[i].start();
		}

		RenderChunk[] chunks = chunkImage(WIDTH, HEIGHT, CHUNK_WIDTH, CHUNK_HEIGHT);
		int currentChunk = 0;

		while (currentChunk < chunks.length)
		{
			chunker.put(chunks[currentChunk++]);
		}

		// To fix weird bug of last chunk not having time to finish
        chunker.put(chunks[chunks.length - 1]);

        for (int i = 0; i < workers.length; i++)
        {
        	workers[i].stopRunning();
        }

		for (int i = 0; i < workers.length; i++)
		{
			try
			{
				workers[i].join();				
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