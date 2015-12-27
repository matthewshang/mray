package prim;

import math.Vector3f;
import matl.Material;

public class Intersection
{
	private float m_distance;
	private Vector3f m_normal;
	private Material m_material;
	private int m_objectIndex;
	private boolean m_isIntersect;

	public Intersection(float distance, Vector3f normal, Material material, int index)
	{
		m_distance = distance;
		m_normal = normal.copy();
		m_isIntersect = true;
		m_material = material;
		m_objectIndex = index;
	}

	public Intersection()
	{
		m_isIntersect = false;
	}

	public void setIndex(int index)
	{
		m_objectIndex = index;
	}

	public boolean isIntersect()
	{
		return m_isIntersect;
	}

	public int getObjectIndex()
	{
		return m_objectIndex;
	}

	public float getDistance()
	{
		return m_distance;
	}

	public Vector3f getNormal()
	{
		return m_normal;
	}

	public Material getMaterial()
	{
		return m_material;
	}
}