package prim;

import math.Ray;

public interface EngineObject
{
	public Intersection intersect(Ray ray);
}