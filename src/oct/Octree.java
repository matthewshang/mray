package oct;

import java.util.ArrayList;

import math.AABB;
import math.Ray;
import prim.EngineObject;
import prim.Intersection;

public class Octree
{
	public static int MAX_DEPTH = 5;
	public static int MAX_OBJECTS_PER_NODE = 6;

	private OctreeNode m_root;

	public Octree(AABB bounds)
	{
		m_root = new OctreeNode(bounds, 1);
	}

	public void insert(EngineObject object)
	{
		m_root.insert(object);
	}

	public Intersection intersect(Ray ray)
	{
		Intersection min = m_root.intersect(ray);
		if (min.isIntersect())
		{
			return min;
		}
		else
		{
			return new Intersection();
		}
	}
}