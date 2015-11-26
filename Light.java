public class Light
{
	private Vector3f m_position;
	private Vector3f m_color;

	public Light(Vector3f position, Vector3f color)
	{
		m_position = position;
		m_color = color;
	}

	public Vector3f getPosition()
	{
		return m_position;
	}

	public Vector3f getColor()
	{
		return m_color;
	}

}