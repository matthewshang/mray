package light;

import math.Ray;
import math.Vector3f;
import prim.Intersection;

public class PointLight implements Light
{
	private Vector3f m_position;
	private Vector3f m_color;
	private float m_power;

	public PointLight(Vector3f position, float power, Vector3f color)
	{
		m_position = position;
		m_color = color;
		m_power = power;
	}

	public Vector3f getSample(Vector3f position)
	{
		return m_position;
	}

	public Intersection intersect(Ray ray)
	{
		return new Intersection();
	}

	public float getPower()
	{
		return m_power;
	}

	public Vector3f getColor()
	{
		return m_color;
	}
}