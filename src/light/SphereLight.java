package light;

import math.Ray;
import math.Sampler;
import math.Vector3f;
import prim.Intersection;
import prim.Sphere;

public class SphereLight implements Light
{
	private Vector3f m_position;
	private float m_power;
	private float m_radius;
	private Vector3f m_color;
	private Sphere m_geometry;

	public SphereLight(Vector3f position, float power, float radius, Vector3f color)
	{
		m_position = position;
		m_power = power;
		m_radius = radius;
		m_color = color;
		m_geometry = new Sphere(m_position, m_radius, null);
	}

	public Vector3f getSample(Vector3f position)
	{
		Vector3f normal = m_position.getSub(position).normalize();
		return Sampler.sampleSphere(normal, m_radius, 0f, (float) Math.PI / 2f).add(m_position);
	}

	public Intersection intersect(Ray ray)
	{
		return m_geometry.intersect(ray);
	}

	public Vector3f getColor()
	{
		return m_color;
	}

	public float getPower()
	{
		return m_power;
	}
}