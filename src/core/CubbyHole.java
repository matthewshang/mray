package core;

public class CubbyHole
{
	private RenderChunk m_chunk;
	private boolean m_available = false;

	public synchronized RenderChunk get()
	{
		while (m_available == false)
		{
			try
			{
				wait();
			}
			catch (InterruptedException ex)
			{

			}
		}

		m_available = false;
		notifyAll();
		return m_chunk;
	}

	public synchronized void put(RenderChunk chunk)
	{
		while (m_available == true)
		{
			try
			{
				wait();
			}
			catch (InterruptedException ex)
			{

			}
		}

		m_chunk = chunk;
		m_available = true;
		notifyAll();
	}
}