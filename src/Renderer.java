public class Renderer
{
	private int m_width;
	private int m_height;
	private int m_samples;
	private float m_heightWidthRatio;
	private float m_pixelSize;
	private float m_imageLeft;
	private float m_imageTop;
	private float m_halfTanFOV = (float) Math.tan(45f * (float) Math.PI / 180f);

	private Scene m_scene;

	public Renderer(int width, int height, int samples, Scene scene)
	{
		m_width = width;
		m_height = height;
		m_samples = samples;

		m_heightWidthRatio = (float) m_height / (float) m_width;
		m_pixelSize = m_halfTanFOV * 2f / (float) m_width;
		m_scene = scene;
		m_imageLeft = -1f * m_halfTanFOV;
		m_imageTop = m_heightWidthRatio * m_halfTanFOV;
	}

	public RenderChunk[] getImageChunks(int chunkWidth, int chunkHeight)
	{
		int columns = m_width / chunkWidth;
		int rows = m_height / chunkHeight;
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

	public void traceChunk(RenderChunk chunk)
	{
		for (int y = chunk.getStartY(); y < chunk.getEndY(); y++)
		{
			for (int x = chunk.getStartX(); x < chunk.getEndX(); x++)
			{
				Vector3f color = tracePixel(x, y);
				chunk.setAt(x, y, (int) color.getX() << 16 | (int) color.getY() << 8 | (int) color.getZ());
			}
		}
	}

	private Vector3f tracePixel(int imageX, int imageY)
	{
		float left = m_imageLeft + m_pixelSize * imageX;
		float right = left + m_pixelSize;
		float top = m_imageTop - m_pixelSize * imageY;
		float bottom = top - m_pixelSize;

		Vector3f color = new Vector3f(0f, 0f, 0f);
		for (int i = 0; i < m_samples; i++)
		{
			color.add(m_scene.traceRay(new Ray(new Vector3f(0f, 0f, 0f), new Vector3f(randomFloat(left, right), randomFloat(bottom, top), 1f)), 0));
		}

		return color.mul(1f / (float) m_samples); 
	}

	private float randomFloat(float low, float high)
	{
		return low + (high - low) * (float) Math.random();
	}
}