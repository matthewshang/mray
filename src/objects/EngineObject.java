package objects;

import math.Intersection;
import math.Ray;

public interface EngineObject
{
	public Intersection intersect(Ray ray);
}