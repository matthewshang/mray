package oct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import math.AABB;
import math.Ray;
import math.Vector3f;
import prim.EngineObject;
import prim.Intersection;

public class OctreeNode
{
	private OctreeNode[] m_children = null;
	private ArrayList<EngineObject> m_objects;
	private AABB m_bounds;
	private int m_depth;

	public OctreeNode(AABB bounds, int depth)
	{
		m_objects = new ArrayList<EngineObject>();
		m_bounds = bounds;
		m_depth = depth;
	}

	public AABB getBounds()
	{
		return m_bounds;
	}

	public boolean isLeaf()
	{
		return m_children == null;
	}

	public Intersection intersect(Ray ray)
	{
		if (isLeaf())
		{
			Intersection min = new Intersection(Float.MAX_VALUE, Vector3f.zero(), null, -1);
			boolean intersected = false;
			for (int i = 0; i < m_objects.size(); i++)
			{
				Intersection inter = m_objects.get(i).intersect(ray);
				float d = inter.getDistance();

				if (d > 0f && d < min.getDistance())
				{
					min = inter;
					intersected = true;
				}
			}

			if (intersected)
			{
				return min;
			}
			else
			{
				return new Intersection();
			}
		}
		else
		{
			// System.out.println();

			HashMap<Float, Integer> map = new HashMap<Float, Integer>(8);
			float[] distances = new float[8];


			for (int i = 0; i < 8; i++)
			{
				float d = m_children[i].getBounds().intersect(ray);
				// System.out.println(m_children[i].getBounds().getMin() + " " + m_children[i].getBounds().getMax());
				distances[i] = d;
				if (d != 0f)
				{
					map.put(d, i);
				}
			}

			// System.out.println(map);

			Arrays.sort(distances);
			// System.out.println(Arrays.toString(distances));

			for (int i = 0; i < 8; i++)
			{
				if (map.containsKey(distances[i]))
				{
					// System.out.println(distances[i]);
					Intersection min = m_children[map.get(distances[i])].intersect(ray);
					if (min.isIntersect())
					{
						// System.out.println("i");
						return min;
					}
				}
			}

			return new Intersection();
		}
	}

	public void insert(EngineObject object)
	{
		if (isLeaf())
		{
			if (m_objects.size() <= Octree.MAX_OBJECTS_PER_NODE || m_depth == Octree.MAX_DEPTH)
			{
				m_objects.add(object);
			}
			else
			{
				Vector3f mid = m_bounds.getMax().getSub(m_bounds.getMin()).mul(0.5f).add(m_bounds.getMin());
				Vector3f[] vertices = m_bounds.getVertices();

				m_children = new OctreeNode[8];
				for (int i = 0; i < 8; i++)
				{
					Vector3f[] bounds = sortBounds(mid, vertices[i]);

					m_children[i] = new OctreeNode(new AABB(bounds[0], bounds[1]), m_depth + 1);
				}

				for (int i = 0; i < m_objects.size(); i++)
				{
					insertIntoChildren(m_objects.get(i));
				}

				insertIntoChildren(object);
			}
		}
		else
		{
			insertIntoChildren(object);
		}
	}

	private void insertIntoChildren(EngineObject object)
	{
		for (int i = 0; i < 8; i++)
		{
			if (m_children[i].getBounds().intersect(object.getBounds()))
			{
				m_children[i].insert(object);
			}
		}
	}

	public static Vector3f[] sortBounds(Vector3f m1, Vector3f m2)
	{
		Vector3f min = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		Vector3f max = new Vector3f(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);
		m1.sortMinAndMax(min, max);
		m2.sortMinAndMax(min, max);
		return new Vector3f[]{min, max};
	}
}