package prim;

import math.Vector3f;

public class Triangle
{
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
	}

	public Vector3f getNormal()
	{
		return m_normal;
	}

	public Vector3f getV0()
	{
		return m_v0;
	}

	public Vector3f getV10()
	{
		return m_v10;
	}

	public Vector3f getV20()
	{
		return m_v20;
	}
}