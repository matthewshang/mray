public class MRay
{
	private Display m_display;
	private static int WIDTH = 960;
	private static int HEIGHT = 720;

	public MRay()
	{
		m_display = new Display(WIDTH, HEIGHT);
	}

	public void start()
	{		
		Scene scene = new Scene();
		scene.addObject(new Sphere(new Vector3f(0.0f, 0.0f, 5.0f), 1.0f, new Vector3f(255.0f, 255.0f, 255.0f)));
		scene.addObject(new Sphere(new Vector3f(2.0f, 0.0f, 5.0f), 1.0f, new Vector3f(255.0f, 255.0f, 255.0f)));
		scene.addObject(new Sphere(new Vector3f(-2.0f, 0.0f, 5.0f), 1.0f, new Vector3f(255.0f, 255.0f, 255.0f)));
		scene.addObject(new Sphere(new Vector3f(0.0f, 0.5f, 2.5f), 0.25f, new Vector3f(255.0f, 255.0f, 255.0f)));

		scene.addLight(new Light(new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(0.0f, 255.0f, 0.0f)));
		// scene.addLight(new Light(new Vector3f(-1.0f, 5.0f, 3.0f), new Vector3f(255.0f, 0.0f, 0.0f)));
		// scene.addLight(new Light(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 255.0f)));

		int[] pixelBuffer = m_display.getPixels();
		long start = System.nanoTime();
		traceImage(pixelBuffer, scene);
		long time = System.nanoTime() - start;
		System.out.println("Render time: " + (float) time / 1000000000.0f + " seconds"); 
		

		while (true)
		{
			m_display.drawBuffer();

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

	private void traceImage(int[] buffer, Scene scene)
	{
		Vector3f camera = new Vector3f(0.0f, 0.0f, 0.0f);
		float pixelSize = 2.0f / (float) WIDTH;
		float heightRatio = (float) HEIGHT / (float) WIDTH;

		for (int y = 0; y < HEIGHT; y++)
		{
			float ry = heightRatio - pixelSize / 2 - pixelSize * y; 

			for (int x = 0; x < WIDTH; x++)
			{
				float rx = -1.0f + pixelSize / 2 + pixelSize * x;

				Vector3f color = scene.traceRay(new Ray(camera, new Vector3f(rx, ry, 1.0f)));
				buffer[y * WIDTH + x] = (int) color.getX() << 16 | (int) color.getY() << 8 | (int) color.getZ();
			}
		}
	}

	public static void main(String[] args)
	{
		MRay mray = new MRay();
		mray.start();
	}
}