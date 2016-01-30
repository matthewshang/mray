package prim;

import math.AABB;
import math.Ray;

public interface EngineObject
{
	public Intersection intersect(Ray ray);
	public AABB getBounds();
}