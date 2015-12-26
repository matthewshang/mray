package objects;

import math.Vector3f;

public class Light
{
	private Vector3f m_position;
	private Vector3f m_color;
	private float m_radius;

	public Light(Vector3f position, float radius, Vector3f color)
	{
		m_position = position;
		m_color = color;
		m_radius = radius;
	}

	public Vector3f getPosition()
	{
		return m_position;
	}

	public float getRadius()
	{
		return m_radius;
	}

	public Vector3f getColor()
	{
		return m_color;
	}

}