public class RenderChunk
{
	private int[] m_pixels;
	private int m_startX;
	private int m_endX;
	private int m_startY;
	private int m_endY;

	public RenderChunk(int startX, int endX, int startY, int endY)
	{
		m_pixels = new int[(endX - startX) * (endY - startY)];
		m_startX = startX;
		m_endX = endX;
		m_startY = startY;
		m_endY = endY;

	}

	public void setAt(int x, int y, int value)
	{
		m_pixels[(m_endX - m_startX) * (y - m_startY) + (x - m_startX)] = value;
	}

	public void copyToBuffer(int[] buffer, int width)
	{
		for (int y = m_startY; y < m_endY; y++)
		{
			for (int x = m_startX; x < m_endX; x++)
			{
				int value = m_pixels[(y - m_startY) * (m_endX - m_startX) + (x - m_startX)];
				buffer[y * width + x] = value;
			}
		}
	}

	public int getStartX()
	{
		return m_startX;
	}

	public int getEndX()
	{
		return m_endX;
	}

	public int getStartY()
	{
		return m_startY;
	}

	public int getEndY()
	{
		return m_endY;
	}
}