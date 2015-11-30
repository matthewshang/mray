public class RaytracerProcess extends Thread
{
	private int[] m_buffer;
	private int m_start;
	private int m_end;
	private int m_width;
	private int m_samples;
	private float m_heightRatio;
	private Scene m_scene;
	private Vector3f m_camera;
	private float m_pixelSize;

	public void init(int[] buffer, int start, int end, int width, int samples, 
					 float heightRatio, Scene scene, Vector3f camera, float pixelSize)
	{
		m_buffer = buffer;
		m_start = start;
		m_end = end;
		m_width = width;
		m_samples = samples;
		m_heightRatio = heightRatio;
		m_scene = scene;
		m_camera = camera;
		m_pixelSize = pixelSize;
	}

	public void run()
	{
		for (int y = m_start; y < m_end; y++)
		{
			for (int x = 0; x < m_width; x++)
			{
				Vector3f color = tracePixel(m_samples, m_heightRatio, m_scene, m_camera, x, y);
				m_buffer[y * m_width + x] = (int) color.getX() << 16 | (int) color.getY() << 8 | (int) color.getZ();
			}
		}
	}

	private Vector3f tracePixel(int samples, float heightRatio, Scene scene, Vector3f camera,
								int imagex, int imagey)
	{
		float halfTanFOV = (float) Math.tan(45.0f * (float) Math.PI / 180.0f);
		float left = -1.0f * halfTanFOV + m_pixelSize * halfTanFOV * imagex;
		float right = left + m_pixelSize * halfTanFOV;
		float top = heightRatio * halfTanFOV - m_pixelSize * halfTanFOV * imagey;
		float bottom = top - m_pixelSize * halfTanFOV;

		Vector3f color = new Vector3f(0.0f, 0.0f, 0.0f);
		for (int i = 0; i < samples; i++)
		{
			color.add(scene.traceRay(new Ray(camera, new Vector3f(randomFloat(left, right), randomFloat(bottom, top), 1.0f))));
		}

		return color.mul(1.0f / (float) samples);
	}

	private float randomFloat(float low, float high)
	{
		return low + (high - low) * (float) Math.random();
	}
}