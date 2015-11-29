public class MRay
{
	private Display m_display;
	private static int WIDTH = 960;
	private static int HEIGHT = 720;
	private static float PIXEL_SIZE = 2.0f / (float) WIDTH;

	public MRay()
	{
		m_display = new Display(WIDTH, HEIGHT);
	}

	public void start()
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
															 5.0f + k * 2.0f), 1.0f,
											   new Vector3f(255.0f, 255.0f, 255.0f)));
				}
			}
		}

		scene.addLight(new Light(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(255.0f, 0.0f, 0.0f)));
		scene.addLight(new Light(new Vector3f(-8.0f, 0.0f, 8.0f), new Vector3f(0.0f, 255.0f, 0.0f)));

		long start = System.nanoTime();
		traceImage(scene, m_display);
		long time = System.nanoTime() - start;
		float seconds = (float) time / 1000000000.0f;
		System.out.println("Render time: " + seconds + " seconds");		

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

	private void traceImage(Scene scene, Display display)
	{
		int[] buffer = display.getPixels();

		Vector3f camera = new Vector3f(0.0f, 0.0f, 0.0f);
		float heightRatio = (float) HEIGHT / (float) WIDTH;

		for (int y = 0; y < HEIGHT; y++)
		{
			for (int x = 0; x < WIDTH; x++)
			{
				Vector3f color = tracePixel(8, heightRatio, scene, camera, x, y);
				buffer[y * WIDTH + x] = (int) color.getX() << 16 | (int) color.getY() << 8 | (int) color.getZ();
			}

			if (y % 50 == 0)
			{
				display.drawBuffer();
			}
		}
	}

	private Vector3f tracePixel(int antialias, float heightRatio, Scene scene, Vector3f camera,
								int imagex, int imagey)
	{
		float halfTanFOV = (float) Math.tan(45.0f * (float) Math.PI / 180.0f);
		float left = -1.0f * halfTanFOV + PIXEL_SIZE * halfTanFOV * imagex;
		float right = left + PIXEL_SIZE * halfTanFOV;
		float top = heightRatio * halfTanFOV - PIXEL_SIZE * halfTanFOV * imagey;
		float bottom = top - PIXEL_SIZE * halfTanFOV;

		Vector3f color = new Vector3f(0.0f, 0.0f, 0.0f);
		for (int i = 0; i < antialias; i++)
		{
			color.add(scene.traceRay(new Ray(camera, new Vector3f(randomFloat(left, right), randomFloat(bottom, top), 1.0f))));
		}

		return color.mul(1.0f / (float) antialias);
	}

	private float randomFloat(float low, float high)
	{
		return low + (high - low) * (float) Math.random();
	}

	public static void main(String[] args)
	{
		MRay mray = new MRay();
		mray.start();
	}
}