import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import core.CubbyHole;
import core.MRayWorker;
import core.RenderChunk;
import core.Renderer;
import scn.Scene;
import scn.TestScene;
import util.ImageSaver;

public class MRay
{
	private final static int WIDTH = 1920;
	private final static int HEIGHT = 1080;
	// private final static int WIDTH = 960;
	// private final static int HEIGHT = 960;
	// private final static int WIDTH = 480;
	// private final static int HEIGHT = 270;

	private final static int CHUNK_WIDTH = 60;
	private final static int CHUNK_HEIGHT = 60;
	private final static int SAMPLES = 10;
	private final static int MAX_DEPTH = 8;

	public static void main(String[] args)
	{
		MRay mray = new MRay();
		mray.start();
	}

	public void start()
	{		
		// Scene scene = TestScene.head();
		Scene scene = TestScene.dragon();
		// Scene scene = TestScene.bunny();
		// Scene scene = TestScene.cube();
		// Scene scene = TestScene.box();

		Renderer renderer = new Renderer(WIDTH, HEIGHT, SAMPLES, MAX_DEPTH, scene);

		long start = System.nanoTime();
		BufferedImage image = traceImage(renderer, getCores() - 1);
		long time = System.nanoTime() - start;
		float seconds = (float) time / 1000000000f;
		System.out.println("Render time: " + seconds + " seconds");	

		ImageSaver.saveAsPNG(image, SAMPLES + "spp " + MAX_DEPTH + "depth " + seconds + "secs", "./out/");
	}

	private BufferedImage traceImage(Renderer renderer, int numberOfThreads)
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

		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		int[] buffer = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

		RenderChunk[] chunks = renderer.getImageChunks(CHUNK_WIDTH, CHUNK_HEIGHT);

		int currentChunk = 0;
		int lastProgress = -1;
		while (currentChunk < chunks.length)
		{
			chunker.put(chunks[currentChunk++]);
			int progress = (int) (((float) currentChunk / (float) chunks.length) * 100f);
			if (progress % 10 == 0 && lastProgress != progress)
			{
				System.out.println("MRay: render " + progress + "% complete");
				lastProgress = progress;
			}
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

		return image;
	}

	private int getCores()
	{
		return Runtime.getRuntime().availableProcessors();
	}}