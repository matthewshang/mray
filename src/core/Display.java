package core;

import java.awt.*;
import java.awt.image.*;
import javax.swing.JFrame;

public class Display extends Canvas
{
	private JFrame m_window;
	private BufferedImage m_image;
	private int[] m_imageData;
	private int m_width;
	private int m_height;

	public Display(int width, int height)
	{
		m_width = width;
		m_height = height;
		Dimension size = new Dimension(m_width, m_height);
		setMaximumSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
		setSize(size);

		m_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		m_imageData = ((DataBufferInt) m_image.getRaster().getDataBuffer()).getData();

		createWindow("mray");
		m_window.setVisible(true);
		drawBuffer();
	}

	public int getWidth()
	{
		return m_width;
	}

	public int getHeight()
	{
		return m_height;
	}

	public int[] getPixels()
	{
		return m_imageData;
	}

	public BufferedImage getBufferedImage()
	{
		return m_image;
	}

	public void setTitle(String title)
	{
		m_window.setTitle(title);
	}

	public void drawBuffer()
	{
		BufferStrategy b = getBufferStrategy();
		if (b == null)
		{
			createBufferStrategy(1);
			return;
		}



		Graphics2D g = (Graphics2D) b.getDrawGraphics();
		g.drawImage(m_image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		b.show();

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
}