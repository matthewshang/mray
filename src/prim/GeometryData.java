package prim;

import java.util.ArrayList;

import math.AABB;
import math.Ray;
import math.Vector3f;
import oct.Octree;

public class GeometryData
{
	private ArrayList<Vector3f> m_vertices;
	private ArrayList<Triangle> m_triangles;
	private AABB m_boundingBox;
	private Octree m_octree;

	public GeometryData()
	{
		m_vertices = new ArrayList<Vector3f>();
		m_triangles = new ArrayList<Triangle>();
	}

	public void setBoundingBox(AABB box)
	{
		m_boundingBox = box;
	}

	public Intersection intersect(Ray ray)
	{
		return m_octree.intersect(ray);
	}

	public void buildTree()
	{
		Vector3f min = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		Vector3f max = new Vector3f(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);

		for (int i = 0; i < m_vertices.size(); i++)
		{
			m_vertices.get(i).sortMinAndMax(min, max);
		}

		// m_octree = new Octree(new AABB(min.sub(new Vector3f(0.01f, 0.01f, 0.01f)), 
									   // max.add(new Vector3f(0.01f, 0.01f, 0.01f))));
		m_octree = new Octree(new AABB(min, max));

		for (int i = 0; i < m_triangles.size(); i++)
		{
			m_octree.insert(m_triangles.get(i));			
		}
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

	public ArrayList<Triangle> getTriangles()
	{
		return m_triangles;
	}
}