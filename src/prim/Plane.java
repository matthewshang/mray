package prim;

import math.Ray;
import math.Vector3f;

public class Plane implements EngineObject
{
	private Vector3f m_point;
	private Vector3f m_normal;
	private Material m_material;

	public Plane(Vector3f point, Vector3f normal, Material material)
	{
		m_point = point;
		m_normal = normal.copy().normalize();
		m_material = material;
	}

	public Intersection intersect(Ray ray)
	{
		float d = m_normal.dot(m_point.getSub(ray.getOrigin())) / m_normal.dot(ray.getDirection());
		if (d < 0.0f)
		{
			return new Intersection();
		}
		else
		{
			return new Intersection(d, m_normal, m_material, -1);
		}
	}
}