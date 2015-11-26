import java.awt.*;
import java.awt.image.*;
import javax.swing.JFrame;

public class Display extends Canvas
{
	private JFrame m_window;
	private BufferedImage m_image;
	private int[] m_imageData;;

	public Display(int width, int height)
	{
		Dimension size = new Dimension(width, height);
		setMaximumSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
		setSize(size);

		m_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		m_imageData = ((DataBufferInt) m_image.getRaster().getDataBuffer()).getData();

		for (int i = 0; i < m_imageData.length; i++)
		{
			m_imageData[i] = 0xFFFFFF;
		}

		BufferStrategy b = getBufferStrategy();
		if (b == null)
		{
			createBufferStrategy(1);
		}

		createWindow("mray");
		m_window.setVisible(true);
	}

	private void createWindow(String title)
	{
		m_window = new JFrame(title);
		m_window.setSize(getWidth(), getHeight());
		m_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_window.setLocationRelativeTo(null);
		m_window.setResizable(false);
		m_window.add(this);
	}

	public int[] getPixels()
	{
		return m_imageData;
	}

	public void drawBuffer()
	{
		BufferStrategy b = getBufferStrategy();
		Graphics2D g = (Graphics2D) b.getDrawGraphics();
		g.drawImage(m_image, 0, 0, getWidth(), getHeight(), null);
		b.show();
		g.dispose();

	}
}