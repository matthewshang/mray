public class MRay
{
	private Display m_display;

	public MRay()
	{
		m_display = new Display(480, 360);
	}

	public void start()
	{
		int[] pixelBuffer = m_display.getPixels();
		Vector3f camera = new Vector3f(0.0f, 0.0f, 0.0f);
		float pixelSize = 2.0f / 480.0f;

		for (int x = 0; x < 480; x++)
		{
			for (int y = 0; y < 360; y++)
			{
				float rx = -1.0f + pixelSize / 2 + pixelSize * x;
				float ry = 1.0f - pixelSize / 2 + pixelSize * y; 
				Vector3f color = traceRay(new Ray(camera, new Vector3f(rx, ry, 1.0f)));
				pixelBuffer[y * 480 + x] = (int) color.getX() << 16 | (int) color.getY() << 8 | (int) color.getZ();
			}
		}

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

	private Vector3f traceRay(Ray ray)
	{
		if (raySphereIntersect(ray, new Vector3f(0.0f, 0.0f, 5.0f), 4.0f) != -1)
		{
			return new Vector3f(255.0f, 0.0f, 0.0f);
		}
		else
		{
			return new Vector3f(153.0f, 204.0f, 255.0f);
		}
	}

	private float raySphereIntersect(Ray ray, Vector3f sphereCenter, float sphereRadius)
	{
		Vector3f l = ray.getDirection();
		Vector3f o = ray.getOrigin();
		float a = l.dot(o.getSub(sphereCenter));
		Vector3f b = sphereCenter.getSub(o);
		float discrim = a * a - (b.dot(b) - sphereRadius * sphereRadius);

		if (discrim < 0)
		{
			return -1;
		}

		float out = -1 * l.dot(o.getSub(sphereCenter));
		if (discrim == 0)
		{
			return out;
		}
		discrim = (float) Math.sqrt(discrim);
		float dp = out + discrim;
		float dm = out - discrim;

		if (dp > dm)
		{
			return dm;
		}
		else
		{
			return dp;
		}
	}

	public static void main(String[] args)
	{
		MRay mray = new MRay();
		mray.start();
	}
}