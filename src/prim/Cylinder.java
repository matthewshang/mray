package prim;

import math.Intersection;
import math.Ray;
import math.Vector3f;

public class Cylinder implements EngineObject
{
	private Vector3f m_position;
	private Vector3f m_axis;
	private float m_radius;
	private Material m_material;

	public Cylinder(Vector3f position, Vector3f axis, float radius, Material material)
	{
		m_position = position;
		m_axis = axis.normalize();
		m_radius = radius;
		m_material = material;
	}

	public Intersection intersect(Ray ray)
	{
		Vector3f o = ray.getOrigin();
		Vector3f l = ray.getDirection();
		Vector3f deltaP = o.getSub(m_position);
		Vector3f lu = l.getSub(m_axis.getMul(l.dot(m_axis)));
		Vector3f pu = deltaP.getSub(m_axis.getMul(deltaP.dot(m_axis)));

		float a = lu.dot(lu);
		float b = 2 * lu.dot(pu);
		float c = pu.dot(pu) - m_radius * m_radius;

		float discrim = b * b - 4 * a * c;

		if (discrim < 0f)
		{
			return new Intersection();
		}
		
		discrim = (float) Math.sqrt(discrim);
		float invDenom = 1f / (2f * a);
		float dp = (-1f * b + discrim) * invDenom;
		float dm = (-1f * b - discrim) * invDenom;

		if (dp > 0f && dm > 0f)
		{
			if (dm < dp)
			{
				return new Intersection(dm, getNormal(ray, dm), m_material, -1);
			}
			else
			{
				return new Intersection(dp, getNormal(ray, dp), m_material, -1);
			}
		}
		else
		{
			if (dp < 0f && dm < 0f)
			{
				return new Intersection();
			}
			else if (dp < 0f)
			{
				return new Intersection(dm , getNormal(ray, dm), m_material, -1);
			}
			else if (dm < 0f)
			{
				return new Intersection(dp, getNormal(ray, dp), m_material, -1);
			}

			return new Intersection();
		}
	}

	private Vector3f getNormal(Ray ray, float d)
	{
		Vector3f intersection = ray.getPoint(d);
		// intersection.sub(m_position);
		return intersection.sub(m_axis.getMul(intersection.dot(m_axis)).add(m_position));
	}
}