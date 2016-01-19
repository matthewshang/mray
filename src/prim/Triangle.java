package prim;

public class Triangle
{
	private int[] m_vertices;

	public Triangle(int v0, int v1, int v2)
	{
		m_vertices = new int[]{v0, v1, v2};
	}

	public int[] getVertices()
	{
		return m_vertices;
	}
}