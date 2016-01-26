package prim;

import java.util.ArrayList;

import math.AABB;
import math.Vector3f;

public class GeometryData
{
	private ArrayList<Vector3f> m_vertices;
	private ArrayList<Triangle> m_triangles;
	private AABB m_boundingBox;

	public GeometryData()
	{
		m_vertices = new ArrayList<Vector3f>();
		m_triangles = new ArrayList<Triangle>();
	}

	public void setBoundingBox(AABB box)
	{
		m_boundingBox = box;
	}

	public void addVertex(Vector3f v)
	{
		m_vertices.add(v);
	}

	public void addTriangle(int v0, int v1, int v2)
	{
		m_triangles.add(new Triangle(m_vertices.get(v0), m_vertices.get(v1), m_vertices.get(v2)));
	}

	public AABB getBoundingBox()
	{
		return m_boundingBox;
	}

	public ArrayList<Vector3f> getVertices()
	{
		return m_vertices;
	}

	public ArrayList<Triangle> getTriangles()
	{
		return m_triangles;
	}
}