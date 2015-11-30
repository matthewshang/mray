public class MRay
{
	private Display m_display;
	private static int WIDTH = 960;
	private static int HEIGHT = 720;
	private static float HEIGHT_WIDTH_RATIO = (float) HEIGHT / (float) WIDTH;
	private static float PIXEL_SIZE = 2.0f / (float) WIDTH;

	public MRay()
	{
		m_display = new Display(WIDTH, HEIGHT);
	}

	public void start()
	{		
		// Scene scene = scene_ballAndPlane();
		Scene scene = scene_ballCube();

		long start = System.nanoTime();
		traceImage(scene, m_display, 8, getCores());
		long time = System.nanoTime() - start;
		float seconds = (float) time / 1000000000.0f;
		System.out.println("Render time: " + seconds + " seconds");	

		m_display.drawBuffer();	

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
		Vector3f camera = new Vector3f(0.0f, 0.0f, 0.0f);

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

	private Scene scene_ballAndPlane()
	{
		Scene scene = new Scene();
		scene.addObject(new Plane(new Vector3f(0.0f, -2.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(255.0f, 255.0f, 255.0f)));
		scene.addObject(new Sphere(new Vector3f(0.0f, 0.0f, 10.0f), 2.0f, new Vector3f(255.0f, 255.0f, 255.0f)));
		scene.addObject(new Sphere(new Vector3f(3.0f, -1.0f, 7.0f), 1.0f, new Vector3f(255.0f, 255.0f, 255.0f)));
		scene.addObject(new Sphere(new Vector3f(-3.0f, -1.0f, 7.0f), 1.0f, new Vector3f(255.0f, 255.0f, 255.0f)));

		scene.addLight(new Light(new Vector3f(-7.0f, 2.0f, 0.0f), 5.0f, new Vector3f(255.0f, 153.0f, 51.0f)));
		scene.addLight(new Light(new Vector3f(7.0f, 2.0f, 0.0f), 5.0f, new Vector3f(51.0f, 153.0f, 255.0f)));
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
					scene.addObject(new Sphere(new Vector3f(-3.0f + i * 2.0f,
															-3.0f + j * 2.0f,
															 7.0f + k * 2.0f), 1.0f,
											   new Vector3f(255.0f, 255.0f, 255.0f)));
				}
			}
		}

		scene.addLight(new Light(new Vector3f(0.0f, 0.0f, 0.0f), 2.0f, new Vector3f(255.0f, 0.0f, 0.0f)));
		scene.addLight(new Light(new Vector3f(-8.0f, 0.0f, 8.0f), 5.0f, new Vector3f(0.0f, 255.0f, 0.0f)));
		scene.addLight(new Light(new Vector3f(8.0f, 0.0f, 8.0f), 5.0f, new Vector3f(0.0f, 0.0f, 255.0f)));
		return scene;
	}

	private int getCores()
	{
		return Runtime.getRuntime().availableProcessors();
	}

	public static void main(String[] args)
	{
		MRay mray = new MRay();
		mray.start();
	}
}