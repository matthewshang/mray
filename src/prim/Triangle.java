package prim;

import math.AABB;
import math.Ray;
import math.Vector3f;

public class Triangle implements EngineObject
{
	private AABB m_bounds;
	private Vector3f m_normal;
	private Vector3f m_v0;
	private Vector3f m_v10;
	private Vector3f m_v20;

	public Triangle(Vector3f v0, Vector3f v1, Vector3f v2)
	{
		m_v0 = v0;
		m_v10 = v1.getSub(v0);
		m_v20 = v2.getSub(v0);
		m_normal = m_v10.cross(m_v20).normalize();

		Vector3f min = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		Vector3f max = new Vector3f(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);
		v0.sortMinAndMax(min, max);
		v1.sortMinAndMax(min, max);
		v2.sortMinAndMax(min, max);
		if (max.getX() == min.getX())
		{
			max.setX(max.getX() + 0.001f);
		}

		if (max.getY() == min.getY())
		{
			max.setY(max.getY() + 0.001f);
		}

		if (max.getZ() == min.getZ())
		{
			max.setZ(max.getZ() + 0.001f);
		}

		m_bounds = new AABB(min, max);
	}

	public AABB getBounds()
	{
		return m_bounds;
	}

	public Intersection intersect(Ray ray)
	{
		float d = m_normal.dot(m_v0.getSub(ray.getOrigin())) / m_normal.dot(ray.getDirection());
		if (d < 0)
		{
			return new Intersection();
		}

		Vector3f point = ray.getPoint(d);

		Vector3f p0 = point.getSub(m_v0);
		Vector3f ex = m_v10.getNormalized();
		Vector3f ey = ex.cross(m_normal);

		if (pointInTriangle(0f, 0f, m_v10.dot(ex), m_v10.dot(ey), m_v20.dot(ex), m_v20.dot(ey), p0.dot(ex), p0.dot(ey)))
		{
			return new Intersection(d, m_normal, null, -1);
		}
		else
		{
			return new Intersection();
		}
	}

	private static boolean pointInTriangle(float ax, float ay, float bx, float by, float cx, float cy, float px, float py)
	{
		float aABC = triangleAreaTimesTwo(ax, ay, bx, by, cx, cy);
		float aABP = triangleAreaTimesTwo(ax, ay, bx, by, px, py);
		float aBCP = triangleAreaTimesTwo(bx, by, cx, cy, px, py);
		float aCAP = triangleAreaTimesTwo(cx, cy, ax, ay, px, py);
		return aABP + aBCP + aCAP <= 1.0001 * aABC;
	}

	private static float triangleAreaTimesTwo(float ax, float ay, float bx, float by, float cx, float cy)
	{
		return Math.abs((ax * by) + (bx * cy) + (cx * ay) - (ay * bx) - (by * cx) - (cy * ax));
	}
}