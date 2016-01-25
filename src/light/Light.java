package light;

import math.Vector3f;
import math.Ray;
import prim.Intersection;

public interface Light
{
	public Vector3f getSample(Vector3f position);
	public Intersection intersect(Ray ray);
	public Vector3f getColor();
	public float getPower();
}