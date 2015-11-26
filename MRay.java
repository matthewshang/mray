public class MRay
{
	private Display m_display;

	private MRay()
	{
		m_display = new Display(480, 360);
		start();
	}

	private void start()
	{
		int[] pixelBuffer = m_display.getPixels();
		for (int i = 0; i < 240; i++)
		{
			pixelBuffer[i + 480 * 100] = 0x000000;
		}
		m_display.drawBuffer();
	}

	public static void main(String[] args)
	{
		MRay ray = new MRay();
	}
}