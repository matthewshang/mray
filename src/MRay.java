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
import scn.Scene;
import scn.TestScene;
import math.*;

public class MRay
{
	// private final static int WIDTH = 1920;
	// private final static int HEIGHT = 1080;
	private final static int WIDTH = 960;
	private final static int HEIGHT = 960;
	// private final static int WIDTH = 480;
	// private final static int HEIGHT = 270;

	private final static int CHUNK_WIDTH = 80;
	private final static int CHUNK_HEIGHT = 80;
	private final static int SAMPLES = 128;
	private final static int MAX_DEPTH = 3;

	private Display m_display;

	public static void main(String[] args)
	{
		MRay mray = new MRay();
		mray.start();
	}

	public MRay()
	{

		m_display = new Display(WIDTH, HEIGHT);
	}

	public void start()
	{		
		// Scene scene = TestScene.glossyBalls();
		// Scene scene = TestScene.ballAndPlane();
		Scene scene = TestScene.diffuseBallAndPlane();
		// Scene scene = TestScene.coloredBalls();
		// Scene scene = TestScene.cube();
		// Scene scene = TestScene.box();

		Renderer renderer = new Renderer(m_display.getWidth(), m_display.getHeight(), SAMPLES, MAX_DEPTH, scene);

		long start = System.nanoTime();
		traceImage(m_display, renderer, getCores() - 1);
		long time = System.nanoTime() - start;
		float seconds = (float) time / 1000000000f;
		System.out.println("Render time: " + seconds + " seconds");	

		m_display.drawBuffer();	

		if (true)
		{
			saveImage();
		}
	}

	private void traceImage(Display display, Renderer renderer, int numberOfThreads)
	{
		CubbyHole chunker = new CubbyHole();
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
	    int[] buffer = display.getPixels();

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
}