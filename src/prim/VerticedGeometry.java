package prim;

import java.util.ArrayList;

import math.Vector3f;

public class VerticedGeometry
{
	private ArrayList<Vector3f> m_vertices;
	private ArrayList<Triangle> m_triangles;

	public VerticedGeometry()
	{
		m_vertices = new ArrayList<Vector3f>();
		m_triangles = new ArrayList<Triangle>();
	}

	public void addVertex(Vector3f v)
	{
		m_vertices.add(v);
	}

	public void addTriangle(int v0, int v1, int v2)
	{
		m_triangles.add(new Triangle(v0, v1, v2));
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