import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

import core.Display;
import core.CubbyHole;
import core.MRayWorker;
import core.RenderChunk;
import core.Renderer;
import objects.Scene;
import objects.TestScene;

public class MRay
{
	private final static int WIDTH = 960;
	private final static int HEIGHT = 720;
	private final static int CHUNK_WIDTH = 96;
	private final static int CHUNK_HEIGHT = 72;
	private final static int SAMPLES_PER_PIXEL = 32;
	private final static int MAX_DEPTH = 5;

	private Display m_display;

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
		traceImage(scene, m_display, SAMPLES_PER_PIXEL, MAX_DEPTH, getCores() - 1);
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

	private void traceImage(Scene scene, Display display, int samplesPerPixel, int maxDepth, int numberOfThreads)
	{
		CubbyHole chunker = new CubbyHole();
		Renderer renderer = new Renderer(display.getWidth(), display.getHeight(), samplesPerPixel, maxDepth, scene);
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

		RenderChunk[] chunks = renderer.getImageChunks(CHUNK_WIDTH, CHUNK_HEIGHT);
		int currentChunk = 0;

		while (currentChunk < chunks.length)
		{
			chunker.put(chunks[currentChunk++]);
			display.setTitle(currentChunk + "/" + chunks.length);
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

		display.setTitle("mray");
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