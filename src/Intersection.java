public class Intersection
{
	private float m_distance;
	private Vector3f m_normal;
	private boolean m_isIntersect;
	private Vector3f m_color;
	private int m_objectIndex;

	public Intersection(float distance, Vector3f normal, Vector3f color, int index)
	{
		m_distance = distance;
		m_normal = normal.copy();
		m_isIntersect = true;
		m_color = color.copy();
		m_objectIndex = index;
	}

	public Intersection()
	{
		m_isIntersect = false;
		m_distance = -1.0f;
		m_objectIndex = -1;
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

	public Vector3f getColor()
	{
		return m_color;
	}

	public void print()
	{
		System.out.println("distance: " + m_distance + "\nnormal: " + m_normal.toString() + "\ncolor: " + m_color.toString() + "\nintersect: " + m_isIntersect);
	}
}