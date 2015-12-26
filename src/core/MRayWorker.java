package core;

public class MRayWorker extends Thread
{
	private boolean m_isRunning;
	private CubbyHole m_chunker;
	private Renderer m_renderer;

	public void init(CubbyHole chunker, Renderer renderer)
	{
		m_isRunning = true;
		m_chunker = chunker;
		m_renderer = renderer;
	}

	public void stopRunning()
	{
		m_isRunning = false;
	}

	public void run()
	{
		while (m_isRunning)
		{
			RenderChunk chunk = m_chunker.get();
			m_renderer.traceChunk(chunk);
		}
	}
}