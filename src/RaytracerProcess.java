public class RaytracerProcess extends Thread
{
	private int m_samples;
	private float m_heightRatio;
	private Scene m_scene;
	private Vector3f m_camera;
	private float m_pixelSize;
	private RenderChunk m_chunk;
	private boolean m_isRunning;
	private CubbyHole m_chunker;

	public void init(CubbyHole chunker, int samples, float heightRatio, Scene scene, Vector3f camera, float pixelSize)
	{
		m_samples = samples;
		m_heightRatio = heightRatio;
		m_scene = scene;
		m_camera = camera;
		m_pixelSize = pixelSize;
		m_isRunning = true;
		m_chunker = chunker;
	}

	public void stopRunning()
	{
		m_isRunning = false;
	}

	public void run()
	{
		while (m_isRunning)
		{
			m_chunk = m_chunker.get();

			for (int y = m_chunk.getStartY(); y < m_chunk.getEndY(); y++)
			{
				for (int x = m_chunk.getStartX(); x < m_chunk.getEndX(); x++)
				{
					Vector3f color = tracePixel(x, y);
					m_chunk.setAt(x, y, (int) color.getX() << 16 | (int) color.getY() << 8 | (int) color.getZ());
				}
			}
		}
	}

	private Vector3f tracePixel(int imagex, int imagey)
	{
		float halfTanFOV = (float) Math.tan(45.0f * (float) Math.PI / 180.0f);
		float left = -1.0f * halfTanFOV + m_pixelSize * halfTanFOV * imagex;
		float right = left + m_pixelSize * halfTanFOV;
		float top = m_heightRatio * halfTanFOV - m_pixelSize * halfTanFOV * imagey;
		float bottom = top - m_pixelSize * halfTanFOV;

		Vector3f color = new Vector3f(0.0f, 0.0f, 0.0f);
		for (int i = 0; i < m_samples; i++)
		{
			color.add(m_scene.traceRay(new Ray(m_camera, new Vector3f(randomFloat(left, right), randomFloat(bottom, top), 1.0f))));
		}

		return color.mul(1.0f / (float) m_samples);
	}

	private float randomFloat(float low, float high)
	{
		return low + (high - low) * (float) Math.random();
	}
}